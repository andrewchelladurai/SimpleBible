package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityInterface;
import com.andrewchelladurai.simplebible.presentation.SimpleBibleActivityPresenter;

public class SimpleBibleActivity
        extends AppCompatActivity
        implements SimpleBibleActivityInterface {

    private static final String TAG = "SB_SimpleBibleActivity";
    private SimpleBibleActivityPresenter mPresenter;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init the presenter so it can start the necessary DB and Alarm services.
        mPresenter = new SimpleBibleActivityPresenter(this);

        setContentView(R.layout.activity_simple_bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.activity_simple_bible_container);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_simple_bible_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.activity_simple_bible_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d(TAG, "onCreate() returned");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple_bible, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override public Context getThisApplicationContext() {
        return getApplicationContext();
    }

    public static class PlaceholderFragment
            extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_simple_bible, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(
                    getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    private class PagerAdapter
            extends FragmentPagerAdapter {

        String mTitles[] = null;

        /**
         * Initializes the super class and also the titles to be used in the Tabs. A string-array
         * "main_activity_tab_titles" is used for this. If the array is not present, the Tabs will
         * have a single entry that uses the application_name resource string.
         *
         * @param fragmentManager
         */
        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            if (null == mTitles || mTitles.length == 0) {
                mTitles = getResources().getStringArray(R.array.main_activity_tab_titles);
                if (mTitles == null || mTitles.length == 0) {
                    mTitles = new String[]{getString(R.string.application_name)};
                    Log.d(TAG, "string-array \"main_activity_tab_titles\" not found");
                }
                Log.d(TAG, "Tab Titles populated with " + mTitles.length + " entries <= " +
                           "This should ideally print only once during init.");
            }
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
