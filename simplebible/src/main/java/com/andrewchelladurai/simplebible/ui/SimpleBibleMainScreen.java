/*
 *
 * This file 'SimpleBibleMainScreen.java' is part of SimpleBible :
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
import android.view.View;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ops.BooksScreenOps;
import com.andrewchelladurai.simplebible.ops.HomeScreenOps;
import com.andrewchelladurai.simplebible.ops.MainScreenOps;
import com.andrewchelladurai.simplebible.presenter.MainScreenPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView
    .OnNavigationItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SimpleBibleMainScreen
    extends AppCompatActivity
    implements MainScreenOps {

    private static final String TAG = "SimpleBibleMainScreen";

    private MainScreenPresenter mPresenter;
    private HomeScreenOps mHomeScreenOps;
    private BooksScreenOps mBooksScreenOps;

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SbTheme_Home);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        mPresenter = new MainScreenPresenter(this);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.main_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationListener());

        if (savedInstanceState == null) {
            // hide the bottom bar, we will show it after the DB is loaded
            // and the daily verse is updated on the home screen
            // If the DB could not be loaded, keep the bottom bar hidden
            // and show a message to the use to inform the developer - me

            // hideNavigationControls();
            showHomeScreen();
            // loadDatabase();
        }
    }

    @Override
    public void hideNavigationControls() {
        Log.d(TAG, "hideNavigationControls:");
        findViewById(R.id.main_bottom_navigation).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNavigationControls() {
        Log.d(TAG, "showNavigationControls:");
        findViewById(R.id.main_bottom_navigation).setVisibility(View.VISIBLE);
    }

    private void showHomeScreen() {
        Log.d(TAG, "showHomeScreen:");
        final HomeScreen homeScreen = new HomeScreen();
        mHomeScreenOps = homeScreen;
        final String tag = homeScreen.getClass().getSimpleName();

        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment_container, homeScreen, tag)
                                   .commit();
    }

    private void showBooksScreen() {
        Log.d(TAG, "showBooksScreen:");
        final BooksScreen booksScreen = new BooksScreen();
        mBooksScreenOps = booksScreen;
        final String tag = booksScreen.getClass().getSimpleName();

        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.main_fragment_container, booksScreen, tag)
                                   .commit();
    }

    private class BottomNavigationListener
        implements OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showHomeScreen();
                    return true;
                case R.id.navigation_books:
                    showBooksScreen();
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.bottom_bar_search);
                    return true;
                case R.id.navigation_bookmarks:
                    mTextMessage.setText(R.string.bottom_bar_bookmarks);
                    return true;
                default:
                    Log.e(TAG, "onNavigationItemSelected: unknown [" + item.getTitle() + "]]");
                    return false;
            }
        }
    }
}
