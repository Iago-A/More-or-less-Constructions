package com.example.moreorlessconstructions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.BounceInterpolator;
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

// In game replacement rules:
// - The loser is usually replaced
// - If the winner has won 2 consecutive rounds (winStreak), the winner is replaced

public class GameActivity extends AppCompatActivity {
    private int randomNumber1;
    private int randomNumber2;
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
    private TextView topNameTextView;
    private TextView topCountryTextView;
    private TextView topHeightTextView;
    private TextView bottomNameTextView;
    private TextView bottomCountryTextView;
    private TextView bottomHeightTextView;
    private TextView counterTextView;

    private boolean topHeightShown;
    private int winStreak;
    private Boolean topWonLastRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Obtain views reference
        topImageView = findViewById(R.id.top_image_view);
        bottomImageView = findViewById(R.id.bottom_image_view);
        topNameTextView = findViewById(R.id.top_name_text_view);
        topCountryTextView = findViewById(R.id.top_country_text_view);
        topHeightTextView = findViewById(R.id.top_height_text_view);
        bottomNameTextView = findViewById(R.id.bottom_name_text_view);
        bottomCountryTextView = findViewById(R.id.bottom_country_text_view);
        bottomHeightTextView = findViewById(R.id.bottom_height_text_view);
        counterTextView = findViewById(R.id.counterTextView);

        List<Construction> constructions;
        Random random = new Random();
        gameOver = false;
        hitsCounter = 0;
        winStreak = 0;
        topWonLastRound = null;

        // Load the constructions data
        constructions = getConstructionsFromJson();

        // Choose different random constructions
        randomNumber1 = random.nextInt(constructions.size());
        do {
            randomNumber2 = random.nextInt(constructions.size());
        } while(randomNumber1 == randomNumber2);

        loadConstruction(constructions, randomNumber1, true);
        loadConstruction(constructions, randomNumber2, false);

        topHeightTextView.setText((height1) + " m");
        counterTextView.setText(Integer.toString(hitsCounter));

        topHeightShown = true;

        // Establish click listeners
        topImageView.setOnClickListener(v -> {
            // Deactivate temporarily the click function
            topImageView.setEnabled(false);
            bottomImageView.setEnabled(false);

            checkResult(height1, height2);

            showHeight(() -> {
                // Callback when animation end
                if (!gameOver) {
                    updateCounter();
                    startNewRound(constructions);
                } else {
                    endGame();
                }
            });
        });

        bottomImageView.setOnClickListener(v -> {
            // Deactivate temporarily the click function
            topImageView.setEnabled(false);
            bottomImageView.setEnabled(false);

            checkResult(height2, height1);

            showHeight(() -> {
                // Callback when animation end
                if (!gameOver) {
                    updateCounter();
                    startNewRound(constructions);
                } else {
                    endGame();
                }
            });
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


    public void loadConstruction(List<Construction> constructions, int index, boolean isTop) {
        // Get chosen construction from the constructions list
        Construction cons = constructions.get(index);

        // New top construction
        if (isTop) {
            name1 = cons.getName();
            height1 = cons.getHeight();
            image1 = cons.getImage();
            country1 = cons.getCountry();

            // Set construction data in the screen
            int resId1 = getResources().getIdentifier(image1, "drawable", getPackageName());
            topImageView.setImageResource(resId1);
            topNameTextView.setText(name1);
            topCountryTextView.setText(country1);
        }
        // New bottom construction
        else {
            name2 = cons.getName();
            height2 = cons.getHeight();
            image2 = cons.getImage();
            country2 = cons.getCountry();

            // Set construction data in the screen
            int resId2 = getResources().getIdentifier(image2, "drawable", getPackageName());
            bottomImageView.setImageResource(resId2);
            bottomNameTextView.setText(name2);
            bottomCountryTextView.setText(country2);
        }
    }


    public void showHeight(Runnable onAnimationEnd) {
        if (topHeightShown) {
            // Show bottom construction height
            animateHeight(bottomHeightTextView, height2, onAnimationEnd);
        } else {
            // Show top construction height
            animateHeight(topHeightTextView, height1, onAnimationEnd);
        }
    }


    private void animateHeight(TextView heightTextView, int height, Runnable onAnimationEnd) {
        int start = (int) (0.7 * height);

        ValueAnimator animator = ValueAnimator.ofInt(start, height - 1);
        animator.setDuration(1500); // Main duration
        animator.setInterpolator(new android.view.animation.PathInterpolator(0.2f, 0f, 0f, 1f));

        animator.addUpdateListener(animation -> heightTextView.setText(animation.getAnimatedValue() + " m"));

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                heightTextView.postDelayed(() -> {
                    heightTextView.setText(height + " m");

                    // Bounce X and Y whit AnimatorSet
                    ObjectAnimator bounceY = ObjectAnimator.ofFloat(heightTextView, "scaleY", 1f, 1.3f, 1f);
                    ObjectAnimator bounceX = ObjectAnimator.ofFloat(heightTextView, "scaleX", 1f, 1.3f, 1f);

                    AnimatorSet bounceSet = new AnimatorSet();
                    bounceSet.playTogether(bounceX, bounceY);
                    bounceSet.setDuration(500);
                    bounceSet.setInterpolator(new BounceInterpolator());

                    // Play sound effect
                    if (gameOver) {
                        playSound(R.raw.bass);
                    }
                    else {
                        playSound(R.raw.win_sound);
                    }

                    // Execute callback (only when bounce is over)
                    bounceSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            topImageView.setEnabled(true);
                            bottomImageView.setEnabled(true);
                            if (onAnimationEnd != null) onAnimationEnd.run();
                        }
                    });

                    bounceSet.start();

                }, 400);
            }
        });

        animator.start();
    }


    public void checkResult (int chosenHeight, int leftoverHeight) {
        // gameOver starts being false

        if (chosenHeight < leftoverHeight){
            gameOver = true;
        }
    }


    public void updateCounter () {
        hitsCounter += 1;
        counterTextView.setText(Integer.toString(hitsCounter));
    }


    private void startNewRound(List<Construction> constructions) {
        Random random = new Random();
        boolean topIsHigher;

        // See the current round winner
        if (height1 > height2) {
            topIsHigher = true;
        }
        else {
            topIsHigher = false;
        }

        // Update the consecutive wins counter
        if (topWonLastRound != null && topWonLastRound == topIsHigher) {
            winStreak += 1;
        } else {
            winStreak = 1;
        }

        // Update the winner
        topWonLastRound = topIsHigher;

        // Choose the new construction for the next round
        int newIndex;
        do {
            newIndex = random.nextInt(constructions.size());
        } while (newIndex == randomNumber1 || newIndex == randomNumber2);

        // Replacement rules:
        // - The loser is usually replaced
        // - If the winner has won 2 consecutive rounds (winStreak), the winner is replaced
        if (winStreak == 2) {
            // Replace the winner
            if (topIsHigher) {
                randomNumber1 = newIndex;
                loadConstruction(constructions, randomNumber1, true);

                topHeightTextView.setText("");
                topHeightShown = false;
            } else {
                randomNumber2 = newIndex;
                loadConstruction(constructions, randomNumber2, false);

                bottomHeightTextView.setText("");
                topHeightShown = true;
            }
            winStreak = 0;
        } else {
            // Replace the loser
            if (topIsHigher) {
                randomNumber2 = newIndex;
                loadConstruction(constructions, randomNumber2, false);

                bottomHeightTextView.setText("");
                topHeightShown = true;
            } else {
                randomNumber1 = newIndex;
                loadConstruction(constructions, randomNumber1, true);

                topHeightTextView.setText("");
                topHeightShown = false;
            }
        }
    }


    private void endGame() {
        Toast.makeText(this, "¡Game Over!", Toast.LENGTH_SHORT).show();
    }


    private void playSound(int resId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release(); // Release resources when finished
        });
    }

}
