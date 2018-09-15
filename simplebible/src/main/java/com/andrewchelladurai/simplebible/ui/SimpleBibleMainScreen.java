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

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.presenter.DbSetupLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView
    .OnNavigationItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

public class SimpleBibleMainScreen
    extends AppCompatActivity {

    private static final String TAG = "SimpleBibleMainScreen";

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Home);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_simple_bible);

        // Load database
        DbSetupLoaderCallback dbSetupLoaderCallback = new DbSetupLoaderCallback();
        LoaderManager.getInstance(this)
                     .initLoader(R.integer.DB_LOADER, null, dbSetupLoaderCallback)
                     .forceLoad();

        showLoadingScreen();

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationSelectionListener());
    }

    private void showLoadingScreen() {
        Log.d(TAG, "showLoadingScreen: ");
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
        implements OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.main_bottom_navigation_home);
                    return true;
                case R.id.navigation_books:
                    mTextMessage.setText(R.string.main_bottom_navigation_books);
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.main_bottom_navigation_search);
                    return true;
                case R.id.navigation_bookmarks:
                    mTextMessage.setText(R.string.main_bottom_navigation_bookmarks);
                    return true;
            }
            return false;
        }
    }
}
