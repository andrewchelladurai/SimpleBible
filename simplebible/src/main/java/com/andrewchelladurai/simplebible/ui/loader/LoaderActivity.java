package com.andrewchelladurai.simplebible.ui.loader;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.main.MainActivity;

public class LoaderActivity
    extends AppCompatActivity
    implements LoaderActivityOps {

    private static final String TAG = "LoaderActivity";
    private static LoaderActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Loader);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_simplebible);
        if (presenter == null) {
            presenter = LoaderActivityPresenter.getInstance();
        }
        showLoadingScreen();
        getSupportLoaderManager().initLoader(16, null, this).forceLoad();
    }

    @NonNull
    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }

    @Override
    public void showLoadingScreen() {
        TextView tvVerse = findViewById(R.id.act_load_tv_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvVerse.setText(Html.fromHtml(getString(R.string.act_load_verse),
                                          Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvVerse.setText(Html.fromHtml(getString(R.string.act_load_verse)));
        }
    }

    @Override
    public void showFailedScreen() {
        TextView tvVerse = findViewById(R.id.act_load_tv_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvVerse.setText(Html.fromHtml(getString(R.string.act_load_verse_err),
                                          Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvVerse.setText(Html.fromHtml(getString(R.string.act_load_verse_err)));
        }
        findViewById(R.id.act_load_progress).setVisibility(View.INVISIBLE);
        tvVerse = findViewById(R.id.act_load_tv_message);
        tvVerse.setText(R.string.act_load_msg_err);
    }

    @Override
    public void showMainScreen() {
        Log.e(TAG, "showMainScreen() called");
        TextView tvVerse = findViewById(R.id.act_load_tv_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvVerse.setText(Html.fromHtml(getString(R.string.default_verse_with_reference),
                                          Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvVerse.setText(Html.fromHtml(getString(R.string.default_verse_with_reference)));
        }
        findViewById(R.id.act_load_progress).setVisibility(View.INVISIBLE);
        tvVerse = findViewById(R.id.act_load_tv_message);
        tvVerse.setText(R.string.act_load_msg_success);
        startActivity(new Intent(this, MainActivity.class));
    }

    @NonNull
    @Override
    public AsyncTaskLoader<Boolean> onCreateLoader(final int i, @Nullable final Bundle bundle) {
        return new LoaderActivityPresenter.DbAsyncLoader(getSystemContext());
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<Boolean> loader,
                               final Boolean successfullyLoaded) {
        if (successfullyLoaded) {
            showMainScreen();
        } else {
            showFailedScreen();
        }
    }

    @Override
    public void onLoaderReset(@NonNull final Loader<Boolean> loader) {
    }
}
