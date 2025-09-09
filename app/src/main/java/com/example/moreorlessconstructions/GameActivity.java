package com.example.moreorlessconstructions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Obtain views reference
        ImageView topImageView = findViewById(R.id.top_image_view);
        ImageView bottomImageView = findViewById(R.id.bottom_image_view);
        TextView topNameTextView = findViewById(R.id.top_name_text_view);
        TextView topCountryTextView = findViewById(R.id.top_country_text_view);
        TextView topHeightTextView = findViewById(R.id.top_height_text_view);
        TextView bottomNameTextView = findViewById(R.id.bottom_name_text_view);
        TextView bottomCountryTextView = findViewById(R.id.bottom_country_text_view);
        TextView bottomHeightTextView = findViewById(R.id.bottom_height_text_view);

        List<Construction> constructions;
        Random random = new Random();
        int randomNumber1 = 0;
        int randomNumber2 = 0;
        int counter = 0;

        // Load the constructions data
        constructions = getConstructionsFromJson();

        // Choose different random constructions
        randomNumber1 = random.nextInt(constructions.size());
        do {
            randomNumber2 = random.nextInt(constructions.size());
        } while(randomNumber1 == randomNumber2);

        Construction cons1 = constructions.get(randomNumber1);
        String name1 = cons1.getName();
        int height1 = cons1.getHeight();
        String image1 = cons1.getImage();
        String country1 = cons1.getCountry();

        Construction cons2 = constructions.get(randomNumber2);
        String name2 = cons2.getName();
        int height2 = cons2.getHeight();
        String image2 = cons2.getImage();
        String country2 = cons2.getCountry();

        // Set construction data in the screen
        int resId1 = getResources().getIdentifier(image1, "drawable", getPackageName());
        topImageView.setImageResource(resId1);
        topNameTextView.setText(name1);
        topCountryTextView.setText(country1);

        int resId2 = getResources().getIdentifier(image2, "drawable", getPackageName());
        bottomImageView.setImageResource(resId2);
        bottomNameTextView.setText(name2);
        bottomCountryTextView.setText(country2);

        topImageView.setOnClickListener(v -> {
            showHeight(topHeightTextView, height1);
            gameOver = checkResult(height1, height2);
        });

        bottomImageView.setOnClickListener(v -> {
            showHeight(bottomHeightTextView, height2);
            gameOver = checkResult(height2, height1);
        });
    }

    public List<Construction> getConstructionsFromJson (){
        // List to save each construction as an object
        List<Construction> constructions = new ArrayList<>();

        // Open an Input Stream to the raw file
        InputStream inputStream = getResources().openRawResource(R.raw.constructions);

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            // String builder to add the lines to it
            StringBuilder builder = new StringBuilder();
            String line;

            // Read line by line and add to the String builder
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Convert all text to String
            String jsonString = builder.toString();

            // Parse to JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);

            // Save the constructions from the JSON into the list
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String name = object.getString("name");
                int height = object.getInt("height");
                String image = object.getString("image");
                String country = object.getString("country");

                Construction cons = new Construction(name, height, image, country);

                constructions.add(cons);
            }
        }catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return constructions;
    }

    public void showHeight(TextView heightTextView, int height) {
        int start = (int) (0.7 * height);

        ValueAnimator animator = ValueAnimator.ofInt(start, height - 1);
        animator.setDuration(1500); // Main duration
        animator.setInterpolator(new android.view.animation.PathInterpolator(0.2f, 0f, 0f, 1f));

        animator.addUpdateListener(animation -> {
            int currentValue = (int) animation.getAnimatedValue();
            heightTextView.setText(String.valueOf(currentValue));
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Show real height whit a little delay
                heightTextView.postDelayed(() -> {
                    heightTextView.setText(String.valueOf(height));

                    // Bounce animation at the end
                    ObjectAnimator bounce = ObjectAnimator.ofFloat(heightTextView, "scaleY", 1f, 1.3f, 1f);
                    bounce.setDuration(500);
                    bounce.setInterpolator(new BounceInterpolator());
                    bounce.start();

                    ObjectAnimator bounceX = ObjectAnimator.ofFloat(heightTextView, "scaleX", 1f, 1.3f, 1f);
                    bounceX.setDuration(500);
                    bounceX.setInterpolator(new BounceInterpolator());
                    bounceX.start();

                }, 400); // Final delay
            }
        });

        animator.start();
    }

    public boolean checkResult (int chosenHeight, int leftoverHeight) {
        boolean gameOver = false;

        if (chosenHeight < leftoverHeight){
            gameOver = true;
        }

        return gameOver;
    }

}
