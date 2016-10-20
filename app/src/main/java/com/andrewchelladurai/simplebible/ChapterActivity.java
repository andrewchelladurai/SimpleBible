/*
 *
 * This file 'ChapterDetailActivity.java' is part of SimpleBible :
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
import android.support.v7.widget.Toolbar;

import com.andrewchelladurai.simplebible.interaction.ChapterActivityOperations;
import com.andrewchelladurai.simplebible.model.BooksList.BookItem;
import com.andrewchelladurai.simplebible.presentation.ChapterActivityPresenter;

public class ChapterActivity
        extends AppCompatActivity
        implements ChapterActivityOperations {

    //    private int                      mChapterNumber;
//    private BooksList.BookItem       mBookItem;
    private ChapterActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_chapter_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chapter_detail_toolbar);
        setSupportActionBar(toolbar);

        init();

        // savedState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle argsReceived = getIntent().getExtras();
            BookItem mBookItem = argsReceived.getParcelable(ChapterFragment.ARG_BOOK_ITEM);
            int mChapterNumber = argsReceived.getInt(ChapterFragment.ARG_CHAPTER_NUMBER);

            String bookName = (mBookItem == null) ? "Genesis" : mBookItem.getBookName();
            int count = (mBookItem == null) ? 1 : mBookItem.getChapterCount();
            String appendText = getResources().getQuantityString(
                    R.plurals.fragment_books_chapter_count_template, count, count);
            toolbar.setTitle(bookName + " : " + appendText);
            setTitle(bookName + " : " + appendText);

            Bundle argsToPassOn = new Bundle();
            argsToPassOn.putParcelable(ChapterFragment.ARG_BOOK_ITEM, mBookItem);
            argsToPassOn.putInt(ChapterFragment.ARG_CHAPTER_NUMBER, mChapterNumber);

            ChapterFragment fragment = ChapterFragment.newInstance();
            fragment.setArguments(argsToPassOn);
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.chapter_detail_container, fragment)
                                       .commit();
        }
    }

    @Override public void init() {
        if (mPresenter == null) {
            mPresenter = new ChapterActivityPresenter(this);
        }
    }

    @Override public void refresh() {

    }
}
