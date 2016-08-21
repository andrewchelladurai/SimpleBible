/*
 *
 * This file 'Utilities.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 14-May-2016 @ 9:14 PM
 */
public class Utilities {

    // constants for arguments used in Fragments and Activities.
    public static final String TODAY_VERSE_REFERENCE  = "TODAY_VERSE_REFERENCE";
    public static final String BOOKS_COLUMN_COUNT     = "BOOKS_COLUMN_COUNT";
    public static final String CURRENT_BOOK           = "CURRENT_BOOK";
    public static final String CURRENT_CHAPTER        = "CURRENT_CHAPTER";
    public static final String CURRENT_CHAPTER_NUMBER = "CURRENT_CHAPTER_NUMBER";
    //    public static final String CURRENT_VERSE          = "CURRENT_VERSE";
    public static final String REFERENCES             = "REFERENCES";
    public static final String LOAD_CHAPTER           = "LOAD_CHAPTER";
    public static final String LOAD_CHAPTER_NO        = "LOAD_CHAPTER_NO";
    public static final String LOAD_CHAPTER_YES       = "LOAD_CHAPTER_YES";
    public static final String BOOKMARK_EDIT          = "BOOKMARK_EDIT";
    public static final String BOOKMARK_SAVE          = "BOOKMARK_SAVE";

    public static final String DELIMITER_IN_REFERENCE      = ":";
    public static final String DELIMITER_BETWEEN_REFERENCE = "~";

    private static final String    TAG            = "SB_Utilities";
    private static       Utilities staticInstance = null;
    private static Resources mResources;

    private Utilities(final Resources pResources) {
        mResources = pResources;
    }

    public static Utilities getInstance(final Resources pResources) {
        if (staticInstance == null) {
            staticInstance = new Utilities(pResources);
            ListBooks.populateBooks(pResources.getStringArray(R.array.books_n_chapter_count_array));
            Utilities.log(TAG, "getInstance: staticInstance initialized");
        }
        return staticInstance;
    }

    public static void log(String TAG, String message) {
        Log.i(TAG, message);
    }

    public static Intent shareVerse(String textToShare) {
        Utilities.log(TAG, "shareVerse() textToShare of [" + textToShare.length() + "] length");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    public static void throwError(String errorMessage) {
        throw new AssertionError(errorMessage);
    }

    public static String getFormattedDailyVerse(String[] reference) {
        String bookName = ListBooks.getItem(reference[0]).getName();
        DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        String verseText = dbu.getSpecificVerse(Integer.parseInt(reference[0]),
                                                Integer.parseInt(reference[1]),
                                                Integer.parseInt(reference[2]));

        String fText = getResourceString(R.string.daily_verse_template);

        fText = fText.replaceAll(getResourceString(R.string.daily_verse_template_book), bookName);
        fText = fText.replaceAll(getResourceString(R.string.daily_verse_template_chapter),
                                 reference[1]);
        fText = fText.replaceAll(getResourceString(R.string.daily_verse_template_verse),
                                 reference[2]);
        fText = fText.replaceAll(getResourceString(R.string.daily_verse_template_text), verseText);

        return fText;
    }

    private static String getResourceString(int resourceID) {
        return mResources.getString(resourceID);
    }

    public static String getFormattedChapterVerse(int verseNumber, String verseText) {
        String fText = getResourceString(R.string.chapter_verse_template);
        fText = fText.replaceAll(getResourceString(R.string.chapter_verse_template_verse),
                                 String.valueOf(verseNumber));
        fText = fText.replaceAll(getResourceString(R.string.chapter_verse_template_text),
                                 verseText);
        return fText;
    }

    public static String getFormattedSearchVerse(ListSearch.Entry entry) {
        String bookName = ListBooks.getItem(entry.getBookNumber()).getName();
        String fText = getResourceString(R.string.search_result_template);
        fText = fText.replaceAll(getResourceString(R.string.search_result_template_book), bookName);
        fText = fText.replaceAll(getResourceString(R.string.search_result_template_chapter),
                                 entry.getChapterNumber());
        fText = fText.replaceAll(getResourceString(R.string.search_result_template_verse),
                                 entry.getVerseNumber());
        fText = fText.replaceAll(getResourceString(R.string.search_result_template_text),
                                 entry.getVerse());

        return fText;
    }

    public static String getFormattedBookmarkVerse(
            String bookNumber, String chapterNumber, String verseNumber, String verseText) {
        String bookName = ListBooks.getItem(bookNumber).getName();
        return bookName + " (" + chapterNumber + ":" + verseNumber + ") " + verseText;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager im =
                (InputMethodManager) activity.getApplicationContext()
                                             .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
