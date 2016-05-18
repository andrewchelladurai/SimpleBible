/*
 *
 * This file 'SimpleBibleActivity.java' is part of SimpleBible :  SimpleBible
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleBibleActivity
        extends AppCompatActivity {

    private static final String TAG = "SimpleBibleActivity";
    //    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.activity_simple_bible_container);
        if (null != mViewPager) {
            mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_simple_bible_tabs);
        if (null != tabLayout) {
            tabLayout.setupWithViewPager(mViewPager);
        }

/*
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.activity_simple_bible_fab);
        if (null != fab) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
*/

        Utilities.getInstance(getResources());
        DatabaseUtility.getInstance(getApplicationContext());
        mViewPager.addOnPageChangeListener(new KeyboardHideListener(this));
        Book.populateBooks(getResources().getStringArray(R.array.books_n_chapter_count_array));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                settingsButtonClicked();
                return true;
            case R.id.action_about:
                aboutButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void settingsButtonClicked() {
        Log.i(TAG, "settingsButtonClicked: ");
        // FIXME: Handle Settings Click
    }

    private void aboutButtonClicked() {
        Log.i(TAG, "aboutButtonClicked: ");
        // FIXME: Handle About Click
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
            View rootView = inflater.inflate(R.layout.fragment_simple_bible, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(
                    getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    private class PagerAdapter
            extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return BookListFragment.newInstance();
                case 2:
                    return SearchFragment.newInstance();
                case 3:
                    return PlaceholderFragment.newInstance(position + 1);
                default: // FIXME: handle default case being hit
                    return HomeFragment.newInstance();
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
                    return getString(R.string.tab_home);
                case 1:
                    return getString(R.string.tab_books);
                case 2:
                    return getString(R.string.tab_search);
                case 3:
                    return getString(R.string.tab_notes);
                default: // FIXME: handle default case being hit
                    return "BLANK";
            }
        }
    }
}
