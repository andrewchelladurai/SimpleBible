/*
 * This file 'SimpleBibleActivity.java' is part of SimpleBible :
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

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;

public class SimpleBible
        extends AppCompatActivity
        implements ViewPager.OnPageChangeListener {

    private static SectionsPagerAdapter pagerAdapter;
    private static ViewPager pager;

    public static void showNotesSection() {
        pager.setCurrentItem(2);
        pagerAdapter.notifyDataSetChanged();
    }

    public static void showSearchSection() {
        pager.setCurrentItem(3);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseUtility.getInstance(getBaseContext());
        BookNameContent.populateBooks(getResources().getStringArray(
                R.array.books_n_chapter_count_array));

        setContentView(R.layout.activity_simple_bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.activity_simple_bible_container);
        if (pager != null) {
            pager.setAdapter(pagerAdapter);
            pager.addOnPageChangeListener(this);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_simple_bible_tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(pager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple_biblev2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pager.getWindowToken(), 0);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class SectionsPagerAdapter
            extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentHome.newInstance("");
                case 1:
                    return FragmentBooksList.newInstance(1);
                case 2:
                    return FragmentVerseNotes.newInstance(1);
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
                    return getString(R.string.home_label);
                case 1:
                    return getString(R.string.books_label);
                case 2:
                    return getString(R.string.notes_label);
                case 3:
                    return getString(R.string.search_label);
            }
            return null;
        }
    }
}
