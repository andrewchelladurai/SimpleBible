/*
 *
 * This file 'ActivitySimpleBible.java' is part of SimpleBible :
 *
 * Copyright (c) 2018.
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

package com.andrewchelladurai.simplebible.ui;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.andrewchelladurai.simplebible.HomeScreen;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.presenter.DbSetupLoader;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

public class SimpleBibleMainScreen
    extends AppCompatActivity {

    private static final String TAG = "SimpleBibleMainScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Home);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_simple_bible);

        BottomAppBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.replaceMenu(R.menu.main_bottom_navigation);
        bottomBar.setOnMenuItemClickListener(new BottomNavigationSelectionListener());

        FloatingActionButton fab = findViewById(R.id.bottom_bar_fab);
        fab.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                showHomeScreen();
            }
        });

        // Load database
        DbSetupLoaderCallback dbSetupLoaderCallback = new DbSetupLoaderCallback();
        LoaderManager.getInstance(this)
                     .initLoader(R.integer.DB_LOADER, null, dbSetupLoaderCallback)
                     .forceLoad();

        showLoadingScreen();
    }

    private void showSettingsScreen() {
        Log.d(TAG, "showSettingsScreen: ");
    }

    private void showBookmarksScreen() {
        Log.d(TAG, "showBookmarksScreen: ");
    }

    private void showSearchScreen() {
        Log.d(TAG, "showSearchScreen: ");
    }

    private void showBooksScreen() {
        Log.d(TAG, "showBooksScreen: ");
    }

    private void showHomeScreen() {
        Log.d(TAG, "showHomeScreen: ");
    }

    private void showLoadingScreen() {
        Log.d(TAG, "showLoadingScreen: ");

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .add(R.id.content, HomeScreen.newInstance())
                       .commit();
    }

    private void showFailedScreen() {
        Log.d(TAG, "showFailedScreen: ");
    }

    private void showDailyVerse() {
        Log.d(TAG, "showDailyVerse: ");
    }

    private void showSuccessScreen() {
        Log.d(TAG, "showSuccessScreen: ");
    }

    private class DbSetupLoaderCallback
        implements LoaderCallbacks<Boolean> {

        @Override
        public Loader<Boolean> onCreateLoader(final int id, final Bundle args) {
            Log.d(TAG, "onCreateLoader: ");
            return new DbSetupLoader(SimpleBibleMainScreen.this);
        }

        @Override
        public void onLoadFinished(final Loader<Boolean> loader, final Boolean loaded) {
            if (loaded) {
                showDailyVerse();
                showSuccessScreen();
            } else {
                showFailedScreen();
            }
        }

        @Override
        public void onLoaderReset(final Loader<Boolean> loader) {
            Log.d(TAG, "onLoaderReset: ");
            showFailedScreen();
        }
    }

    private class BottomNavigationSelectionListener
        implements OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(final MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_books:
                    showBooksScreen();
                    return true;
                case R.id.navigation_search:
                    showSearchScreen();
                    return true;
                case R.id.navigation_bookmarks:
                    showBookmarksScreen();
                    return true;
                case R.id.navigation_settings:
                    showSettingsScreen();
                    return true;
            }
            return false;
        }
    }

}
