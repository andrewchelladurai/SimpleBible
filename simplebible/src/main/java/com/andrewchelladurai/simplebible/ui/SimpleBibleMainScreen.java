package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.Intent;
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

public class SimpleBibleMainScreen
    extends AppCompatActivity
    implements SplashScreenOps {

    private static final String                TAG        = "SimpleBibleMainScreen";
    private static       SplashScreenPresenter mPresenter = null;

    private TextView    mVerse;
    private TextView    mMessage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVerse = findViewById(R.id.act_main_verse);
        mMessage = findViewById(R.id.act_main_msg);
        mProgressBar = findViewById(R.id.act_main_pbar);

        if (mPresenter == null) {
            mPresenter = new SplashScreenPresenter(this);
        }

        findViewById(R.id.act_fab_books).setOnClickListener(this);
        findViewById(R.id.act_fab_search).setOnClickListener(this);
        findViewById(R.id.act_fab_bookmarks).setOnClickListener(this);
        findViewById(R.id.act_fab_settings).setOnClickListener(this);

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
                getString(R.string.act_main_splash_verse), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mVerse.setText(Html.fromHtml(getString(R.string.act_main_splash_verse)));
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
                getString(R.string.act_main_splash_verse_err), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mVerse.setText(Html.fromHtml(getString(R.string.act_main_splash_verse_err)));
        }
    }

    @Override
    public Loader<Boolean> onCreateLoader(final int id, final Bundle args) {
        return new SplashScreenPresenter.DbSetupAsyncTask();
    }

    @Override
    public void onLoadFinished(final Loader<Boolean> loader, final Boolean data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (data) {
            mMessage.setVisibility(View.INVISIBLE);
            findViewById(R.id.act_main_container_fabs).setVisibility(View.VISIBLE);
        } else {
            mMessage.setText(R.string.act_main_splash_msg_err);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Boolean> loader) {
        Log.e(TAG, "onLoaderReset:");
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_fab_books:
                handleFabClickBooks();
                break;
            case R.id.act_fab_search:
                handleFabClickSearch();
                break;
            case R.id.act_fab_bookmarks:
                handleFabClickBookmarks();
                break;
            case R.id.act_fab_settings:
                handleFabClickSettings();
                break;
            default:
                Log.d(TAG, "onClick: unhandled click event from view");
        }
    }

    private void handleFabClickSettings() {
        Log.d(TAG, "handleFabClickSettings() called");
    }

    private void handleFabClickBookmarks() {
        Log.d(TAG, "handleFabClickBookmarks() called");
    }

    private void handleFabClickSearch() {
        Log.d(TAG, "handleFabClickSearch() called");
    }

    private void handleFabClickBooks() {
        startActivity(new Intent(this, BooksScreen.class));
    }
}
