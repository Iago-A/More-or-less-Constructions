package com.example.moreorlessconstructions;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExtraActivity extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Obtain views references
        FloatingActionButton returnFab = findViewById(R.id.return_fab);
        Button resetRecordButton = findViewById(R.id.reset_record_button);
        Button aboutAppButton = findViewById(R.id.about_app_button);
        TextView versionTextView = findViewById(R.id.version_text_view);

        showAppVersion(versionTextView);

        returnFab.setOnClickListener(v -> {
            finish();
        });

        resetRecordButton.setOnClickListener(v -> {
            new AlertDialog.Builder(ExtraActivity.this)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.reset_record_warning)
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        resetRecord();
                        Toast.makeText(ExtraActivity.this, R.string.reset_record_success, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

            // Centrar mensaje
            // Colores a los botones????
        });

        aboutAppButton.setOnClickListener(v -> {
            // Terminar esto Iago!!
        });

    }

    public void showAppVersion (TextView versionTextView) {
        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0)
                    .versionName;
            versionTextView.setText("Version " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionTextView.setText("Version N/A");
        }
    }

    public void resetRecord () {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("best_score", 0);
        editor.apply();
    }
}
