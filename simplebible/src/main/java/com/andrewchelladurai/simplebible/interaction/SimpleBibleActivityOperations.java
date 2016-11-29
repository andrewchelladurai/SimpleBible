package com.andrewchelladurai.simplebible.interaction;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:55 PM
 */
public interface SimpleBibleActivityOperations
        extends BasicOperations {

    Context getThisApplicationContext();

    String[] getBookNameChapterCountArray();

    InputStreamReader getMainScript();

    InputStreamReader getDowngradeScript();

    InputStreamReader getUpgradeScript();

    SharedPreferences getDefaultPreferences();

    String getResourceString(int stringId);

    String exportBookmarks();

    File getBookmarkFileLocation();
}
