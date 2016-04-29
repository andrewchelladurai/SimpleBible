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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class ActivityChapter
        extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "ActivityChapter";
    public static String ARG_BOOK_NUMBER = "BOOK_NUMBER";
    public static String ARG_CHAPTER_NUMBER = "CHAPTER_NUMBER";

    private Book.Details mBook = null;
    private int mCurrentChapter = 0;
    private AdapterVerseItem<String> mVersesAdapter = null;
    private ArrayList<String> mVersesArray = null;
    private ListViewCompat mVerseList = null;
    private AlertDialog mNewChapterDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_toolbar);
        setSupportActionBar(toolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        bindButton(R.id.activity_chapter_but_previous, true);
        bindButton(R.id.activity_chapter_but_notes, false);
        bindButton(R.id.activity_chapter_but_chapter, false);
        bindButton(R.id.activity_chapter_but_search, false);
        bindButton(R.id.activity_chapter_but_next, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.chapter_dialog);
        mNewChapterDialog = builder.create();

        mVersesArray = new ArrayList<>(0);
        mVersesAdapter = new AdapterVerseItem<>(getApplicationContext(),
                                                android.R.layout.simple_list_item_1, mVersesArray);
        mVerseList = (ListViewCompat) findViewById(R.id.activity_chapter_list);
        if (mVerseList != null) {
            mVerseList.setAdapter(mVersesAdapter);
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

    private void refreshChapter() {
        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        int book = Integer.parseInt(mBook.getNumber());
        mVersesArray.clear();
        mVersesArray.addAll(dbu.getAllVersesOfChapter(book, mCurrentChapter));
        mVerseList.setSelectionAfterHeaderView();
        mVersesAdapter.notifyDataSetChanged();
        setTitle(mBook.getName() + " : " +
                 getString(R.string.label_chapter) + " " + mCurrentChapter);
    }

    @Override
    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.activity_chapter_but_previous:
                handlePreviousButtonClick();
                break;
            case R.id.activity_chapter_but_notes:
                handleNotesButtonClick();
                break;
            case R.id.activity_chapter_but_chapter:
                handleChapterButtonClick();
                break;
            case R.id.activity_chapter_but_search:
                handleSearchButtonClick();
                break;
            case R.id.activity_chapter_but_next:
                handleNextButtonClick();
                break;
            default: // FIXME: 29/4/16 This must be showcased
        }
    }

    private void handlePreviousButtonClick() {
        mCurrentChapter--;
        if (isChapterValid()) {
            refreshChapter();
        } else {
            mCurrentChapter++;
            Snackbar.make(mVerseList, "You are at First Chapter", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void handleNotesButtonClick() {
        finish();
        ActivitySimpleBible.showNotesSection();
    }

    private void handleChapterButtonClick() {
        mNewChapterDialog.show();
        GridView gridView = (GridView) mNewChapterDialog.findViewById(R.id.chapter_dialog_grid);
        if (null != gridView) {
            int pCount = Integer.parseInt(mBook.getChapterCount());
            String[] values = new String[pCount];
            for (int i = 0; i < pCount; i++) {
                values[i] = "" + (i + 1);
            }
            gridView.setAdapter(
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent, View view, int position, long id) {
                    mNewChapterDialog.dismiss();
                    handleNewChapterSelected(position + 1);
                }
            });
        }
    }

    private void handleSearchButtonClick() {
        finish();
        ActivitySimpleBible.showSearchSection();
    }

    private void handleNextButtonClick() {
        mCurrentChapter++;
        if (isChapterValid()) {
            refreshChapter();
        } else {
            Snackbar.make(mVerseList, "You are at Last Chapter", Snackbar.LENGTH_SHORT).show();
            mCurrentChapter--;
        }
    }

    private void handleNewChapterSelected(int pNewChapter) {
        if (pNewChapter == mCurrentChapter) {
            return;
        }
        int oldChapterNumber = mCurrentChapter;
        mCurrentChapter = pNewChapter;
        if (isChapterValid()) {
            refreshChapter();
        } else {
            mCurrentChapter = oldChapterNumber;
        }
    }
}
