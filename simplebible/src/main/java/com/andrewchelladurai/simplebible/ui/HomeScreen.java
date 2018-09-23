/*
 *
 * This file 'HomeScreen.java' is part of SimpleBible :
 *
 * Copyright (c) 2018.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.data.Verse;
import com.andrewchelladurai.simplebible.ops.HomeScreenOps;
import com.andrewchelladurai.simplebible.ops.MainScreenOps;
import com.andrewchelladurai.simplebible.presenter.HomeScreenPresenter;
import com.andrewchelladurai.simplebible.repository.BookRepository;
import com.andrewchelladurai.simplebible.repository.VerseRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class HomeScreen
    extends Fragment
    implements HomeScreenOps {

    private static final String TAG = "HomeScreen";

    private static HomeScreenPresenter mPresenter;
    private MainScreenOps mMainScreenOps;

    private TextView mVerseView;
    private TextView mMessage;
    private ProgressBar mProgressBar;

    @SuppressWarnings("WeakerAccess")
    public HomeScreen() {
        mPresenter = new HomeScreenPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.home_screen, container, false);
        mVerseView = rootView.findViewById(R.id.daily_verse);
        mMessage = rootView.findViewById(R.id.message);
        mProgressBar = rootView.findViewById(R.id.progress_bar);

        FloatingActionButton fab = rootView.findViewById(R.id.share);
        fab.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                shareDailyVerse();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        // the parent activity must implement this interface
        // allows us to hide and show navigation controls
        mMainScreenOps = (MainScreenOps) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // no longer needed
        mMainScreenOps = null;
    }

    private void loadDatabase() {
        Log.d(TAG, "loadDatabase:");
        LoaderManager.getInstance(this)
                     .initLoader(R.integer.DB_LOADER, null, new DbInitLoaderCallback())
                     .forceLoad();
    }

    private void shareDailyVerse() {
        Log.d(TAG, "shareDailyVerse:");
    }

    public void startLoadingScreen() {
        Log.d(TAG, "startLoadingScreen: ");
        mMainScreenOps.hideNavigationControls();
        mVerseView.setText(HtmlCompat.fromHtml(
            getString(R.string.home_screen_daily_verse_loading),
            HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void stopLoadingScreen() {
        mVerseView.setText(HtmlCompat.fromHtml(
            getString(R.string.home_screen_daily_verse_default),
            HtmlCompat.FROM_HTML_MODE_LEGACY));

        mMessage.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showFailedLoadingMessage() {
        Log.d(TAG, "showFailedLoadingMessage:");
        mVerseView.setText(HtmlCompat.fromHtml(
            getString(R.string.home_screen_daily_verse_failure),
            HtmlCompat.FROM_HTML_MODE_LEGACY));

        mMessage.setText(R.string.home_screen_message_loading_failure);

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showDefaultDailyVerse() {
        Log.d(TAG, "showDefaultDailyVerse:");
        stopLoadingScreen();
    }

    private void showDailyVerse(@NonNull final Verse verse) {
        Log.d(TAG, "showDailyVerse:" + verse.getReference());

        BookRepository repository = ViewModelProviders.of(this).get(BookRepository.class);
        repository.getBookName(verse.getBook()).observe(this, new Observer<List<Book>>() {

            @Override
            public void onChanged(final List<Book> books) {
                if (books == null || books.isEmpty()) {
                    Log.e(TAG, "empty book returned for passed verse reference");
                    return;
                }
                updateDailyVerse(books.get(0), verse);
            }
        });
    }

    public Context getSystemContext() {
        return getContext();
    }

    private void updateDailyVerse(@NonNull final Book book, @NonNull final Verse verse) {
        Log.d(TAG, "updateDailyVerse:");
        final String bookName = book.getBookName().toUpperCase();
        final int chapterNumber = verse.getChapter();
        final int verseNumber = verse.getVerse();
        final String verseText = verse.getText();

        final String displayText = String.format(
            getString(R.string.daily_verse_template),
            verseText, bookName, chapterNumber, verseNumber);

        mVerseView.setText(HtmlCompat.fromHtml(displayText, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    private void handleDbInitLoaderResult(final Boolean databaseLoaded) {
        Log.d(TAG, "handleDbInitLoaderResult: " + databaseLoaded);
        stopLoadingScreen();
        if (databaseLoaded) {
            loadDailyVerse();
            mMainScreenOps.showNavigationControls();
        } else {
            showFailedLoadingMessage();
        }
    }

    private void loadDailyVerse() {
        Log.d(TAG, "loadDailyVerse:");

        final String[] array = getResources().getStringArray(R.array.daily_verse_list);
        final String defaultReference = getString(R.string.daily_verse_default_reference);

        VerseRepository repository = ViewModelProviders.of(this).get(VerseRepository.class);
        repository.getVerseForReference(mPresenter.getDailyVerseReference(array, defaultReference))
                  .observe(this, new Observer<List<Verse>>() {

                      @Override
                      public void onChanged(final List<Verse> list) {
                          if (list == null || list.isEmpty()) {
                              Log.e(TAG, "empty daily verse reference passed");
                              showDefaultDailyVerse();
                              return;
                          }
                          showDailyVerse(list.get(0));
                      }
                  });
    }

    private class DbInitLoaderCallback
        implements LoaderManager.LoaderCallbacks<Boolean> {

        private static final String TAG = "DbInitLoaderCallback";

        @Override
        public Loader<Boolean> onCreateLoader(final int id, final Bundle args) {
            Log.d(TAG, "onCreateLoader:");
            return new HomeScreenPresenter.DbInitLoader();
        }

        @Override
        public void onLoadFinished(final Loader<Boolean> loader, final Boolean databaseLoaded) {
            handleDbInitLoaderResult(databaseLoaded);
        }

        @Override
        public void onLoaderReset(final Loader<Boolean> loader) {
            Log.d(TAG, "onLoaderReset:");
            showFailedLoadingMessage();
        }
    }
}
