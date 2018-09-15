package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;

import com.andrewchelladurai.simplebible.R;

import androidx.appcompat.app.AppCompatActivity;

public class SimpleBibleMainScreen
    extends AppCompatActivity {

    private static final String TAG = "SimpleBibleMainScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Home);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);

    }

}
