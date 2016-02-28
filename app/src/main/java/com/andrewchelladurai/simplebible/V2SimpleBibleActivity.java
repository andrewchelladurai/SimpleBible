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

public class V2SimpleBibleActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   V2HomeFragment.InteractionListener,
                   V2AboutFragment.InteractionListener,
                   V2SearchFragment.InteractionListener,
                   BooksListFragment.InteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_simple_bible);

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
        getMenuInflater().inflate(R.menu.simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_simple_bible_action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.activity_simple_bible_fragment_container);
        StringBuilder title = new StringBuilder(getString(R.string.app_name));
        switch (item.getItemId()) {
            case R.id.activity_simple_bible_navbar_welcome:
                if (!(fragment instanceof V2HomeFragment)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_simple_bible_fragment_container,
                                     V2HomeFragment.getInstance(""))
                            .commit();
                }
                break;
            case R.id.activity_simple_bible_navbar_otbooks: {
                Utilities utilities = Utilities.getInstance();
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                int columnCount = utilities.getChapterListColumnCount(rotation, getResources());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_simple_bible_fragment_container,
                                 BooksListFragment.getInstance(
                                         BooksListFragment.ARG_OLD_TESTAMENT_LIST,
                                         columnCount))
                        .commit();
                title.append(" : Old Testament");
            }
            break;
            case R.id.activity_simple_bible_navbar_ntbooks: {
                Utilities utilities = Utilities.getInstance();
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                int columnCount = utilities.getChapterListColumnCount(rotation, getResources());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_simple_bible_fragment_container,
                                 BooksListFragment.getInstance(
                                         BooksListFragment.ARG_NEW_TESTAMENT_LIST,
                                         columnCount))
                        .commit();
                title.append(" : New Testament");
            }
            break;
            case R.id.activity_simple_bible_navbar_bookmarked:
                // TODO : Implement and activate a Bookmark Fragment
                title.append(" : Bookmarked");
                break;
            case R.id.activity_simple_bible_navbar_search:
                if (!(fragment instanceof V2SearchFragment)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.activity_simple_bible_fragment_container,
                                    V2SearchFragment.getInstance())
                            .commit();
                }
                title.append(" : Search");
                break;
            case R.id.activity_simple_bible_navbar_about:
                if (!(fragment instanceof V2AboutFragment)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.activity_simple_bible_fragment_container,
                                    V2AboutFragment.getInstance())
                            .commit();
                }
                title.append(" : About");
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
        Toast.makeText(V2SimpleBibleActivity.this,
                       "onHomeFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAboutFragmentInteraction(View view) {
        Toast.makeText(V2SimpleBibleActivity.this,
                       "onAboutFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchFragmentInteraction(View view) {
        Toast.makeText(V2SimpleBibleActivity.this,
                       "onSearchFragmentInteraction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBooksListFragmentInteraction(AllBooks.Book item) {
        Intent intent = new Intent(this, V2ChapterVersesActivity.class);
        intent.putExtra(V2ChapterVersesActivity.ARG_BOOK_NUMBER, item.getBookNumber());
        startActivity(intent);
    }
}
