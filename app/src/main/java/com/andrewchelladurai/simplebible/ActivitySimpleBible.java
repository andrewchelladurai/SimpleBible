/*
 *
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
 *
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ActivitySimpleBible
        extends AppCompatActivity {

    private static final String TAG = "SB_ActivitySimpleBible";

    static {
        //        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private PagerAdapter  mPagerAdapter;
    private ViewPager     mPager;
    private FragmentNotes fragmentNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);
        DatabaseUtility.getInstance(getApplicationContext());
        Utilities.getInstance(getResources());

        Toolbar toolbar = (Toolbar) findViewById(R.id.act_sb_toolbar);
        setSupportActionBar(toolbar);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.act_sb_container);
        if (mPager == null) {
            Utilities.throwError(TAG + " mPager == null");
        }
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new KeyboardHideListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return R.id.action_settings == item.getItemId() || super.onOptionsItemSelected(item);
    }

    public void refreshNotesScreen() {
        Utilities.log(TAG, "refreshNotesScreen() called");
        if (fragmentNotes == null) {
            fragmentNotes = FragmentNotes.newInstance();
        }
        fragmentNotes.refreshData();
    }

    public class PagerAdapter
            extends FragmentPagerAdapter {

        private static final String TAG = "SB_PagerAdapter";

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentHome.newInstance();
                case 1:
                    return FragmentBooks.newInstance();
                case 2:
                    return FragmentSearch.newInstance();
                case 3:
                    if (fragmentNotes == null) {
                        fragmentNotes = FragmentNotes.newInstance();
                    }
                    return fragmentNotes;
                default:
                    throw new AssertionError("Pager Position in default case" + position);
            }
        }

        @Override
        public int getCount() {
            return 4; // Show 3 total pages.
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_home);
                case 1:
                    return getString(R.string.tab_books);
                case 2:
                    return getString(R.string.tab_search);
                case 3:
                    return getString(R.string.tab_notes);
            }
            return getString(R.string.app_name);
        }
    }
}
