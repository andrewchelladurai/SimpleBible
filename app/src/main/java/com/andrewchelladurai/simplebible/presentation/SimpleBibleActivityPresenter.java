package com.andrewchelladurai.simplebible.presentation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.andrewchelladurai.simplebible.HomeFragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityInterface;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:54 PM
 */
public class SimpleBibleActivityPresenter {

    private static final String TAG = "SB_SBActivityPresenter";
    private SimpleBibleActivityInterface mInterface;

    /**
     * This must be called in the onCreate of the Activity and also before any of the views is
     * created. Reason being, this constructor will init the DB connection etc...
     */
    public SimpleBibleActivityPresenter(SimpleBibleActivityInterface aInterface) {
        mInterface = aInterface;
        init();
    }

    /**
     * Actual init work is done here.
     */
    private void init() {
        Log.d(TAG, "init: called");
    }

    public Fragment getPageForTitle(String title) {
        Log.d(TAG, "getPageForTitle: " + title);
        Context context = mInterface.getThisApplicationContext();
        if (title.equalsIgnoreCase(context.getString(R.string.main_activity_tab_title_home))) {
            Log.d(TAG, "returning HomeTabFragment()");
            return HomeFragment.newInstance();
        } else if (title.equalsIgnoreCase(
                context.getString(R.string.main_activity_tab_title_books))) {
            Log.d(TAG, "getPageForTitle: books");
        } else if (title.equalsIgnoreCase(
                context.getString(R.string.main_activity_tab_title_search))) {
            Log.d(TAG, "getPageForTitle: search");
        } else if (title.equalsIgnoreCase(
                context.getString(R.string.main_activity_tab_title_notes))) {
            Log.d(TAG, "getPageForTitle: notes");
        }
        return null;
    }
}
