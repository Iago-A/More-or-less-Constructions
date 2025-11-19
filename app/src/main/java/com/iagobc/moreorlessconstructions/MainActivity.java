package com.iagobc.moreorlessconstructions;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain views references
        Button startButton = findViewById(R.id.start_button);
        TextView tutorialTextView = findViewById(R.id.tutorial_text_view);
        FloatingActionButton extrasFab = findViewById(R.id.extras_fab);

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        tutorialTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
        });

        extrasFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExtraActivity.class);
            startActivity(intent);
        });
    }
}