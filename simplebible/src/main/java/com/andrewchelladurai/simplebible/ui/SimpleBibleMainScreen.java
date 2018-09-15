package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.presenter.DbSetupLoader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

public class SimpleBibleMainScreen
    extends AppCompatActivity {

    private static final String TAG = "SimpleBibleMainScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Home);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);

        // Load database
        DbSetupLoaderCallback dbSetupLoaderCallback = new DbSetupLoaderCallback();
        LoaderManager.getInstance(this)
                     .initLoader(R.integer.DB_LOADER, null, dbSetupLoaderCallback)
                     .forceLoad();

        showLoadingScreen();
    }

    private void showLoadingScreen() {
        Log.d(TAG, "showLoadingScreen: ");
    }

    private void showFailedScreen() {
        Log.d(TAG, "showFailedScreen: ");
    }

    private void showDailyVerse() {
        Log.d(TAG, "showDailyVerse: ");
    }

    private void showSuccessScreen() {
        Log.d(TAG, "showSuccessScreen: ");
    }

    private class DbSetupLoaderCallback
        implements LoaderCallbacks<Boolean> {

        @Override
        public Loader<Boolean> onCreateLoader(final int id, final Bundle args) {
            Log.d(TAG, "onCreateLoader: ");
            return new DbSetupLoader(SimpleBibleMainScreen.this);
        }

        @Override
        public void onLoadFinished(final Loader<Boolean> loader, final Boolean loaded) {
            if (loaded) {
                showDailyVerse();
                showSuccessScreen();
            } else {
                showFailedScreen();
            }
        }

        @Override
        public void onLoaderReset(final Loader<Boolean> loader) {
            Log.d(TAG, "onLoaderReset: ");
            showFailedScreen();
        }
    }

}
