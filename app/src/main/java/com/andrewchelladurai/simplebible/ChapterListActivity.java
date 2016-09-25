/*
 *
 * This file 'ChapterListActivity.java' is part of SimpleBible :
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.andrewchelladurai.simplebible.adapter.ChapterListAdapter;
import com.andrewchelladurai.simplebible.interaction.BasicOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.ChapterList;

public class ChapterListActivity
        extends AppCompatActivity
        implements BasicOperations {

    private static final String TAG = "SB_ChapterListActivity";
    /**
     * Flag used to indicate if the Dual Pane mode needs to be shown. This is set looking at the
     * layout file loaded at runtime.
     */
    private boolean            showDualPane;
    private int                mChapterNumber;
    private BooksList.BookItem mBookItem;
    private boolean            isAllSet;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        init();
        setContentView(R.layout.activity_chapter_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_list_toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: isAllSet = " + isAllSet);
        if (isAllSet) {
            String bookName = mBookItem.getBookName();
            int count = mBookItem.getChapterCount();
            String appendText = getResources().getQuantityString(
                    R.plurals.fragment_books_chapter_count_template, count, count);
            toolbar.setTitle(bookName + " : " + appendText);
            setTitle(bookName + " : " + appendText);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chapter_list);
        assert recyclerView != null;
        recyclerView.setAdapter(new ChapterListAdapter(this, ChapterList.getAllItems()));

        if (findViewById(R.id.chapter_detail_container) != null) {
            showDualPane = true;
        }
    }

    public boolean showDualPanel() {
        return showDualPane;
    }

    @Override public void init() {
        isAllSet = false;
        Bundle bundle = getIntent().getExtras();
        mBookItem = bundle.getParcelable(ChapterFragment.ARG_BOOK_ITEM);
        mChapterNumber = bundle.getInt(ChapterFragment.ARG_CHAPTER_NUMBER);
        isAllSet = (mBookItem != null);
    }

    @Override public void refresh() {
    }

    public BooksList.BookItem getBookItem() {
        return mBookItem;
    }
}
