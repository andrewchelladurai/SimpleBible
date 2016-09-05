package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.util.Log;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:54 PM
 */
public class SimpleBibleActivityPresenter {

    private static final String TAG = "SB_SBActivityPresenter";
    private SimpleBibleActivityInterface mAInterface;
    private String[] mTabTitles = null;

    public SimpleBibleActivityPresenter(SimpleBibleActivityInterface aInterface) {
        mAInterface = aInterface;
        init();
    }

    private void init() {
        Log.d(TAG, "init: called");
    }

    public int getTabsCount() {
        return getTabTitles().length;
    }

    public String[] getTabTitles() {
        if (null == mTabTitles || mTabTitles.length == 0) {
            Context context = mAInterface.getAppContext();
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
