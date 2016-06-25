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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ActivitySimpleBible
        extends AppCompatActivity {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private static final String TAG = "SB_ActivitySimpleBible";

    private PagerAdapter mPagerAdapter;
    private ViewPager    mPager;

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
        if (mPager == null) throw new AssertionError(TAG + " mPager == null");
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new KeyboardHideListener(this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.act_sb_tabs);
        if (tabLayout == null) throw new AssertionError(TAG + " tabLayout == null");
        tabLayout.setupWithViewPager(mPager);
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

    public static class PlaceholderFragment
            extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_simple_bible, container,
                                             false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(
                    getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class PagerAdapter
            extends FragmentPagerAdapter {

        private static final String TAG = "SB_SectionsPagerAdapter";

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentHome.newInstance();
                case 1:
                    return PlaceholderFragment.newInstance(position + 1);
                case 2:
                    return PlaceholderFragment.newInstance(position + 1);
                default:
                    throw new AssertionError("Pager Position in default case" + position);
            }
        }

        @Override
        public int getCount() {
            return 3; // Show 3 total pages.
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
            }
            return null;
        }
    }
}
