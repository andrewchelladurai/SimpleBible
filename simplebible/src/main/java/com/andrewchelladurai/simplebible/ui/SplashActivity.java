package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;

import com.andrewchelladurai.simplebible.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity
    extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
