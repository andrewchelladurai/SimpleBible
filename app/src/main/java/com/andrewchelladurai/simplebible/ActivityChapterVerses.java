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

    private static final String TAG = "SB_ActivityChapterVerses";

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_chapter_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_detail_toolbar);
        if (toolbar == null) {
            Utilities.showError(TAG + " onCreate : toolbar == null");
        }
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedState == null) {
            FragmentChapterVerses fragment = new FragmentChapterVerses();
            Bundle extras = getIntent().getExtras();
            Bundle args = new Bundle();
            args.putParcelable(Utilities.CURRENT_CHAPTER,
                               extras.getParcelable(Utilities.CURRENT_CHAPTER));
            args.putParcelable(Utilities.CURRENT_BOOK,
                               extras.getParcelable(Utilities.CURRENT_BOOK));
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(
                    R.id.activity_chapter_detail_chapter_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Bundle extras = getIntent().getExtras();
            BooksList.Entry book = extras.getParcelable(Utilities.CURRENT_BOOK);
            ChapterList.Entry chapter = extras.getParcelable(Utilities.CURRENT_CHAPTER);

            Bundle args = new Bundle();
            args.putParcelable(Utilities.CURRENT_CHAPTER, chapter);
            args.putParcelable(Utilities.CURRENT_BOOK, book);
            args.putString(Utilities.CURRENT_CHAPTER_NUMBER, chapter.getChapterNumber());

            Intent intent = new Intent(this, ActivityChapterList.class);
            intent.putExtras(args);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
