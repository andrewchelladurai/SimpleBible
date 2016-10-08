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

package com.andrewchelladurai.simplebible.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 25-Sep-2016 @ 1:32 PM
 */
public class Utilities {

    private Utilities() {
    }

    /**
     * Hides the keyboard from the given context and view
     *
     * @param context Context from which to hide the keyboard
     * @param view    View that currently has keyboard focus
     */
    public static void hideKeyboard(@NonNull Context context, @NonNull View view) {
        InputMethodManager im =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static SpannableString getHighlightedText(@NonNull String highlightText,
                                                     @NonNull String completeText,
                                                     int highlightColor) {
        if (completeText.isEmpty()) {
            Log.d(TAG, "getHighlightedText: completeText isEmpty, returning null");
            return null;
        }

        SpannableString formattedText = new SpannableString(completeText);
        if (highlightText.isEmpty()) {
            Log.d(TAG, "getHighlightedText: highlightText isEmpty, returning default");
            return formattedText;
        }

        if (!completeText.contains(highlightText)) {
            Log.d(TAG, "getHighlightedText: highlightText is not present, returning default");
            return formattedText;
        }

        int start = completeText.indexOf(highlightText);
        int end = highlightText.length();

        formattedText.setSpan(new ForegroundColorSpan(highlightColor),
                              start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        formattedText.setSpan(new StyleSpan(Typeface.BOLD),
                              start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return formattedText;
    }

    public static String prepareReferenceString(int bookNumber, int chapterNumber,
                                                int verseNumber) {
        return String.valueOf(bookNumber) + Constants.DELIMITER_IN_REFERENCE +
               String.valueOf(chapterNumber) + Constants.DELIMITER_IN_REFERENCE +
               String.valueOf(verseNumber);
    }

    public static String[] getReferenceParts(@NonNull String reference) {
        if (reference.isEmpty()) {
            Log.d(TAG, "getReferenceParts: " + reference + " isEmpty");
            return null;
        }
        if (!reference.contains(Constants.DELIMITER_IN_REFERENCE)) {
            Log.d(TAG, "getReferenceParts: " + reference + " does not contain DELIMITER");
            return null;
        }
        String parts[] = reference.split(Constants.DELIMITER_IN_REFERENCE);
        if (parts.length != 3) {
            Log.d(TAG, "getReferenceParts: " + reference + " does not have 3 parts");
            return null;
        }
        return parts;
    }

    public static Intent shareVerse(String stringToShare) {
        Log.d(TAG, "shareVerse() called with [" + stringToShare.length() + "] chars");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
        sendIntent.setType("text/plain");
        return sendIntent;
    }
}
