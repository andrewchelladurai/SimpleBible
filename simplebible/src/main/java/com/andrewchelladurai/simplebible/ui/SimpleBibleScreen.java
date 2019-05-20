package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.andrewchelladurai.simplebible.R;

public class SimpleBibleScreen extends AppCompatActivity implements HomeScreen.FragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_bible_screen);
    }
}
