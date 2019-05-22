package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;

public class SimpleBibleScreen
    extends AppCompatActivity
    implements SimpleBibleScreenOps,
               HomeScreen.FragmentInteractionListener {

  private static final String TAG = "SimpleBibleScreen";

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setTheme(R.style.SbTheme);
    setContentView(R.layout.simple_bible_screen);
  }

}
