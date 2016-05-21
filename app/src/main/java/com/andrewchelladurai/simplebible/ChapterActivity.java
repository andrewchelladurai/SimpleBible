/*
 *
 * This file 'ChapterActivity.java' is part of SimpleBible :
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
 *
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class ChapterActivity
        extends AppCompatActivity {

    private static final String TAG = "ChapterActivity";
    public static final String COLUMN_COUNT = "COLUMN_COUNT";
    public static final String BOOK_NUMBER = "BOOK_NUMBER";
    public static final String CHAPTER_NUMBER = "CHAPTER_NUMBER";

    private int mColumnCount;
    private int mChapterNumber, mBookNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mColumnCount = getIntent().getIntExtra(COLUMN_COUNT, 1);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_chapter_verse_list);
        if (recyclerView != null) {
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(
                        getApplicationContext(), mColumnCount));
            }
            recyclerView.setAdapter(new VerseViewAdapter(
                    ChapterContent.ITEMS, new VerseEventHandler(this,
                                                                VerseEventHandler.HANDLE.CHAPTER)));
        }
        mBookNumber = getIntent().getIntExtra(BOOK_NUMBER, 1);
        mChapterNumber = getIntent().getIntExtra(CHAPTER_NUMBER, 1);
        Log.i(TAG, "onCreate: " + mBookNumber + ":" + mChapterNumber);
        Book.Details book = Book.getBookDetails(mBookNumber);
        Log.i(TAG, "onCreate: " + book.toString());
        if (book != null) {
            String title = book.name + " : " + getString(R.string.title_activity_chapter) + " "
                           + mChapterNumber;
            setTitle(title);
        }
    }

    public void handleLongClick(final ChapterContent.VerseEntry pItem) {
        Log.i(TAG, "handleLongClick: " + pItem.toString());
    }
}
