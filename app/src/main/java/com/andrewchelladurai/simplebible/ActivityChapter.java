/*
 * This file 'ActivityChapter.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
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
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ActivityChapter
        extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "ActivityChapter";
    public static String ARG_BOOK_NUMBER = "BOOK_NUMBER";
    public static String ARG_CHAPTER_NUMBER = "CHAPTER_NUMBER";

    private Book.Details mBook;
    private int mCurrentChapter = 0;
    private AdapterVerse<String> mVersesAdapter;
    private ArrayList<String> mVersesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_toolbar);
        setSupportActionBar(toolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bindButton(R.id.activity_chapter_but_previous, true);
        bindButton(R.id.activity_chapter_but_notes, false);
        bindButton(R.id.activity_chapter_but_chapter, false);
        bindButton(R.id.activity_chapter_but_search, false);
        bindButton(R.id.activity_chapter_but_next, true);

        try {
            mBook = Book.getBookDetails(
                    Integer.parseInt(getIntent().getStringExtra(ARG_BOOK_NUMBER)));
        } catch (NumberFormatException pE) {
            mBook = null;
            pE.printStackTrace();
        }
        try {
            mCurrentChapter = Integer.parseInt(getIntent().getStringExtra(ARG_CHAPTER_NUMBER));
        } catch (NumberFormatException pE) {
            mCurrentChapter = 0;
            pE.printStackTrace();
        }
        if (null != mBook) {
            mCurrentChapter = (isChapterValid()) ? mCurrentChapter : 1;
        }
        Log.i(TAG, "onCreate: Showing [book][chapter] : [" + mBook.getName() +
                   "][" + mCurrentChapter + "]");
        String value = getString(R.string.label_chapter) + " " + mCurrentChapter;
        setTitle(value);

        mVersesList = new ArrayList<>(0);
        mVersesAdapter = new AdapterVerse<>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1, mVersesList);
        final ListViewCompat list = (ListViewCompat) findViewById(R.id.activity_chapter_list);
        if (list != null) {
            list.setAdapter(mVersesAdapter);
        }
        refreshChapter();
    }

    private boolean isChapterValid() {
        return mCurrentChapter > 0 && mCurrentChapter <= Integer.parseInt(mBook.getChapterCount());
    }

    private void bindButton(int pButtonId, boolean pIsImageButton) {
        if (pIsImageButton) {
            AppCompatImageButton button = (AppCompatImageButton) findViewById(pButtonId);
            if (null != button) {
                button.setOnClickListener(this);
            }
        } else {
            AppCompatButton button = (AppCompatButton) findViewById(pButtonId);
            if (null != button) {
                button.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.activity_chapter_but_previous: handlePreviousButtonClick(); break;
            case R.id.activity_chapter_but_notes: handleNotesButtonClick(); break;
            case R.id.activity_chapter_but_chapter: handleChapterButtonClick(); break;
            case R.id.activity_chapter_but_search: handleSearchButtonClick(); break;
            case R.id.activity_chapter_but_next: handleNextButtonClick(); break;
            default: // FIXME: 29/4/16 This must be showcased
        }
    }

    private void handleNextButtonClick() {

    }

    private void handleSearchButtonClick() {
        finish();
        ActivitySimpleBible.showSearchSection();
    }

    private void handleChapterButtonClick() {

    }

    private void handleNotesButtonClick() {
        finish();
        ActivitySimpleBible.showNotesSection();
    }

    private void handlePreviousButtonClick() {

    }

    private void refreshChapter() {
        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        int book = Integer.parseInt(mBook.getNumber());
        ArrayList<String> values = dbu.getAllVersesOfChapter(book, mCurrentChapter);
        mVersesList.clear();
        mVersesList.addAll(values);
        mVersesAdapter.notifyDataSetChanged();
    }
}
