package com.andrewchelladurai.simplebible.presentation;

import android.content.Context;
import android.util.Log;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityInterface;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:54 PM
 */
public class SimpleBibleActivityPresenter {

    private static final String TAG = "SB_SBActivityPresenter";
    private SimpleBibleActivityInterface mAInterface;
    private String[] mTabTitles = null;

    /**
     * This must be called in the onCreate of the Activity and also before any of the views is
     * created.
     * Reason being, this constructor will init the DB connection etc...
     */
    public SimpleBibleActivityPresenter(SimpleBibleActivityInterface aInterface) {
        mAInterface = aInterface;
        init();
    }

    /**
     * Actual init work is done here.
     */
    private void init() {
        Log.d(TAG, "init: called");
    }

    /**
     * Returns the count of Tabs to show on the activity.
     * Calculates based on the items in the string array defined main_activity_tab_titles
     * @return count of Tabs to show on the activity.
     */
    public int getTabsCount() {
        return getTabTitles().length;
    }

    /**
     * Returns the items in the string array defined main_activity_tab_titles
     * If the array is not defined only one entry with the Application Name is returned
     * @return an array of texts defined to be used as titles on Tabs.
     */
    public String[] getTabTitles() {
        if (null == mTabTitles || mTabTitles.length == 0) {
            Context context = mAInterface.getThisApplicationContext();
            mTabTitles = context.getResources().getStringArray(R.array.main_activity_tab_titles);
            if (mTabTitles == null || mTabTitles.length == 0) {
                mTabTitles = new String[]{context.getString(R.string.application_name)};
                Log.d(TAG, "getTabTitles: mTabTitles main_activity_tab_titles not found");
            }
            Log.d(TAG, "getTabTitles: initialized");
        }
        return mTabTitles;
    }
}
