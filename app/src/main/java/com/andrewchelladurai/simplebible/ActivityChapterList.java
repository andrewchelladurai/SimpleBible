/*
 *
 * This file 'ItemListActivity.java' is part of SimpleBible :
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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ActivityChapterList
        extends AppCompatActivity {

    private static final String TAG = "SB_ActivityChapterList";
    private boolean           mTwoPane;
    private BooksList.Entry   mBook;
    private ChapterList.Entry mChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_list_toolbar);
        setSupportActionBar(toolbar);
        if (toolbar == null) {
            Utilities.showError(TAG + "onCreate() toolbar == null");
        }
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle args = getIntent().getExtras();
        mBook = args.getParcelable(Utilities.CURRENT_BOOK);
        if (mBook == null) {
            Utilities.showError(TAG + " onCreate : mBook == null");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chapter_list);
        if (recyclerView == null) {
            Utilities.showError(TAG + " onCreate : recyclerView == null");
        }

        String chapterText = getString(R.string.chapter_list_prepend_text).trim();
        ChapterList.populateList(Integer.parseInt(mBook.getChapterCount()), chapterText);
        mChapter = ChapterList.getItem(args.getString(Utilities.CURRENT_CHAPTER_NUMBER));
        if (mChapter == null) {
            Utilities.showError(TAG + " onCreate : mChapter == null");
        }

        if (ChapterList.getCount() == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    getApplicationContext(), LinearLayoutManager.HORIZONTAL, true
            ));
        }
        recyclerView.setAdapter(new AdapterChapterList(this, ChapterList.getList()));

        if (findViewById(R.id.chapter_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isDualPane() {
        return mTwoPane;
    }

    public BooksList.Entry getBook() {
        return mBook;
    }
}
