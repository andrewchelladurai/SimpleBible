/*
 *
 * This file is part of SimpleBible : A Holy Bible Application on the
 * Android Mobile platform with easy navigation and offline access.
 *
 * Copyright (c) 2015.
 * Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Activity_Welcome
        extends ActionBarActivity
        implements ActionBar.TabListener,
                   Fragment_Books.OnFragmentInteractionListener,
                   Fragment_Search.OnFragmentInteractionListener,
                   Fragment_About.OnFragmentInteractionListener {

    private static final String CLASS_NAME = "Activity_Welcome";
    private static SharedPreferences   sPreferences;
    private static DataBaseHelper      sDataBaseHelper;
    private        Adapter_TabSections mTabsAdapter;
    private        ViewPager           mPager;

    public static DataBaseHelper getDataBaseHelper() {
        return sDataBaseHelper;
    }

    protected static boolean getBooleanPreference(String preference_key) {
        Log.i(CLASS_NAME, "Entering getBooleanPreference" + preference_key);
        return sPreferences.getBoolean(preference_key, false);
    }

    public static int getVerseStyle(String verse_text_style, Context context) {
        return Integer.parseInt(sPreferences.getString(verse_text_style, "0"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(CLASS_NAME, "Entering onCreate");
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Activity_Settings.changeTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mTabsAdapter = new Adapter_TabSections(
                getSupportFragmentManager(),
                getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mTabsAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mTabsAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mTabsAdapter.getPageTitle(i))
                                      .setTabListener(this));
        }

        if (sDataBaseHelper == null) {
            sDataBaseHelper = new DataBaseHelper(this, "NIV.db");
            getDataBaseHelper().openDataBase();
        }
        Log.i(CLASS_NAME, "Exiting onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, Activity_Settings.class));
                return true;
            default:
                Log.e(CLASS_NAME, "Error : Option Item Selected hit Default : " + item.getTitle());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(final Tab tab, final FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(final Tab tab, final FragmentTransaction ft) {
    }

    @Override
    public void onFragmentBooksInteraction(final Book book) {
        Log.i(CLASS_NAME, "Entering onFragmentBooksInteraction");
        Intent intent = new Intent(this, Activity_VerseViewer.class);
        intent.putExtra("ID", book.getBookNumber());
        startActivity(intent);
        Log.i(CLASS_NAME, "Exiting onFragmentBooksInteraction");
    }

    @Override
    public void onFragmentAboutInteraction(final String id) {
        Log.i(CLASS_NAME, "Entering onFragmentAboutInteraction");
        Log.i(CLASS_NAME, "Exiting onFragmentAboutInteraction");
    }

    @Override
    public void onFragmentSearchInteraction(final String id) {
    }

    public void loadBookFragment(View view) {
        Log.i(CLASS_NAME, "Entering loadBookFragment");
        CharSequence bookName = ((EditText) findViewById(R.id.lookup_book)).getText();
        int bookId = BookList.getBookID(bookName);
        Log.d(CLASS_NAME, "BOOK NAME : " + bookId);
        if (bookName.length() > 0 && bookId > 0) {
            Intent intent = new Intent(this, Activity_VerseViewer.class);
            intent.putExtra("ID", BookList.getBookID(bookName));
            startActivity(intent);
        }
        Log.i(CLASS_NAME, "Exiting loadBookFragment");
    }

    public void searchShowResults(final View view) {
        Log.i(CLASS_NAME, "Entering loadBookFragment");
        mTabsAdapter.searchShowResults(view);
        Log.i(CLASS_NAME, "Exiting loadBookFragment");
    }
}
