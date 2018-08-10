package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.presenter.SplashScreenPresenter;
import com.andrewchelladurai.simplebible.ui.ops.SplashScreenOps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

public class SplashScreen
    extends AppCompatActivity
    implements SplashScreenOps {

    private static final String                TAG        = "SplashScreen";
    private static       SplashScreenPresenter mPresenter = null;

    private TextView    mVerse;
    private TextView    mMessage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mVerse = findViewById(R.id.tv_verse_act_splash);
        mMessage = findViewById(R.id.tv_msg_act_splash);
        mProgressBar = findViewById(R.id.pb_act_splash);

        if (mPresenter == null) {
            mPresenter = new SplashScreenPresenter(this);
        }

        showLoadingScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatabase();
    }

    private void initDatabase() {
        getSupportLoaderManager().initLoader(R.integer.DB_LOADER, null, this).forceLoad();
    }

    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }

    @Override
    public void updateProgress(final Integer value) {
        mMessage.setText(String.valueOf(value));
    }

    @Override
    public void showLoadingScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mVerse.setText(Html.fromHtml(
                getString(R.string.tv_verse_act_splash), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mVerse.setText(Html.fromHtml(getString(R.string.tv_verse_act_splash)));
        }
    }

    @Override
    public void showNextScreen() {
        Log.d(TAG, "showNextScreen: ");
    }

    @Override
    public void showLoadingFailureScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mVerse.setText(Html.fromHtml(
                getString(R.string.tv_verse_act_splash_err), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mVerse.setText(Html.fromHtml(getString(R.string.tv_verse_act_splash_err)));
        }
    }

    @Override
    public Loader<Boolean> onCreateLoader(final int id, final Bundle args) {
        return new SplashScreenPresenter.DbSetupAsyncTask();
    }

    @Override
    public void onLoadFinished(final Loader<Boolean> loader, final Boolean data) {
        if (data) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mMessage.setText(R.string.tv_msg_act_splash_success);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Boolean> loader) {
        Log.e(TAG, "onLoaderReset:");
    }
}
