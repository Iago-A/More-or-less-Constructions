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
    private boolean gameOver;
    private String name1;
    private int height1;
    private String image1;
    private String country1;
    private String name2;
    private int height2;
    private String image2;
    private String country2;
    private int hitsCounter;
    private ImageView topImageView;
    private ImageView bottomImageView;
    private boolean topSelected;
    private int higherConstructionCounter;
    private boolean topHeightShown;
    private boolean bottomHeightShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Obtain views reference
        topImageView = findViewById(R.id.top_image_view);
        bottomImageView = findViewById(R.id.bottom_image_view);
        TextView topNameTextView = findViewById(R.id.top_name_text_view);
        TextView topCountryTextView = findViewById(R.id.top_country_text_view);
        TextView topHeightTextView = findViewById(R.id.top_height_text_view);
        TextView bottomNameTextView = findViewById(R.id.bottom_name_text_view);
        TextView bottomCountryTextView = findViewById(R.id.bottom_country_text_view);
        TextView bottomHeightTextView = findViewById(R.id.bottom_height_text_view);
        TextView counterTextView = findViewById(R.id.counterTextView);

        List<Construction> constructions;
        Random random = new Random();
        int randomNumber1 = 0;
        int randomNumber2 = 0;
        gameOver = false;
        hitsCounter = 0;
        higherConstructionCounter = 0;

        // Load the constructions data
        constructions = getConstructionsFromJson();

        // Choose different random constructions
        randomNumber1 = random.nextInt(constructions.size());
        do {
            randomNumber2 = random.nextInt(constructions.size());
        } while(randomNumber1 == randomNumber2);

        loadConstruction(constructions, randomNumber1, true);
        loadConstruction(constructions, randomNumber2, false);

        // Set construction data and hitsCounter in the screen
        int resId1 = getResources().getIdentifier(image1, "drawable", getPackageName());
        topImageView.setImageResource(resId1);
        topNameTextView.setText(name1);
        topCountryTextView.setText(country1);
        topHeightTextView.setText((height1) + " m");
        topHeightShown = true;

        int resId2 = getResources().getIdentifier(image2, "drawable", getPackageName());
        bottomImageView.setImageResource(resId2);
        bottomNameTextView.setText(name2);
        bottomCountryTextView.setText(country2);
        bottomHeightShown = false;

        counterTextView.setText(Integer.toString(hitsCounter));

        // Establish click listeners
        topImageView.setOnClickListener(v -> {
            // Deactivate temporarily the click function
            topImageView.setEnabled(false);
            bottomImageView.setEnabled(false);

            // At first round, top construction height is always visible
            //Therefore, we will shown the bottom construction height
            showHeight(bottomHeightTextView, height1, () -> {
                // Callback when animation end
                gameOver = checkResult(height1, height2, counterTextView);

                if (!gameOver) {
                    startNewRound();
                } else {
                    endGame();
                }
            });
        });

        bottomImageView.setOnClickListener(v -> {
            // Deactivate temporarily the click function
            topImageView.setEnabled(false);
            bottomImageView.setEnabled(false);

            showHeight(bottomHeightTextView, height2, () -> {
                // Callback when animation end
                gameOver = checkResult(height2, height1, counterTextView);

                if (!gameOver) {
                    startNewRound();
                } else {
                    endGame();
                }
            });
        });
    }


    public void loadConstruction(List<Construction> constructions, int index, boolean isTop) {
        // Get chosen construction from the constructions list
        Construction cons = constructions.get(index);

        if (isTop) {
            name1 = cons.getName();
            height1 = cons.getHeight();
            image1 = cons.getImage();
            country1 = cons.getCountry();
        } else {
            name2 = cons.getName();
            height2 = cons.getHeight();
            image2 = cons.getImage();
            country2 = cons.getCountry();
        }
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


    public void showHeight(TextView heightTextView, int height, Runnable onAnimationEnd) {
        int start = (int) (0.7 * height);

        ValueAnimator animator = ValueAnimator.ofInt(start, height - 1);
        animator.setDuration(1500); // Main duration
        animator.setInterpolator(new android.view.animation.PathInterpolator(0.2f, 0f, 0f, 1f));

        animator.addUpdateListener(animation -> {
            int currentValue = (int) animation.getAnimatedValue();
            heightTextView.setText(currentValue + " m");
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Show real height whit a little delay
                heightTextView.postDelayed(() -> {
                    heightTextView.setText(height + " m");

                    // Bounce animation at the end
                    ObjectAnimator bounce = ObjectAnimator.ofFloat(heightTextView, "scaleY", 1f, 1.3f, 1f);
                    bounce.setDuration(500);
                    bounce.setInterpolator(new BounceInterpolator());
                    bounce.start();

                    ObjectAnimator bounceX = ObjectAnimator.ofFloat(heightTextView, "scaleX", 1f, 1.3f, 1f);
                    bounceX.setDuration(500);
                    bounceX.setInterpolator(new BounceInterpolator());
                    bounceX.start();

                    // Reactivate clicks
                    topImageView.setEnabled(true);
                    bottomImageView.setEnabled(true);

                    // Execute callback when animation ends
                    if (onAnimationEnd != null) onAnimationEnd.run();

                }, 400); // Final delay
            }
        });

        animator.start();
    }


    public boolean checkResult (int chosenHeight, int leftoverHeight, TextView counterTextView) {
        boolean gameOver = false;

        if (chosenHeight < leftoverHeight){
            gameOver = true;
        }
        else {
            hitsCounter += 1;
            counterTextView.setText(Integer.toString(hitsCounter));
        }

        return gameOver;
    }


    private void startNewRound() {

    }


    private void endGame() {
        Toast.makeText(this, "¡Game Over!", Toast.LENGTH_SHORT).show();
    }

}
