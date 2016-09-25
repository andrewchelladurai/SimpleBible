package com.andrewchelladurai.simplebible.presentation;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.HomeTabOperations;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

import java.util.Calendar;

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

    public String getVerseContentForToday() {
        final String defaultReference = Constants.DEFAULT_REFERENCE;
        Log.d(TAG, "getVerseContentForToday() called");
        // get verse reference to use for today
        int dayOfTheYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        Log.d(TAG, "getVerseContentForToday: dayOfTheYear = " + dayOfTheYear);
        String[] array = mFragment.getDailyVerseArray();
        String reference = null;
        if (array == null) {
            reference = defaultReference;
        } else {
            reference = (dayOfTheYear > array.length) ? defaultReference : array[dayOfTheYear];
        }
        Log.d(TAG, "getVerseContentForToday: reference = " + reference);

        // check if the reference is correct
        if (!reference.contains(Constants.DELIMITER_IN_REFERENCE)) {
            // reference does not have delimiter
            reference = defaultReference;
            Log.d(TAG, "getVerseContentForToday: reference does not have delimiter, using default");
        }
        array = reference.split(Constants.DELIMITER_IN_REFERENCE);
        if (array.length != 3) {
            // there are not 3 parts to the reference
            reference = defaultReference;
            array = reference.split(Constants.DELIMITER_IN_REFERENCE);
            Log.d(TAG, "getVerseContentForToday: reference does not have 3 parts, using default");
        }

        int bookNumber, chapterNumber, verseNumber;
        try {
            bookNumber = Integer.parseInt(array[0]);
            chapterNumber = Integer.parseInt(array[1]);
            verseNumber = Integer.parseInt(array[2]);
        } catch (NumberFormatException npe) {
            // the reference could not be parsed correctly
            reference = Constants.DEFAULT_REFERENCE;
            array = reference.split(Constants.DELIMITER_IN_REFERENCE);
            bookNumber = Integer.parseInt(array[0]);
            chapterNumber = Integer.parseInt(array[1]);
            verseNumber = Integer.parseInt(array[2]);
            Log.d(TAG, "getVerseContentForToday: NPE when converting reference, using default");
        }

        // now get the verseText for the reference
        DBUtility dbUtility = DBUtility.getInstance();
        String verseText = dbUtility.getVerseForReference(bookNumber, chapterNumber, verseNumber);

        if (verseText == null) {
            verseText = "No verse found for reference : " + reference;
            return verseText;
        }

        // beautify verse Text
        // FIXME: 25/9/16 beautify verse display

        return verseText;
    }
}
