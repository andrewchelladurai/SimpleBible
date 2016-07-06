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

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

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

    private static final String    TAG            = "SB_Utilities";
    private static       Utilities staticInstance = null;
    private static Resources mResources;

    private Utilities(final Resources pResources) {
        mResources = pResources;
    }

    public static Utilities getInstance(final Resources pResources) {
        if (staticInstance == null) {
            staticInstance = new Utilities(pResources);
            BooksList.populateBooks(pResources.getStringArray(
                    R.array.books_n_chapter_count_array));
            Log.i(TAG, "getInstance: staticInstance initialized");
        }
        return staticInstance;
    }

    public static Intent shareVerse(String textToShare) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");
        Log.i(TAG, "shareVerse: " + textToShare);
        return sendIntent;
    }

    public static void throwError(String errorMessage) {
        throw new AssertionError(errorMessage);
    }

    public static String getFormattedDailyVerse(String[] reference) {
        String bookName = BooksList.getItem(reference[0]).getName();
        DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        String verseText = dbu.getSpecificVerse(Integer.parseInt(reference[0]),
                                                Integer.parseInt(reference[1]),
                                                Integer.parseInt(reference[2]));

        String formattedText = getResourceString(R.string.daily_verse_template);

        formattedText = formattedText.replaceAll(getResourceString(
                R.string.daily_verse_template_book), bookName);
        formattedText = formattedText.replaceAll(getResourceString(
                R.string.daily_verse_template_chapter), reference[1]);
        formattedText = formattedText.replaceAll(getResourceString(
                R.string.daily_verse_template_verse), reference[2]);
        formattedText = formattedText.replaceAll(getResourceString(
                R.string.daily_verse_template_text), verseText);

        return formattedText;
    }

    private static String getResourceString(int resourceID) {
        return mResources.getString(resourceID);
    }
}
