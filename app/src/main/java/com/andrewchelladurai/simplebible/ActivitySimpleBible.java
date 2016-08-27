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

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.stephentuso.welcome.WelcomeScreenHelper;

public class ActivitySimpleBible
        extends AppCompatActivity {

    private static final String TAG = "SB_ActivitySimpleBible";
    private PagerAdapter  mPagerAdapter;
    private ViewPager     mPager;
    private FragmentNotes fragmentNotes;
    private WelcomeScreenHelper welcomeScreen;

    @Override
    protected void onResume() {
        super.onResume();
        if (null != fragmentNotes) {
            getSupportFragmentManager().beginTransaction()
                                       .detach(fragmentNotes)
                                       .attach(fragmentNotes)
                                       .commit();
            mPagerAdapter.notifyDataSetChanged();
            mPager.refreshDrawableState();
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start Preference work
        PreferenceManager.setDefaultValues(this, R.xml.preferences_list, false);
        Utilities.getInstance(this);
        if (Utilities.isDarkModeEnabled()) {
            Utilities.log(TAG, "onCreate: Show Dark Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            Utilities.log(TAG, "onCreate: Show Light Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // End Preference work

        welcomeScreen = new WelcomeScreenHelper(this, SplashActivity.class);
        welcomeScreen.show(savedInstanceState);

        // Start loading application
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_bible);
        DatabaseUtility.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.act_sb_toolbar);
        setSupportActionBar(toolbar);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.act_sb_container);
        if (mPager == null) {
            Utilities.throwError(TAG + " mPager == null");
        }
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new PageChangeListener(this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.act_sb_tabs);
        if (tabLayout == null) {
            Utilities.throwError(TAG + " tabLayout == null");
        }
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, ActivitySettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private class PageChangeListener
            implements ViewPager.OnPageChangeListener {

        private ActivitySimpleBible mActivity;

        public PageChangeListener(ActivitySimpleBible pActivity) {
            mActivity = pActivity;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Utilities.hideKeyboard(mActivity);
            StringBuilder pageTitle = new StringBuilder(getString(R.string.app_name));
            if (position == 3 && null != fragmentNotes) {
                getSupportFragmentManager().beginTransaction()
                                           .detach(fragmentNotes)
                                           .attach(fragmentNotes)
                                           .commit();
                mPager.refreshDrawableState();
            }
            setTitle(pageTitle); // This is required to refresh the page on Scroll
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
