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
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.BookRepository;
import com.andrewchelladurai.simplebible.data.repository.BookmarkRepository;
import com.andrewchelladurai.simplebible.data.repository.BookmarkVerseRepository;
import com.andrewchelladurai.simplebible.data.repository.SearchRepository;
import com.andrewchelladurai.simplebible.data.repository.VerseRepository;
import com.andrewchelladurai.simplebible.presenter.SplashScreenPresenter;
import com.andrewchelladurai.simplebible.ui.ops.SplashScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class SimpleBibleMainScreen
    extends AppCompatActivity
    implements SplashScreenOps {

    private static final String TAG = "SimpleBibleMainScreen";

    private static SplashScreenPresenter mPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Home);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);

        if (mPresenter == null) {
            mPresenter = new SplashScreenPresenter(this);
        }

        findViewById(R.id.act_main_fab_books).setOnClickListener(this);
        findViewById(R.id.act_main_fab_search).setOnClickListener(this);
        findViewById(R.id.act_main_fab_bookmarks).setOnClickListener(this);
        findViewById(R.id.act_main_fab_settings).setOnClickListener(this);

        // FIXME: 9/9/18 Daily Verse does not show after DB is loaded and screen rotated

        showLoadingScreen();
        loadDatabase();
    }

    private void loadDatabase() {
        Log.d(TAG, "loadDatabase: loader initiated");
        LoaderManager.getInstance(this)
                     .initLoader(R.integer.DB_LOADER, null, new DatabaseLoaderCallback())
                     .forceLoad();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_main_fab_books:
                handleInteractionBooks();
                break;
            case R.id.act_main_fab_search:
                handleInteractionSearch();
                break;
            case R.id.act_main_fab_bookmarks:
                handleInteractionBookmarks();
                break;
            case R.id.act_main_fab_settings:
                handleInteractionSettings();
                break;
            default:
                Log.d(TAG, "onClick: unhandled click event from view");
        }
    }

    private void handleInteractionSettings() {
        Log.d(TAG, "handleInteractionSettings() called");
    }

    private void handleInteractionBookmarks() {
        startActivity(new Intent(this, BookmarkListScreen.class));
    }

    private void handleInteractionSearch() {
        startActivity(new Intent(this, SearchScreen.class));
    }

    private void handleInteractionBooks() {
        startActivity(new Intent(this, BooksScreen.class));
    }

    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }

    public void showLoadingScreen() {
        Log.d(TAG, "showLoadingScreen");
        TextView textView = findViewById(R.id.act_main_verse);
        textView.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(
                getString(R.string.act_main_splash_verse), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(getString(R.string.act_main_splash_verse)));
        }

        textView = findViewById(R.id.act_main_msg);
        textView.setVisibility(View.VISIBLE);

        ProgressBar progressBar = findViewById(R.id.act_main_pbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stopLoadingScreen() {
        Log.d(TAG, "stopLoadingScreen");
        ProgressBar progressBar = findViewById(R.id.act_main_pbar);
        progressBar.setVisibility(View.INVISIBLE);

        TextView textView = findViewById(R.id.act_main_msg);
        textView.setVisibility(View.INVISIBLE);
    }

    public String getDefaultDailyVerseReference() {
        return getString(R.string.default_daily_verse_reference);
    }

    public String[] getDefaultDailyVerseReferenceArray() {
        return getResources().getStringArray(R.array.daily_verse_list);
    }

    public void initRepositories() {
        Log.d(TAG, "initRepositories");
        final BookRepository bookRepository = ViewModelProviders.of(this).get(BookRepository.class);
        bookRepository.queryAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(final List<Book> list) {
                if (list == null || list.isEmpty()) {
                    Log.e(TAG, "populateBooksCache: empty liveData for Books");
                    return;
                }
                bookRepository.populateCache(
                    list, 66, getString(R.string.first_book), getString(R.string.last_book));
            }
        });

        final BookmarkRepository bookmarkRepository =
            ViewModelProviders.of(this).get(BookmarkRepository.class);
        bookmarkRepository.queryAllBookmarks().observe(this, new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(final List<Bookmark> list) {
                bookmarkRepository.populateCache(list);
            }
        });

        ViewModelProviders.of(this).get(BookmarkVerseRepository.class);
        ViewModelProviders.of(this).get(SearchRepository.class);
        ViewModelProviders.of(this).get(VerseRepository.class);
    }

    public void showLoadingSuccessScreen() {
        Log.d(TAG, "showLoadingSuccessScreen");
        findViewById(R.id.act_main_container_fabs).setVisibility(View.VISIBLE);
    }

    public void showLoadingFailureScreen() {
        Log.d(TAG, "showLoadingFailureScreen");
        TextView textView = findViewById(R.id.act_main_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(
                getString(R.string.act_main_splash_verse_err), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(getString(R.string.act_main_splash_verse_err)));
        }

        textView = findViewById(R.id.act_main_msg);
        textView.setVisibility(View.VISIBLE);
        textView.setText(R.string.act_main_splash_msg_err);
    }

    @Override
    public void displayVerseForToday(@NonNull final Verse verse) {
        if (verse == null) {
            Log.e(TAG, "displayVerseForToday: no verse returned, keeping default");
            return;
        }

        Log.d(TAG, "displayVerseForToday : [" + verse.getReference() + "]");

        final String displayText =
            String.format(getString(R.string.daily_verse_template),
                          verse.getText(),
                          Utilities.getInstance().getBookName(verse.getBook()),
                          verse.getChapter(),
                          verse.getVerse());

        TextView textView = findViewById(R.id.act_main_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(displayText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(displayText));
        }
    }

    private class DatabaseLoaderCallback
        implements LoaderManager.LoaderCallbacks<Boolean> {

        @Override
        public Loader<Boolean> onCreateLoader(final int id, final Bundle args) {
            Log.d(TAG, "DatabaseLoaderCallback : onCreateLoader");
            return new SplashScreenPresenter.DbSetupAsyncTask();
        }

        @Override
        public void onLoadFinished(final Loader<Boolean> loader, final Boolean isDatabaseLoaded) {
            Log.d(TAG, "DatabaseLoaderCallback : onLoadFinished");
            stopLoadingScreen();
            if (isDatabaseLoaded) {
                initRepositories();
                showLoadingSuccessScreen();
            } else {
                showLoadingFailureScreen();
            }
/*
            if (isDatabaseLoaded) {
                final String defaultReference = getDefaultDailyVerseReference();
                final String[] referenceArray = getDefaultDailyVerseReferenceArray();
                new SplashScreenPresenter.GetVerseForTodayTask(
                    defaultReference, referenceArray).execute();
            }
*/
        }

        @Override
        public void onLoaderReset(final Loader<Boolean> loader) {
            Log.d(TAG, "DatabaseLoaderCallback : onLoaderReset");
            showLoadingFailureScreen();
        }

    }
}
