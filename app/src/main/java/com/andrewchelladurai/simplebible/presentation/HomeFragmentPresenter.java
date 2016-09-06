package com.andrewchelladurai.simplebible.presentation;

import android.content.Context;
import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.HomeFragmentInterface;
import com.andrewchelladurai.simplebible.utilities.Constants;

import static com.andrewchelladurai.simplebible.R.*;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:40 AM
 */
public class HomeFragmentPresenter {

    private static final String TAG = "SB_HF_Presenter";
    private HomeFragmentInterface mFragment;

    public HomeFragmentPresenter(HomeFragmentInterface fragment) {
        mFragment = fragment;
    }

    public void init() {
        Log.d(TAG, "init() called");
    }

    public boolean gotoLocationClicked() {
        Log.d(TAG, "gotoLocationClicked");
        String returnValue = validateBookInput();
        if (!returnValue.equalsIgnoreCase(Constants.SUCCESS_RETURN_VALUE)) {
            mFragment.showError(returnValue);
            mFragment.focusBookInputField();
            return false;
        }
        returnValue = validateChapterInput();
        if (!returnValue.equalsIgnoreCase(Constants.SUCCESS_RETURN_VALUE)) {
            mFragment.showError(returnValue);
            mFragment.focusChapterInputField();
            return false;
        }
        Log.d(TAG, "gotoLocationClicked() returned");
        return true;
    }

    private String validateBookInput() {
        String returnValue = Constants.SUCCESS_RETURN_VALUE;
        String input = mFragment.getBookInput();
        Context context = mFragment.getAppContext();
        if (input.isEmpty()) {
            returnValue = context.getString(string.err_msg_goto_empty_book_input);
        }
        return returnValue;
    }

    private String validateChapterInput() {
        String returnValue = Constants.SUCCESS_RETURN_VALUE;
        String input = mFragment.getChapterInput();
        Context context = mFragment.getAppContext();
        if (input.isEmpty()) {
            returnValue = context.getString(string.err_msg_goto_empty_chapter_input);
        }
        return returnValue;
    }
}
