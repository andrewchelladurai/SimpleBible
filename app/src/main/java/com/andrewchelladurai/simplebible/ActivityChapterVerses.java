/*
 *
 * This file 'ItemDetailActivity.java' is part of SimpleBible :
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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ActivityChapterVerses
        extends AppCompatActivity {

    private static final String TAG = "SB_ActivityChapterVerse";
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // Start to get all the necessary arguments for operation
        Bundle          extras = getIntent().getExtras();
        ListBooks.Entry book   = extras.getParcelable(Utilities.CURRENT_BOOK);
        if (book == null) {
            Utilities.throwError(TAG, TAG + "onCreate: book == null");
        }
        ListChapter.Entry chapter = extras.getParcelable(Utilities.CURRENT_CHAPTER);
        if (chapter == null) {
            Utilities.throwError(TAG, TAG + "onCreate: chapter == null");
        }
        String chapterNumber = extras.getString(Utilities.CURRENT_CHAPTER_NUMBER);
        if (chapterNumber == null) {
            Utilities.throwError(TAG, TAG + "onCreate: chapterNumber == null");
        }
        String loadChapter = Utilities.LOAD_CHAPTER_NO;

        mBundle = new Bundle();
        mBundle.putParcelable(Utilities.CURRENT_BOOK, book);
        mBundle.putParcelable(Utilities.CURRENT_CHAPTER, chapter);
        mBundle.putString(Utilities.CURRENT_CHAPTER_NUMBER, chapterNumber);
        mBundle.putString(Utilities.LOAD_CHAPTER, loadChapter);
        Utilities.log(TAG, "onCreate: mBundle created " + book + " - " + chapter + " - " +
                         chapterNumber);
        // Got all the necessary arguments for operation

        setContentView(R.layout.activity_chapter_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_detail_toolbar);
        if (toolbar == null) {
            Utilities.throwError(TAG, TAG + " onCreate : toolbar == null");
        }
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentChapterVerses fragment = new FragmentChapterVerses();
        fragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction().add(
                R.id.activity_chapter_detail_chapter_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, ActivityChapterList.class);
            intent.putExtras(mBundle);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
