/*
 * This file 'ActivitySimpleBible.java' is part of SimpleBible :
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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ActivitySimpleBible
        extends AppCompatActivity {

    private SectionsAdapter mSectionsAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);

        DatabaseUtility.getInstance(getApplicationContext());
        Book.populateDetails(
                getResources().getStringArray(R.array.books_n_chapter_count_array));

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);
        mSectionsAdapter = new SectionsAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.activity_simple_bible_container);
        if (null != mPager) {
            mPager.setAdapter(mSectionsAdapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_simple_bible_tabs);
        if (null != tabLayout) {
            tabLayout.setupWithViewPager(mPager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.activity_simple_bible_action_settings ||
               super.onOptionsItemSelected(item);

    }

    private class SectionsAdapter
            extends FragmentPagerAdapter {

        public SectionsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return FragmentBooksList.newInstance();
                case 2:
                    return FragmentVerseNotes.newInstance();
                case 3:
                    return FragmentSearch.newInstance();
                default:
                    return FragmentHome.newInstance("");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.label_home);
                case 1:
                    return getString(R.string.label_books);
                case 2:
                    return getString(R.string.label_notes);
                case 3:
                    return getString(R.string.label_search);
            }
            return null;
        }
    }
}
