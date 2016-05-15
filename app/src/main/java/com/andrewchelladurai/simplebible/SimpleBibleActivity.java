package com.andrewchelladurai.simplebible;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleBibleActivity
        extends AppCompatActivity {

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_bible);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_simple_bible_toolbar);
        setSupportActionBar(toolbar);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.activity_simple_bible_container);
        if (null != mViewPager) {
            mViewPager.setAdapter(mPagerAdapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_simple_bible_tabs);
        if (null != tabLayout) {
            tabLayout.setupWithViewPager(mViewPager);
        }

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.activity_simple_bible_fab);
        if (null != fab) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        mViewPager.addOnPageChangeListener(new KeyboardHideListener(this));
        Book.populateBooks(getResources().getStringArray(R.array.books_n_chapter_count_array));
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
                settingsButtonClicked();
                return true;
            case R.id.action_about:
                aboutButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void aboutButtonClicked() {
        // FIXME: 14/5/16 Handle About Click
    }

    private void settingsButtonClicked() {
        // FIXME: 14/5/16 Handle Settings Click
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

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return BookListFragment.newInstance();
                case 2:
                    return SearchFragment.newInstance();
                case 3:
                    return PlaceholderFragment.newInstance(position + 1);
                default: // FIXME: 14/5/16
                    return HomeFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOME";
                case 1:
                    return "BOOKS";
                case 2:
                    return "SEARCH";
                case 3:
                    return "NOTES";
                default: // FIXME:
                    return "BLANK";
            }
        }
    }
}
