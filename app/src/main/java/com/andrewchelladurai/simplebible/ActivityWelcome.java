/*
 * This file 'ActivityWelcome.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Copyright (c) Andrew Chelladurai - 2015.
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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

public class ActivityWelcome
        extends ActionBarActivity
        implements ActionBar.TabListener,
                   FragmentBooks.OnFragmentInteractionListener,
                   Fragment_Search.OnFragmentInteractionListener,
                   OnSharedPreferenceChangeListener {

    private static final String TAG = "ActivityWelcome";
    private static HelperDatabase sHelperDatabase;
    private AdapterTabSections mTabsAdapter;
    private ViewPager mPager;

    public static HelperDatabase getDataBaseHelper() {
        return sHelperDatabase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Entering onCreate");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Utilities.createInstance(sharedPref);
        Utilities.updateTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mTabsAdapter = new AdapterTabSections(getSupportFragmentManager(),
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

        if (sHelperDatabase == null) {
            sHelperDatabase = new HelperDatabase(this, "NIV.db");
            getDataBaseHelper().openDataBase();
        }

        sharedPref.registerOnSharedPreferenceChangeListener(this);
        Log.i(TAG, "Exiting onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, ActivitySettings.class));
                return true;
            case R.id.action_about:
                FragmentDialogAbout fda = new FragmentDialogAbout();
                fda.show(getSupportFragmentManager(), "about");
                return true;
            case R.id.action_reminder:
//                FragmentDialogAbout fda = new FragmentDialogAbout();
//                fda.show(getSupportFragmentManager(), "about");
                return true;
            default:
                Log.e(TAG, "Error : Option Item Selected hit Default : " + item.getTitle());
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
    public void onFragmentBooksInteraction(final BookUnit book) {
        Log.i(TAG, "Entering onFragmentBooksInteraction");
        Intent intent = new Intent(this, ActivityVerseViewer.class);
        intent.putExtra("ID", book.getBookNumber());
        startActivity(intent);
        Log.i(TAG, "Exiting onFragmentBooksInteraction");
    }

    @Override
    public void onFragmentSearchInteraction(final String id) {
    }

    public void loadBookFragment(View view) {
        Log.i(TAG, "Entering loadBookFragment");
        CharSequence bookName = ((EditText) findViewById(R.id.lookup_book)).getText();
        int bookId = BookSList.getBookID(bookName);
        Log.d(TAG, "BOOK NAME : " + bookId);
        if (bookName.length() > 0 && bookId > 0) {
            Intent intent = new Intent(this, ActivityVerseViewer.class);
            intent.putExtra("ID", BookSList.getBookID(bookName));
            startActivity(intent);
        }
        Log.i(TAG, "Exiting loadBookFragment");
    }

    public void searchShowResults(final View view) {
        Log.i(TAG, "Entering loadBookFragment");
        mTabsAdapter.searchShowResults(view);
        Log.i(TAG, "Exiting loadBookFragment");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged() called with: " + "sharedPreferences = [" +
                   sharedPreferences + "], s = [" + s + "]");
        if (s.equalsIgnoreCase("pref_app_theme")) {
            Utilities.changeTheme(this);
        } else if (s.equalsIgnoreCase("notifications_new_message")) {
            Log.d(TAG, "onSharedPreferenceChanged() :" +
                       "Inside : else if (s.equalsIgnoreCase(\"notifications_new_message\")){");
        }

    }
}
