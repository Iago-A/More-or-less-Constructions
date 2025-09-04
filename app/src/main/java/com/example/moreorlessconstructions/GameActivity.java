package com.example.moreorlessconstructions;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

//            Log.i("MiApp", Integer.toString(jsonArray.length()));
//
//            JSONObject object = jsonArray.getJSONObject(2);
//            String name = object.getString("name");
//            Log.i("MiApp", name);

        }catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
