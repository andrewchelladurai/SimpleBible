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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ActivityChapter
        extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "ActivityChapter";
    public static final String ARG_BOOK_NUMBER = "ARG_BOOK_NUMBER";
    public static final String ARG_CHAPTER_NUMBER = "ARG_CHAPTER_NUMBER";
    private Book.Details mBookDetails = null;
    private int mCurrentChapter = 0;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_chapter);

        int bNum;
        try {
            bNum = Integer.parseInt(getIntent().getStringExtra(ARG_BOOK_NUMBER));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            bNum = 0;
        }
        if (bNum < 1 || bNum > 66) {
            Log.d(TAG, "onCreate: Invalid Book Number : " + bNum);
            return;
        }

        try {
            mCurrentChapter = Integer.parseInt(getIntent().getStringExtra(ARG_CHAPTER_NUMBER));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            mCurrentChapter = 0;
        }
        if (mCurrentChapter < 1) {
            Log.d(TAG, "onCreate: Invalid Chapter Number : " + mCurrentChapter);
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mBookDetails = Book.getDetails(bNum);

        String title = mBookDetails.getName() + " " +
                       getString(R.string.title_activity_activity_chapter) + " " + mCurrentChapter;
        setTitle(title);

        Log.d(TAG, "onCreate: Showing chapter " + mCurrentChapter + " of " +
                   mBookDetails.getNumber() + "-" + mBookDetails.getName() + ":" +
                   mBookDetails.getChapterCount());

        bindButton(R.id.activity_chapter_but_previous, true);
        bindButton(R.id.activity_chapter_but_notes, false);
        bindButton(R.id.activity_chapter_but_search, false);
        bindButton(R.id.activity_chapter_but_next, true);

    }

    private void bindButton(final int pButtonId, boolean isImageButton) {
        if (isImageButton) {
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

    public void handlePreviousButtonClick(View view) {
        // FIXME: 23/4/16
        Log.d(TAG, "handlePreviousButtonClick: ");
    }

    public void handleNotesButtonClick(View view) {
        finish();
        ActivitySimpleBible.showNotesSection();
    }

    public void handleSearchButtonClick(View view) {
        finish();
        ActivitySimpleBible.showSearchSection();
    }

    public void handleNextButtonClick(View view) {
        // FIXME: 23/4/16
        Log.d(TAG, "handleNextButtonClick: ");
    }

    @Override
    public void onClick(final View pView) {
        switch (pView.getId()) {
            case R.id.activity_chapter_but_previous: handlePreviousButtonClick(pView); break;
            case R.id.activity_chapter_but_notes: handleNotesButtonClick(pView); break;
            case R.id.activity_chapter_but_search: handleSearchButtonClick(pView); break;
            case R.id.activity_chapter_but_next: handleNextButtonClick(pView); break;
            default:
                Log.d(TAG, "onClick: in default case");
        }
    }
}
