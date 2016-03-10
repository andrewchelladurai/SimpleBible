/*
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
 */

package com.andrewchelladurai.simplebible;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ActivitySimpleBible
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   FragmentHome.InteractionListener,
                   FragmentAbout.InteractionListener,
                   FragmentSearch.InteractionListener,
                   FragmentBooksList.InteractionListener,
                   FragmentGotoLocation.InteractionListener {

    private static final String TAG = "ActivitySimpleBible";

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        if (savedState != null) {
            return;
        }

        setContentView(R.layout.activity_simple_bible);

        DatabaseUtility.getInstance(getBaseContext());
        AllBooks.populateBooks(getResources().getStringArray(
                R.array.books_n_chapter_count_array));

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);

/*
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.activity_simple_bible_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_simple_bible_drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar,
                                          R.string.navigation_drawer_open,
                                          R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =
                (NavigationView) findViewById(R.id.activity_simple_bible_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_simple_bible_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
            case R.id.activity_simple_bible_action_settings:
                Intent intent = new Intent(getApplicationContext(), ActivitySettings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.activity_simple_bible_fragment_container);
        StringBuilder title = new StringBuilder(getString(R.string.app_name));
        switch (item.getItemId()) {
            case R.id.activity_simple_bible_navbar_welcome:
                if (!(fragment instanceof FragmentHome)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_simple_bible_fragment_container,
                                     FragmentHome.getInstance(""))
                            .commit();
                }
                break;
            case R.id.activity_simple_bible_navbar_otbooks:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_simple_bible_fragment_container,
                                 FragmentBooksList.getInstance(
                                         FragmentBooksList.ARG_OLD_TESTAMENT_LIST))
                        .commit();
                title.delete(0, title.length()).append("Old Testament");
                break;
            case R.id.activity_simple_bible_navbar_ntbooks:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_simple_bible_fragment_container,
                                 FragmentBooksList.getInstance(
                                         FragmentBooksList.ARG_NEW_TESTAMENT_LIST))
                        .commit();
                title.delete(0, title.length()).append("New Testament");
                break;
            case R.id.activity_simple_bible_navbar_bookmarked:
                // TODO : Implement and activate a Bookmark Fragment
                title.delete(0, title.length()).append("Bookmarked");
                break;
            case R.id.activity_simple_bible_navbar_search:
                if (!(fragment instanceof FragmentSearch)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.activity_simple_bible_fragment_container,
                                    FragmentSearch.getInstance())
                            .commit();
                }
                title.delete(0, title.length()).append("Search");
                break;
            case R.id.activity_simple_bible_navbar_about:
                if (!(fragment instanceof FragmentAbout)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.activity_simple_bible_fragment_container,
                                    FragmentAbout.getInstance())
                            .commit();
                }
                title.delete(0, title.length()).append("About");
                break;
            default:
        }
        setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_simple_bible_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onHomeFragmentInteraction(View view) {
        Toast.makeText(ActivitySimpleBible.this,
                       "onHomeFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAboutFragmentInteraction(View view) {
        Toast.makeText(ActivitySimpleBible.this,
                       "onAboutFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchFragmentInteraction(View view) {
        Toast.makeText(ActivitySimpleBible.this,
                       "onSearchFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBooksListFragmentInteraction(AllBooks.Book item) {
        Intent intent = new Intent(this, ActivityChapterVerses.class);
        intent.putExtra(ActivityChapterVerses.ARG_BOOK_NUMBER, item.getBookNumber());
        startActivity(intent);
    }

    @Override
    public void onGotoFragmentInteraction(View view) {
        Toast.makeText(ActivitySimpleBible.this,
                       "onGotoFragmentInteraction", Toast.LENGTH_SHORT).show();
    }
}
