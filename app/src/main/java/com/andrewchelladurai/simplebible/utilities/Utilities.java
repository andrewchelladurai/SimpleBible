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

import android.app.Activity;
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

import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.SearchResultList.SearchResultItem;
import com.andrewchelladurai.simplebible.model.VerseList.VerseItem;

import java.util.Collection;

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

    public static String prepareBookmarkReferenceFromVerseList(
            @NonNull Collection<VerseItem> items) {
        StringBuilder reference = new StringBuilder();
        String delimiter = Constants.DELIMITER_BETWEEN_REFERENCE;
        for (VerseItem item : items) {
            reference.append(prepareReferenceString(item.getBookNumber(),
                                                    item.getChapterNumber(),
                                                    item.getVerseNumber()))
                     .append(delimiter);
        }
        reference.deleteCharAt(reference.lastIndexOf(delimiter));
        return reference.toString();
    }

    public static String prepareBookmarkReferenceFromSearchResults(
            @NonNull Collection<SearchResultItem> items) {
        StringBuilder reference = new StringBuilder();
        String delimiter = Constants.DELIMITER_BETWEEN_REFERENCE;
        for (SearchResultItem item : items) {
            reference.append(prepareReferenceString(item.getBookNumber(),
                                                    item.getChapterNumber(),
                                                    item.getVerseNumber()))
                     .append(delimiter);
        }
        reference.deleteCharAt(reference.lastIndexOf(delimiter));
        return reference.toString();
    }

    public static String getFormattedReferenceText(int bookNumber, int chapterNumber,
                                                   int verseNumber, String template) {
        String text;
        if (bookNumber == 0 || chapterNumber == 0 || verseNumber == 0) {
            Log.d(TAG, "getFormattedReferenceText: "
                       + "one of the reference parts is 0, returning empty string");
            return "";
        }
        BooksList.BookItem bookItem = BooksList.getBookItem(bookNumber);
        if (bookItem == null) {
            Log.d(TAG, "getFormattedReferenceText: returned BookItem is null");
            return "";
        }
        text = String.format(template, bookItem.getBookName(), chapterNumber, verseNumber);
        return text;
    }

    public static String getShareableTextForReferences(String references, String template) {
        Log.d(TAG, "getShareableTextForReferences() called with references = [" + references + "]");
        String bookName, verseText;
        int bookNumber, chapterNumber, verseNumber;
        StringBuilder shareText = new StringBuilder();
        BooksList.BookItem bookItem;
        DBUtilityOperations dbu = DBUtility.getInstance();

        String eachReference[] = references.split(Constants.DELIMITER_BETWEEN_REFERENCE);
        String parts[];
        for (int i = 0; i < eachReference.length; i++) {
            parts = Utilities.getReferenceParts(eachReference[i]);
            if (parts == null) {
                Log.e(TAG, "getShareableTextForReferences: skipping an invalid reference");
                continue;
            }
            bookNumber = Integer.parseInt(parts[0]);
            chapterNumber = Integer.parseInt(parts[1]);
            verseNumber = Integer.parseInt(parts[2]);
            bookItem = BooksList.getBookItem(bookNumber);
            if (bookItem == null) {
                Log.e(TAG,
                      "getShareableTextForReferences: Skipping invalid bookNumber : " + bookNumber);
                continue;
            }
            bookName = bookItem.getBookName();
            verseText = dbu.getVerseForReference(bookNumber, chapterNumber, verseNumber);
            shareText.append(String.format(template, verseText, bookName, chapterNumber,
                                           verseNumber))
                     .append("\n");
        }
        return shareText.toString();
    }

    public static void restartApplication(Activity activity) {
        Log.d(TAG, "restartApplication() called");
        Context baseContext = activity.getBaseContext();
        Intent intent = baseContext.getPackageManager()
                                   .getLaunchIntentForPackage(baseContext.getPackageName());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "restarting Application");
        baseContext.startActivity(intent);
    }
}
