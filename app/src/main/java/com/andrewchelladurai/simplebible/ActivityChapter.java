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
import android.view.View;

public class ActivityChapter
        extends AppCompatActivity
        implements View.OnClickListener {

    public static String ARG_BOOK_NUMBER = "BOOK_NUMBER";
    public static String ARG_CHAPTER_NUMBER = "CHAPTER_NUMBER";

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
    }

    private void bindButton(int pButtonId, boolean pIsImageButton) {
        if (pIsImageButton) {
            AppCompatButton button = (AppCompatButton) findViewById(pButtonId);
            if (null != button) {
                button.setOnClickListener(this);
            }
        } else {
            AppCompatImageButton button = (AppCompatImageButton) findViewById(pButtonId);
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

    }

    private void handleChapterButtonClick() {

    }

    private void handleNotesButtonClick() {

    }

    private void handlePreviousButtonClick() {

    }
}
