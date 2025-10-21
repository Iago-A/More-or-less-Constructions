package com.iagobc.moreorlessconstructions;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Obtain views references
        TextView scoreTittleTextView = findViewById(R.id.score_tittle_text_view);
        TextView scoreNumberTextView = findViewById(R.id.score_number_text_view);
        TextView gameOverTextView = findViewById(R.id.game_over_text_view);
        Button playAgainButton = findViewById(R.id.play_again_button);
        Button mainMenuButton = findViewById(R.id.main_menu_button);

        int score = getIntent().getIntExtra("score", 0);

        // Set values to text views
        scoreNumberTextView.setText(String.valueOf(score));

        if (score == 0) {
            gameOverTextView.setText(getString(R.string.game_over_1));
        }
        else {
            if (score <= 4) {
                gameOverTextView.setText(getString(R.string.game_over_2));
            }
            else {
                if (score <= 9) {
                    gameOverTextView.setText(getString(R.string.game_over_3));
                }
                else {
                    if (score <= 19) {
                        gameOverTextView.setText(getString(R.string.game_over_4));
                    }
                    else {
                        gameOverTextView.setText(getString(R.string.game_over_5));
                    }
                }
            }
        }

        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
            startActivity(intent);
            finish(); // To avoid the user could return using the back phone button
        });

        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // To avoid the user could return using the back phone button
        });

    }
}
