package com.andrewchelladurai.simplebible;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 27-Feb-2016 @ 11:18 AM
 */
public class Utilities {

    private static final String TAG = "Utilities";

    private static Utilities staticInstance = new Utilities();

    private Utilities() {
    }

    public static Utilities getInstance() {
        return staticInstance;
    }

    public boolean isLarge(Resources resources) {
        return resources.getBoolean(R.bool.isLarge);
    }

    public int getChapterListColumnCount(Resources resources) {
        int orientation = resources.getConfiguration().orientation;
        int count = 1;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                count = (isLarge(resources)) ? 2 : 1;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                count = (isLarge(resources)) ? 2 : 1;
                break;
        }
        Log.d(TAG, "getChapterListColumnCount() : orientation = [" + orientation + "]"
                   + " count [" + count + "]");
        return count;
    }

}
