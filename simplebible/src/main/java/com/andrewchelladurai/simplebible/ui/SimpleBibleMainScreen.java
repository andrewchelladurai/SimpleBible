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
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.BookRepository;
import com.andrewchelladurai.simplebible.data.repository.BookmarkRepository;
import com.andrewchelladurai.simplebible.data.repository.BookmarkVerseRepository;
import com.andrewchelladurai.simplebible.data.repository.SearchRepository;
import com.andrewchelladurai.simplebible.data.repository.VerseRepository;
import com.andrewchelladurai.simplebible.presenter.SplashScreenPresenter;
import com.andrewchelladurai.simplebible.ui.ops.SplashScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

        showLoadingScreen();
        // FIXME: 9/9/18 Daily Verse does not show after DB is loaded and screen rotated
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatabase();
    }

    private void initDatabase() {
        if (!mPresenter.isDatabaseLoaded()) {
            Log.d(TAG, "initDatabase() called");
            getSupportLoaderManager().initLoader(R.integer.DB_LOADER, null, this).forceLoad();
        } else {
            stopLoadingScreen();
            showLoadingSuccessScreen();
        }
    }

    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }

    public void updateProgress(final Integer value) {
        TextView textView = findViewById(R.id.act_main_msg);
        textView.setText(String.valueOf(value));
    }

    public void showLoadingScreen() {
        Log.d(TAG, "showLoadingScreen() called");
        TextView textView = findViewById(R.id.act_main_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(
                getString(R.string.act_main_splash_verse), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(getString(R.string.act_main_splash_verse)));
        }
    }

    public void showLoadingSuccessScreen() {
        Log.d(TAG, "showLoadingSuccessScreen() called");
        initRepositories();
        findViewById(R.id.act_main_container_pbar).setVisibility(View.GONE);
        findViewById(R.id.act_main_container_fabs).setVisibility(View.VISIBLE);
        updateDailyVerse();
    }

    private void populateBooksCache() {
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
    }

    public void showLoadingFailureScreen() {
        Log.d(TAG, "showLoadingFailureScreen() called");
        TextView textView = findViewById(R.id.act_main_verse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(
                getString(R.string.act_main_splash_verse_err), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(getString(R.string.act_main_splash_verse_err)));
        }

        textView = findViewById(R.id.act_main_msg);
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

    public void stopLoadingScreen() {
        Log.d(TAG, "stopLoadingScreen() called");
        ProgressBar progressBar = findViewById(R.id.act_main_pbar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void initRepositories() {
        Log.d(TAG, "initRepositories() called");
        ViewModelProviders.of(this).get(BookRepository.class);
        populateBooksCache();
        ViewModelProviders.of(this).get(BookmarkRepository.class);
        ViewModelProviders.of(this).get(BookmarkVerseRepository.class);
        ViewModelProviders.of(this).get(SearchRepository.class);
        ViewModelProviders.of(this).get(VerseRepository.class);
    }

    @Override
    public void updateDailyVerse() {
        final String defaultReference = getString(R.string.default_daily_verse_reference);
        int dayOfTheYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        Log.d(TAG, "updateDailyVerse: dayOfTheYear [" + dayOfTheYear + "]");
        String[] verseArray = getResources().getStringArray(R.array.daily_verse_list);

        String reference = (dayOfTheYear > verseArray.length) ? defaultReference
                                                              : verseArray[dayOfTheYear];
        mPresenter.getVerseForToday(reference, defaultReference, VerseRepository.getInstance());
    }

    @Override
    public Loader<Boolean> onCreateLoader(final int id, final Bundle args) {
        Log.d(TAG, "onCreateLoader() called");
        return new SplashScreenPresenter.DbSetupAsyncTask();
    }

    @Override
    public void onLoadFinished(final Loader<Boolean> loader, final Boolean data) {
        Log.d(TAG, "onLoadFinished : Database Loaded = [" + data + "]");
        mPresenter.setDatabaseLoaded(data);
        stopLoadingScreen();
        if (data) {
            showLoadingSuccessScreen();
        } else {
            showLoadingFailureScreen();
        }
    }

    @Override
    public void onLoaderReset(final Loader<Boolean> loader) {
        Log.e(TAG, "onLoaderReset:");
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
}
