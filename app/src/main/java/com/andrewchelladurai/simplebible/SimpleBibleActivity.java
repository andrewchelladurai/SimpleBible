package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andrewchelladurai.simplebible.adapter.TabsAdapter;
import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityOperations;
import com.andrewchelladurai.simplebible.presentation.SimpleBibleActivityPresenter;

public class SimpleBibleActivity
    extends AppCompatActivity
    implements SimpleBibleActivityOperations {

    private static final String TAG = "SB_SBActivity";
    private SimpleBibleActivityPresenter mPresenter;
    private PagerAdapter                 mPagerAdapter;
    private ViewPager                    mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_simple_bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);
        mPagerAdapter = new TabsAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.activity_simple_bible_container);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_simple_bible_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Log.d(TAG, "onCreate() returned");
    }

    @Override
    public void init() {
        Log.d(TAG, "init() called");
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
}
