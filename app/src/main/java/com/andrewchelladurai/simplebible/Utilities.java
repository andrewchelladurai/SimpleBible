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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 14-May-2016 @ 9:14 PM
 */
class Utilities {

    // constants for arguments used in Fragments and Activities.
    public static final  String    TODAY_VERSE_REFERENCE       = "TODAY_VERSE_REFERENCE";
    public static final  String    BOOKS_COLUMN_COUNT          = "BOOKS_COLUMN_COUNT";
    public static final  String    CURRENT_BOOK                = "CURRENT_BOOK";
    public static final  String    CURRENT_CHAPTER             = "CURRENT_CHAPTER";
    public static final  String    CURRENT_CHAPTER_NUMBER      = "CURRENT_CHAPTER_NUMBER";
    public static final  String    REFERENCES                  = "REFERENCES";
    public static final  String    LOAD_CHAPTER                = "LOAD_CHAPTER";
    public static final  String    LOAD_CHAPTER_NO             = "LOAD_CHAPTER_NO";
    public static final  String    LOAD_CHAPTER_YES            = "LOAD_CHAPTER_YES";
    public static final  String    BOOKMARK_EDIT               = "BOOKMARK_EDIT";
    public static final  String    BOOKMARK_SAVE               = "BOOKMARK_SAVE";
    public static final  String    DELIMITER_IN_REFERENCE      = ":";
    public static final  String    DELIMITER_BETWEEN_REFERENCE = "~";
    private static final int       VERSE_SIZE_SMALL            = -1;
    private static final int       VERSE_SIZE_MEDIUM           = 0;
    private static final int       VERSE_SIZE_BIG              = 1;
    private static final int       VERSE_SIZE_LARGE            = 2;
    private static final int       VERSE_STYLE_NORMAL          = 0;
    private static final int       VERSE_STYLE_OLD_ENGLISH     = 1;
    private static final int       VERSE_STYLE_TYPEWRITER      = 2;
    private static final String    TAG                         = "SB_Utilities";
    private static       Utilities staticInstance              = null;
    private static Resources         mResources;
    private static SharedPreferences mPreferences;

    private Utilities(ActivitySimpleBible pActivity) {
        mResources = pActivity.getResources();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(pActivity);
        ListBooks.populateBooks(mResources.getStringArray(R.array.books_n_chapter_count_array));
    }

    public static void createInstance(final ActivitySimpleBible pActivity) {
        if (staticInstance == null) {
            staticInstance = new Utilities(pActivity);
            Utilities.log(TAG, "createInstance: staticInstance initialized");
        }
    }

    public static void log(String TAG, String message) {
        Log.i(TAG, message);
    }

    public static void throwError(String tag, String errorMessage) {
        throw new AssertionError(tag + ": " + errorMessage);
    }

    public static Intent shareVerse(String textToShare) {
        Utilities.log(TAG, "shareVerse() textToShare of [" + textToShare.length() + "] length");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    public static String getFormattedDailyVerse(String[] reference) {
        DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        String bookName = ListBooks.getItem(reference[0]).getName();
        String chapterNumber = reference[1];
        String verseNumber = reference[2];
        String verseText = dbu.getSpecificVerse(Integer.parseInt(reference[0]),
                                                Integer.parseInt(reference[1]),
                                                Integer.parseInt(reference[2]));

        return String.format(getString(R.string.daily_verse_template),
                             verseText, bookName, chapterNumber, verseNumber);
    }

    private static String getString(int resourceID) {
        return mResources.getString(resourceID);
    }

    public static SpannableString getFormattedChapterVerse(
            Context context, int verseNumber, String verseText) {

        String verseNum = String.valueOf(verseNumber);
        String fText =
                String.format(getString(R.string.chapter_verse_template), verseNum, verseText);

        // Will be styling this
        SpannableString sText = new SpannableString(fText);
        int spanEnd = fText.indexOf(verseNum) + verseNum.length();
        highlightRangeWithColorInVerse(sText, 0, spanEnd, context);
        spanEnd = fText.length();
        setPreferredVerseSizeToText(sText, 0, spanEnd);
        setPreferredVerseStyleToText(sText, 0, spanEnd);

        return sText;
    }

    private static void highlightRangeWithColorInVerse(
            SpannableString text, int start, int end, Context context) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.verse_reference_color));
        text.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void setPreferredVerseSizeToText(SpannableString text, int start, int end) {
        float size;
        switch (Utilities.getPreferredVerseSize()) {
            case VERSE_SIZE_SMALL:
                size = 0.8f;
                break;
            case VERSE_SIZE_MEDIUM:
                size = 1.0f;
                break;
            case VERSE_SIZE_BIG:
                size = 1.2f;
                break;
            case VERSE_SIZE_LARGE:
                size = 1.4f;
                break;
            default: // set to medium
                size = 1.0f;
        }
        text.setSpan(new RelativeSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void setPreferredVerseStyleToText(SpannableString text, int start, int end) {
        String typeface;
        switch (getPreferredVerseStyle()) {
            case VERSE_STYLE_NORMAL:
                typeface = "sans-serif";
                break;
            case VERSE_STYLE_OLD_ENGLISH:
                typeface = "serif";
                break;
            case VERSE_STYLE_TYPEWRITER:
                typeface = "monospace";
                break;
            default:
                typeface = Typeface.DEFAULT.toString();
        }
        text.setSpan(new TypefaceSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Checks for the preferred size to use for Verses.
     *
     * @return one of the VERSE_SIZE_(SMALL|MEDIUM|BIG|LARGE) values.
     */
    private static int getPreferredVerseSize() {
        String value = mPreferences.getString(getString(R.string.pref_key_text_size),
                                              getString(R.string.pref_key_text_size_default));
        int size;
        if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_small))) {
            size = VERSE_SIZE_SMALL;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_medium))) {
            size = VERSE_SIZE_MEDIUM;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_big))) {
            size = VERSE_SIZE_BIG;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_large))) {
            size = VERSE_SIZE_LARGE;
        } else {
            size = VERSE_SIZE_MEDIUM;
        }
        return size;
    }

    /**
     * Checks for the preferred style to use for Verses.
     *
     * @return one of the VERSE_STYLE_(NORMAL|OLD_ENGLISH|TYPEWRITER) values.
     */
    private static int getPreferredVerseStyle() {
        String value = mPreferences.getString(getString(R.string.pref_key_text_style),
                                              getString(R.string.pref_key_text_style_default));
        int style;
        if (value.equalsIgnoreCase(getString(R.string.pref_key_text_style_normal))) {
            style = VERSE_STYLE_NORMAL;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_style_old_english))) {
            style = VERSE_STYLE_OLD_ENGLISH;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_style_typewriter))) {
            style = VERSE_STYLE_TYPEWRITER;
        } else {
            style = VERSE_STYLE_NORMAL;
        }
        return style;
    }

    public static SpannableString getFormattedSearchVerse(Context context, ListSearch.Entry entry) {

        // Get all necessary items ready
        String bookName = ListBooks.getItem(entry.getBookNumber()).getName();
        String chapterNumber = entry.getChapterNumber();
        String verseNumber = entry.getVerseNumber();
        String verseText = entry.getVerse();
        String fText = String.format(getString(R.string.search_result_template),
                                     bookName, chapterNumber, verseNumber, verseText);

        // this is the text we will style
        SpannableString sText = new SpannableString(fText);

        int spanEnd = (fText.indexOf(verseNumber) + verseNumber.length()) + 1;
        highlightRangeWithColorInVerse(sText, 0, spanEnd, context);
        spanEnd = fText.length();
        setPreferredVerseSizeToText(sText, 0, spanEnd);
        setPreferredVerseStyleToText(sText, 0, spanEnd);

        return sText;
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

    public static boolean isDarkModeEnabled() {
        return mPreferences.getBoolean(getString(R.string.pref_key_theme_dark), false);
    }

    public static void restartApplication(Activity pActivity) {
        Utilities.log(TAG, "restartApplication() called");
        Context baseContext = pActivity.getBaseContext();
        Intent intent = baseContext.getPackageManager()
                                   .getLaunchIntentForPackage(baseContext.getPackageName());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Utilities.log(TAG, "restarting Application");
        baseContext.startActivity(intent);
    }

    public static String[] getStringArray(int arrayId) {
        return mResources.getStringArray(arrayId);
    }
}
