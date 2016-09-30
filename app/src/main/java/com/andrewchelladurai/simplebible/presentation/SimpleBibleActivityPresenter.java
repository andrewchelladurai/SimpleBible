package com.andrewchelladurai.simplebible.presentation;

import android.content.Context;
import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:54 PM
 */
public class SimpleBibleActivityPresenter {

    private static final String TAG = "SB_SBA_Presenter";
    private SimpleBibleActivityOperations mInterface;
    private DBUtility                     dbUtility;

    /**
     * This must be called in the onCreate of the Activity and also before any of the views is
     * created. Reason being, this constructor will init the DB connection etc...
     */
    public SimpleBibleActivityPresenter(SimpleBibleActivityOperations aInterface) {
        mInterface = aInterface;
    }

    /**
     * Actual init work is done here.
     */
    public void init() {
        Log.d(TAG, "init: called");
        String array[] = mInterface.getBookNameChapterCountArray();
        boolean populated = BooksList.populateBooksList(array);
        if (populated) {
            Log.d(TAG, "init: book list populated");
        } else {
            Log.d(TAG, "init: book list NOT populated");
        }

        if (dbUtility == null) {
            Context context = mInterface.getThisApplicationContext();
            if (context == null) {
                Log.d(TAG, "init: context is null");
                return;
            }
            dbUtility = DBUtility.getInstance(mInterface);
        } else {
            Log.d(TAG, "init() dbUtility != null");
        }
    }

    public InputStreamReader getMainScript() {
        Log.d(TAG, "getMainScript() called");
        InputStreamReader reader = null;
        try {
            Context context = mInterface.getThisApplicationContext();
            reader = new InputStreamReader(context.getAssets().open("mainSteps.sql"));
        } catch (IOException ioe) {
            Log.wtf(TAG, "init: Error preparing for DB setup : mainSteps.sql ", ioe);
            ioe.printStackTrace();
        }
        return reader;
    }

    public InputStreamReader getUpgradeScript() {
        Log.d(TAG, "getUpgradeScript() called");
        InputStreamReader reader = null;
        try {
            Context context = mInterface.getThisApplicationContext();
            reader = new InputStreamReader(context.getAssets().open("upgradeSteps.sql"));
        } catch (IOException ioe) {
            Log.wtf(TAG, "init: Error preparing for DB setup : upgradeSteps.sql ", ioe);
            ioe.printStackTrace();
        }
        return reader;
    }

    public InputStreamReader getDowngradeScript() {
        Log.d(TAG, "getDowngradeScript() called");
        InputStreamReader reader = null;
        try {
            Context context = mInterface.getThisApplicationContext();
            reader = new InputStreamReader(context.getAssets().open("downgradeSteps.sql"));
        } catch (IOException ioe) {
            Log.wtf(TAG, "init: Error preparing for DB setup : downgradeSteps.sql ", ioe);
            ioe.printStackTrace();
        }
        return reader;
    }
}
