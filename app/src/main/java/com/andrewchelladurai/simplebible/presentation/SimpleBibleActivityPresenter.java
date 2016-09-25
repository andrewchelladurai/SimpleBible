package com.andrewchelladurai.simplebible.presentation;

import android.content.Context;
import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityOperations;
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
        if (dbUtility == null) {
            Context context = mInterface.getThisApplicationContext();
            if (context == null) {
                Log.d(TAG, "init: context is null");
                return;
            }
            InputStreamReader mainSteps = null;
            InputStreamReader upgradeSteps = null;
            InputStreamReader downgradeSteps = null;
            boolean allSet = true;
            Log.d(TAG, "init: Context != null, Preparing to obtain Script Files");
            try {
                mainSteps = new InputStreamReader(
                        context.getAssets().open("mainSteps.sql"));
            } catch (IOException ioe) {
                allSet = false;
                Log.wtf(TAG, "init: Error preparing for DB setup : mainSteps.sql ", ioe);
                ioe.printStackTrace();
            }
            try {
                upgradeSteps = new InputStreamReader(
                        context.getAssets().open("upgradeSteps.sql"));
            } catch (IOException ioe) {
                allSet = false;
                Log.wtf(TAG, "init: Error preparing for DB setup : upgradeSteps.sql ", ioe);
                ioe.printStackTrace();
            }
            try {
                downgradeSteps = new InputStreamReader(
                        context.getAssets().open("downgradeSteps.sql"));
            } catch (IOException ioe) {
                allSet = false;
                Log.wtf(TAG, "init: Error preparing for DB setup : downgradeSteps.sql ", ioe);
                ioe.printStackTrace();
            }
            if (allSet) {
                Log.d(TAG, "init: allSet, proceeding now");
                dbUtility = DBUtility.getInstance(
                        context, mainSteps, upgradeSteps, downgradeSteps);
            }
        } else {
            Log.d(TAG, "init() dbUtility != null");
        }
    }
}
