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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.andrewchelladurai.simplebible.adapter.ChapterListAdapter;
import com.andrewchelladurai.simplebible.interaction.ChapterListActivityOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.ChapterList;
import com.andrewchelladurai.simplebible.presentation.ChapterListActivityPresenter;

public class ChapterListActivity
        extends AppCompatActivity
        implements ChapterListActivityOperations {

    private static final String TAG = "SB_ChapterListActivity";
    /**
     * Flag used to indicate if the Dual Pane mode needs to be shown. This is set looking at the
     * layout file loaded at runtime.
     */
    private boolean                      showDualPane;
    private int                          mChapterNumber;
    private BooksList.BookItem           mBookItem;
    private boolean                      isAllSet;
    private ChapterListActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        init();
        setContentView(R.layout.activity_chapter_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_list_toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: isAllSet = " + isAllSet);

        ChapterListAdapter listAdapter = null;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chapter_list);
        if (recyclerView != null) {
            listAdapter = new ChapterListAdapter(this, ChapterList.getAllItems());
            recyclerView.setAdapter(listAdapter);
        }
        if (findViewById(R.id.chapter_detail_container) != null) {
            showDualPane = true;
        }

        if (listAdapter != null & showDualPane & mChapterNumber == 0) {
            mChapterNumber = 1;
            chapterItemClicked(ChapterList.getChapterItem(mChapterNumber));
        } else if (listAdapter != null & mChapterNumber != 0) {
            chapterItemClicked(ChapterList.getChapterItem(mChapterNumber));
        }

        if (isAllSet) {
            String bookName = mBookItem.getBookName();
            int count = mBookItem.getChapterCount();
            String appendText = getResources().getQuantityString(
                    R.plurals.fragment_books_chapter_count_template, count, count);
            toolbar.setTitle(bookName + " : " + appendText);
            setTitle(bookName + " : " + appendText);
        }
    }

    public boolean showDualPanel() {
        return showDualPane;
    }

    @Override public void init() {
        isAllSet = false;
        mPresenter = new ChapterListActivityPresenter(this);
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

    @Override public void chapterItemClicked(ChapterList.ChapterItem chapterItem) {
        Bundle args = new Bundle();
        args.putParcelable(ChapterFragment.ARG_BOOK_ITEM, getBookItem());
        args.putInt(ChapterFragment.ARG_CHAPTER_NUMBER, chapterItem.getChapterNumber());

        if (showDualPanel()) {
            ChapterFragment fragment = ChapterFragment.newInstance();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.chapter_detail_container, fragment)
                                       .commit();
        } else {
            Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }
}
