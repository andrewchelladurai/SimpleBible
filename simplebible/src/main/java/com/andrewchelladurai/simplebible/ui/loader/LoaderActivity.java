package com.andrewchelladurai.simplebible.ui.loader;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;

public class LoaderActivity
    extends AppCompatActivity
    implements LoaderActivityOps {

    private static final String TAG = "LoaderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_simplebible);

        TextView tvVerse = findViewById(R.id.act_load_tv_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvVerse.setText(Html.fromHtml(getString(R.string.act_load_verse),
                                          Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvVerse.setText(Html.fromHtml(getString(R.string.act_load_verse)));
        }

        initDatabase();
    }

    private void initDatabase() {
        Log.d(TAG, "initDatabase() called");

    }

    @NonNull
    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }
}
