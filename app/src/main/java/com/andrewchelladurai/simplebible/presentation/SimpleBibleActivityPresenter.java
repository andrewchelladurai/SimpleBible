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
    private SimpleBibleActivityInterface mInterface;

    /**
     * This must be called in the onCreate of the Activity and also before any of the views is
     * created.
     * Reason being, this constructor will init the DB connection etc...
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

}
