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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class ActivityChapterList
        extends AppCompatActivity {

    private static final String TAG = "SB_ActivityChapterList";
    private boolean              mTwoPane;
    private RecyclerView.Adapter chapterAdapter;
    private ListBooks.Entry      mBook;
    private ListChapter.Entry    mChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_list_toolbar);
        setSupportActionBar(toolbar);
        if (toolbar == null) {
            Utilities.throwError(TAG + "onCreate() toolbar == null");
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        mBook = extras.getParcelable(Utilities.CURRENT_BOOK);
        if (mBook == null) {
            Utilities.throwError(TAG + " onCreate : mBook == null");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chapter_list);
        if (recyclerView == null) {
            Utilities.throwError(TAG + " onCreate : recyclerView == null");
        }

        String chapterText = getString(R.string.chapter_list_prepend_text).trim();
        ListChapter.populateList(Integer.parseInt(mBook.getChapterCount()), chapterText);
        mChapter = ListChapter.getItem(extras.getString(Utilities.CURRENT_CHAPTER_NUMBER));
        if (mChapter == null) {
            Utilities.throwError(TAG + " onCreate : mChapter == null");
        }

        if (ListChapter.getCount() == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    getApplicationContext(), LinearLayoutManager.HORIZONTAL, true
            ));
        }
        chapterAdapter = new AdapterChapterList(this, ListChapter.getList());
        recyclerView.setAdapter(chapterAdapter);

        if (findViewById(R.id.chapter_container) != null) {
            mTwoPane = true;
            extras.putString(Utilities.LOAD_CHAPTER, Utilities.LOAD_CHAPTER_YES);
        }
        StringBuilder title = new StringBuilder(mBook.getName())
                .append(" : ").append(mBook.getChapterCount()).append(" ")
                .append(getString(R.string.book_details_append_chapters));
        setTitle(title);
        toolbar.setTitle(getTitle());

        if (extras.getString(Utilities.LOAD_CHAPTER).equalsIgnoreCase(Utilities.LOAD_CHAPTER_YES)) {
            Log.i(TAG, "onCreate: LOAD_CHAPTER = YES");
            chapterClicked(mChapter);
        }
    }

    void chapterClicked(ListChapter.Entry chapterEntry) {
        Bundle args = new Bundle();
        args.putParcelable(Utilities.CURRENT_BOOK, mBook);
        args.putParcelable(Utilities.CURRENT_CHAPTER, chapterEntry);
        args.putString(Utilities.CURRENT_CHAPTER_NUMBER, chapterEntry.getChapterNumber());
        args.putString(Utilities.LOAD_CHAPTER,
                       getIntent().getExtras().getString(Utilities.LOAD_CHAPTER));

        if (isDualPane()) {
            Log.d(TAG, "chapterClicked: isDualPane = true");
            FragmentChapterVerses fragment = new FragmentChapterVerses();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.chapter_container, fragment)
                                       .commit();
        } else {
            Log.d(TAG, "chapterClicked: isDualPane = false");
            Intent intent = new Intent(getApplicationContext(), ActivityChapterVerses.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }

    public boolean isDualPane() {
        return mTwoPane;
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
}
