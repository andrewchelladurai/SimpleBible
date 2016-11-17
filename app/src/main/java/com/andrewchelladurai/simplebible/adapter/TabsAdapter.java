package com.andrewchelladurai.simplebible.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.andrewchelladurai.simplebible.BookmarksFragment;
import com.andrewchelladurai.simplebible.BooksFragment;
import com.andrewchelladurai.simplebible.HomeFragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.SearchFragment;
import com.andrewchelladurai.simplebible.SimpleBibleActivity;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 08-Sep-2016 @ 10:59 PM
 */
public class TabsAdapter
        extends FragmentPagerAdapter {

    private static final String TAG = "SB_PagerAdapter";
    private final SimpleBibleActivity mActivity;
    private String mTitles[] = null;

    /**
     * Initializes the super class and also the titles to be used in the Tabs. A string-array
     * "main_activity_tab_titles" is used for this. If the array is not present, the Tabs will have
     * a single entry that uses the application_name resource string.
     *
     * @param fragmentManager passed to the superclass.
     */
    public TabsAdapter(SimpleBibleActivity activity, FragmentManager fragmentManager) {
        super(fragmentManager);
        mActivity = activity;
        mTitles = mActivity.getResources().getStringArray(R.array.main_activity_tab_titles);
        if (mTitles.length == 0) {
            mTitles = new String[]{mActivity.getString(R.string.application_name)};
            Log.d(TAG, "string-array \"main_activity_tab_titles\" not found");
        }
        Log.d(TAG, "Tab Titles populated with " + mTitles.length + " entries <= " +
                   "This should ideally print only once during init.");
    }

    @Override
    public Fragment getItem(int position) {
        String title = getPageTitle(position).toString();
        if (title.equalsIgnoreCase(
                mActivity.getString(R.string.activity_simple_bible_tab_title_home))) {
            Log.d(TAG, "returning HomeTabFragment()");
            return HomeFragment.newInstance();
        } else if (title.equalsIgnoreCase(
                mActivity.getString(R.string.activity_simple_bible_tab_title_books))) {
            Log.d(TAG, "returning BooksFragment()");
            return BooksFragment.newInstance();
        } else if (title.equalsIgnoreCase(
                mActivity.getString(R.string.activity_simple_bible_tab_title_search))) {
            Log.d(TAG, "returning SearchFragment()");
            return SearchFragment.newInstance();
        } else if (title.equalsIgnoreCase(
                mActivity.getString(R.string.activity_simple_bible_tab_title_notes))) {
            Log.d(TAG, "returning BookmarksFragment()");
            return BookmarksFragment.newInstance();
        } else {
            Log.d(TAG, "getItem: " + mActivity.getString(R.string.how_am_i_here));
            Log.d(TAG, "getItem: Returning another Home Fragment");
            // TODO: 9/11/16 Handle this : may be return a blank fragment
            return HomeFragment.newInstance();
        }
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
