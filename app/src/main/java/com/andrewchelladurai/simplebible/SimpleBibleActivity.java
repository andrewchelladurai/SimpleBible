package com.andrewchelladurai.simplebible;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andrewchelladurai.simplebible.adapter.TabsAdapter;
import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityOperations;
import com.andrewchelladurai.simplebible.presentation.SimpleBibleActivityPresenter;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.io.File;
import java.io.InputStreamReader;

public class SimpleBibleActivity
        extends AppCompatActivity
        implements SimpleBibleActivityOperations {

    // TODO: 22/10/16 Make a notification service
    // TODO: 22/10/16 Select All option, if possible
    // TODO: 22/10/16 remove selective references form existing bookmarks
    // TODO: 22/10/16 swipe gesture
    // TODO: 31/10/16 Fine Tune Bookmark Export

    private static final String TAG = "SB_SBActivity";
    private SimpleBibleActivityPresenter mPresenter;
    private PagerAdapter                 mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start Preference work
        PreferenceManager.setDefaultValues(this, R.xml.preferences_list, false);
        Utilities.setInstance(this);
        if (Utilities.isDarkModeEnabled()) {
            Log.d(TAG, "onCreate: Show Dark Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            Log.d(TAG, "onCreate: Show Light Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // End Preference work

//        welcomeScreen = new WelcomeScreenHelper(this, SplashActivity.class);
//        welcomeScreen.show(savedInstanceState);

        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_simple_bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);
        mPagerAdapter = new TabsAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.activity_simple_bible_container);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_simple_bible_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void init() {
        // init the presenter so it can start the necessary DB and Alarm services.
        mPresenter = new SimpleBibleActivityPresenter(this);
        mPresenter.init();
    }

    @Override
    public void refresh() {
        Log.d(TAG, "refresh() called");
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
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Context getThisApplicationContext() {
        return getApplicationContext();
    }

    @Override
    public String getTabTitle(int position) {
        return mPagerAdapter.getPageTitle(position).toString();
    }

    /**
     * This will return the resource array books_n_chapter_count_array The format of the items must
     * be like this : Book_Name:Chapter_Count Example Genesis:50
     *
     * @return String array
     */
    @Override public String[] getBookNameChapterCountArray() {
        return getResources().getStringArray(R.array.books_n_chapter_count_array);
    }

    @Override public SimpleBibleActivityPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new SimpleBibleActivityPresenter(this);
        }
        return mPresenter;
    }

    @Override public InputStreamReader getMainScript() {
        return mPresenter.getMainScript();
    }

    @Override public InputStreamReader getDowngradeScript() {
        return mPresenter.getDowngradeScript();
    }

    @Override public InputStreamReader getUpgradeScript() {
        return mPresenter.getUpgradeScript();
    }

    @Override public SharedPreferences getDefaultPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override public String getResourceString(int stringId) {
        return getString(stringId);
    }

    @Override public String exportBookmarks() {
        return mPresenter.exportBookmarks();
    }

    @Override public File getBookmarkFileLocation() {
        Log.d(TAG, "getBookmarkFileLocation() called");
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, 0);

        String dir = getResourceString(R.string.application_name)
                             .replaceAll(" ", "_") + File.separator;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), dir);
            if (file.exists()) {
                return file;
            }
            if (file.mkdirs()) {
                return file;
            } else {
                Log.e(TAG, "getBookmarkFileLocation: " + file.getPath()
                           + " Could not be created. Returning null");
                return null;
            }
        }
        Log.e(TAG, "getBookmarkFileLocation: External Storage Unavailable, returning null");
        return null;
    }
}
