/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 *
 */

package com.andrewchelladurai.simplebible;

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

    static         SharedPreferences   sPreferences;
    private static DataBaseHelper      sDataBaseHelper;
    private        Adapter_TabSections mTabsAdapter;
    private        ViewPager           mPager;

    public static DataBaseHelper getDataBaseHelper() {
        return sDataBaseHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Activity_Settings.changeTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mTabsAdapter = new Adapter_TabSections(getSupportFragmentManager(),
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
                Log.e("Error",
                        "Option Item Selected hit Default : " + item.getTitle());
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
        Intent intent = new Intent(this, Activity_VerseViewer.class);
        intent.putExtra("ID", book.getBookNumber());
        startActivity(intent);
    }

    @Override
    public void onFragmentAboutInteraction(final String id) {
        Log.w("About = ", id + " Pressed");
    }

    @Override
    public void onFragmentSearchInteraction(final String id) {
    }

    public void loadBookFragment(View view) {
        CharSequence bookName = ((EditText) findViewById(R.id.lookup_book)).getText();
        Log.d("BOOK_NAME", BookList.getBookID(bookName) + "");

        Intent intent = new Intent(this, Activity_VerseViewer.class);
        intent.putExtra("ID", BookList.getBookID(bookName));
        startActivity(intent);
    }

    public void aboutSendEmail(View view) {
        mTabsAdapter.aboutSendEmail();
    }

    public void aboutOpenGitHub(View view) {
        mTabsAdapter.aboutOpenGitHub();
    }

    public void aboutOpenGPlus(View view) {
        mTabsAdapter.aboutOpenGPlus();
    }

    public void searchShowResults(final View view) {
        mTabsAdapter.searchShowResults();
    }
}
