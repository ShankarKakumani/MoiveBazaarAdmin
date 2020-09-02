package com.sparky.moivebazaaradmin.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.sparky.moivebazaaradmin.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class IntroActivity extends AppCompatActivity {

    TextView textDisplay;
    ProgressBar progress_indeterminate;

    Uri uriNotification;
    private Context mContext;
    private static final int RC_HINT = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        //statusBarColor();
        textDisplay = findViewById(R.id.textDisplay);
        progress_indeterminate = findViewById(R.id.progress_indeterminate);

        //startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        //finish();



        int SPLASH_TIME_OUT = 500;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }, SPLASH_TIME_OUT);

    }


    private void sharedPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name", "Shankar");
        editor.apply();

        String name = preferences.getString("Name", "");
        if (!name.equalsIgnoreCase("")) {
            name = name + "  Sethi";  /* Edit the value here*/

            textDisplay.setText(name);

        }

        String value = preferences.getString("Name", null);
        if (value == null) {
            // the key does not exist
            textDisplay.setText("null");

        } else {
            // handle the value
            textDisplay.setText(value);

            editor.clear();
            editor.commit();
        }

    }


    private void statusBarColor() {

        Window window = IntroActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(IntroActivity.this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Window window = IntroActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(IntroActivity.this, R.color.black));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //statusBarColor();

    }
}
