package com.andrewchelladurai.simplebible.presentation;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.HomeTabOperations;
import com.andrewchelladurai.simplebible.utilities.Constants;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:40 AM
 */
public class HomeTabPresenter {

    private static final String TAG = "SB_HF_Presenter";
    private HomeTabOperations mFragment;

    public HomeTabPresenter(HomeTabOperations fragment) {
        mFragment = fragment;
    }

    public void init() {
        Log.d(TAG, "init() called");
    }

    public String validateBookInput(String input) {
        String returnValue = Constants.SUCCESS_RETURN_VALUE;
        if (null == input || input.isEmpty()) {
            returnValue = mFragment.getBookInputEmptyErrorMessage();
        }
        return returnValue;
    }

    public String validateChapterInput(String input) {
        String returnValue = Constants.SUCCESS_RETURN_VALUE;
        if (null == input || input.isEmpty()) {
            returnValue = mFragment.getChapterInputEmptyErrorMessage();
        }
        return returnValue;
    }
}
