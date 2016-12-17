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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.SearchResultList.SearchResultItem;
import com.andrewchelladurai.simplebible.model.VerseList.VerseItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 25-Sep-2016 @ 1:32 PM
 */
public class Utilities {

    private static final String TAG = "SB_Utilities";
    private static SimpleBibleActivityOperations mOperations;

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

    public static SpannableString getStyledText(@NonNull String highlightText,
                                                @NonNull String completeText,
                                                int highlightColor) {
        if (completeText.isEmpty()) {
            Log.e(TAG, "getStyledText: completeText isEmpty, returning null");
            return null;
        }

        SpannableString formattedText = new SpannableString(completeText);
        if (highlightText.isEmpty()) {
            Log.e(TAG, "getStyledText: highlightText isEmpty, returning default");
            return formattedText;
        }

        if (!completeText.contains(highlightText)) {
            Log.e(TAG, "getStyledText: highlightText is not present, returning default");
            return formattedText;
        }

        final int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

        try {
            // highlight specified text
            int start = completeText.indexOf(highlightText);
            int end = highlightText.length();

            formattedText.setSpan(new ForegroundColorSpan(highlightColor), start, end, flag);
            formattedText.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);

            // style complete text
            start = 0;
            end = completeText.length();
            formattedText.setSpan(new RelativeSizeSpan(getPreferredTextSize()), start, end, flag);
            formattedText.setSpan(new TypefaceSpan(getPreferredVerseStyle()), start, end, flag);
        } catch (Exception e) {
            Log.e(TAG, "getStyledText: returning default " + e.getLocalizedMessage());
            return formattedText;
        }

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
            Log.e(TAG, "getReferenceParts: returning null - " + reference + " isEmpty");
            return null;
        }
        if (!reference.contains(Constants.DELIMITER_IN_REFERENCE)) {
            Log.e(TAG, "getReferenceParts: returning null - no DELIMITER found in reference");
            return null;
        }
        String parts[] = reference.split(Constants.DELIMITER_IN_REFERENCE);
        if (parts.length != 3) {
            Log.e(TAG, "getReferenceParts: returning null - reference does not have 3 parts");
            return null;
        }
        return parts;
    }

    public static Intent shareVerse(@NonNull String stringToShare) {
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
            Log.e(TAG, "getFormattedReferenceText: "
                       + "one of the reference parts is 0, returning empty string");
            return "";
        }
        BooksList.BookItem bookItem = BooksList.getBookItem(bookNumber);
        if (bookItem == null) {
            Log.e(TAG, "getFormattedReferenceText: returning empty string - got a null BookItem");
            return "";
        }
        text = String.format(template, bookItem.getBookName(), chapterNumber, verseNumber);
        return text;
    }

    public static String getShareableTextForReferences(@NonNull String references,
                                                       @NonNull String template) {
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
                Log.e(TAG, "getShareableTextForReferences: Skipping invalid bookNumber : "
                           + bookNumber);
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

    public static void restartApplication(@NonNull Activity activity) {
        Log.d(TAG, "restartApplication() called");
        Context baseContext = activity.getBaseContext();
        Intent intent = baseContext.getPackageManager()
                                   .getLaunchIntentForPackage(baseContext.getPackageName());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG, "restarting Application");
        baseContext.startActivity(intent);
    }

    /**
     * Checks for the preferred size to use for Verses.
     *
     * @return one of the VERSE_SIZE_(SMALL|MEDIUM|BIG|LARGE) values.
     */
    private static float getPreferredTextSize() {
        String value = getPreferences().getString(getString(R.string.pref_key_text_size),
                                                  getString(R.string.pref_key_text_size_default));
        float size;
        if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_small))) {
            size = Constants.VERSE_SIZE_SMALL;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_medium))) {
            size = Constants.VERSE_SIZE_MEDIUM;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_big))) {
            size = Constants.VERSE_SIZE_BIG;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_size_large))) {
            size = Constants.VERSE_SIZE_LARGE;
        } else {
            size = Constants.VERSE_SIZE_MEDIUM;
        }
        return size;
    }

    /**
     * Checks for the preferred style to use for Verses.
     *
     * @return one of the VERSE_STYLE_(NORMAL|OLD_ENGLISH|TYPEWRITER) values.
     */
    private static String getPreferredVerseStyle() {
        String value = getPreferences().getString(getString(R.string.pref_key_text_style),
                                                  getString(R.string.pref_key_text_style_default));
        String style;
        if (value.equalsIgnoreCase(getString(R.string.pref_key_text_style_normal))) {
            style = Constants.VERSE_STYLE_NORMAL;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_style_old_english))) {
            style = Constants.VERSE_STYLE_OLD_ENGLISH;
        } else if (value.equalsIgnoreCase(getString(R.string.pref_key_text_style_typewriter))) {
            style = Constants.VERSE_STYLE_TYPEWRITER;
        } else {
            style = Constants.VERSE_STYLE_NORMAL;
        }
        return style;
    }

    private static String getString(int stringId) {
        return mOperations.getResourceString(stringId);
    }

    public static boolean isDarkModeEnabled() {
        return getPreferences().getBoolean(getString(R.string.pref_key_theme_dark), false);
    }

    private static SharedPreferences getPreferences() {
        return mOperations.getDefaultPreferences();
    }

    public static void setInstance(SimpleBibleActivityOperations operations) {
        mOperations = operations;
    }

    public static String exportBookmarks() {
        return mOperations.exportBookmarks();
    }

    public static int getReminderHour() {
        Calendar calendar = Calendar.getInstance();
        return getPreferences().getInt(
                getString(R.string.pref_key_reminder_hour), calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static int getReminderMinute() {
        Calendar calendar = Calendar.getInstance();
        return getPreferences().getInt(
                getString(R.string.pref_key_reminder_minute), calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static void setReminderTime(final int hour, final int minute) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(getString(R.string.pref_key_reminder_hour), hour);
        editor.putInt(getString(R.string.pref_key_reminder_minute), minute);
        editor.apply();
        editor.commit();
    }

    private static Context getApplicationContext() {
        return mOperations.getThisApplicationContext();
    }

    public static void enableAndUpdateReminder() {
        Log.d(TAG, "enableAndUpdateReminder() called");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, getReminderHour());
        calendar.set(Calendar.MINUTE, getReminderMinute());
        calendar.set(Calendar.SECOND, 0);

        final Context context = mOperations.getThisApplicationContext();
        Intent intent = new Intent(context, NotificationDisplayer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d(TAG, "enableAndUpdateReminder() returned");
    }

    public static String getVerseContentForToday(@NonNull String[] reference,
                                                 @NonNull String template) {
        Log.d(TAG, "getVerseContentForToday() called");
        int bookNumber, chapterNumber, verseNumber;
        final String defaultReference = Constants.DEFAULT_REFERENCE;
        try {
            bookNumber = Integer.parseInt(reference[0]);
            chapterNumber = Integer.parseInt(reference[1]);
            verseNumber = Integer.parseInt(reference[2]);
        } catch (NumberFormatException npe) {
            // the reference could not be parsed correctly
            reference = defaultReference.split(Constants.DELIMITER_IN_REFERENCE);
            bookNumber = Integer.parseInt(reference[0]);
            chapterNumber = Integer.parseInt(reference[1]);
            verseNumber = Integer.parseInt(reference[2]);
            Log.e(TAG, "getVerseContentForToday: NPE when converting reference, using default",
                  npe);
        }

        // now get the verseText for the reference
        DBUtilityOperations dbUtility = DBUtility.getInstance();
        String verseText = dbUtility.getVerseForReference(bookNumber, chapterNumber, verseNumber);

        if (verseText == null || verseText.isEmpty()) {
            Log.e(TAG, "getVerseContentForToday: No verse found for reference " + Arrays
                    .toString(reference) + "\nUsing Default Reference");
            reference = defaultReference.split(Constants.DELIMITER_IN_REFERENCE);
            return getVerseContentForToday(reference, template);
        }

        // Beautify the Verse
        BooksList.BookItem bookItem = BooksList.getBookItem(bookNumber);
        String bookName = (null == bookItem) ? "" : bookItem.getBookName();

        return String.format(template, verseText, bookName, chapterNumber, verseNumber);
    }

    public static String[] getVerseReferenceForToday() {
        Log.d(TAG, "getVerseReferenceForToday() called");

        final String defaultReference = Constants.DEFAULT_REFERENCE;
        // get verse reference to use for today
        int dayOfTheYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        Log.d(TAG, "getVerseReferenceForToday: dayOfTheYear = " + dayOfTheYear);
        String reference;
        String[] verseArray = getApplicationContext()
                .getResources().getStringArray(R.array.daily_verse_list);

        reference = (dayOfTheYear > verseArray.length) ? defaultReference
                                                       : verseArray[dayOfTheYear];

        if (reference == null || reference.isEmpty()) {
            Log.e(TAG, "getVerseReferenceForToday: Empty reference, using default");
            reference = defaultReference;
        }
        Log.d(TAG, "getVerseReferenceForToday: reference = " + reference);

        // check if the reference is correct
        if (!reference.contains(Constants.DELIMITER_IN_REFERENCE)) {
            // reference does not have delimiter
            reference = defaultReference;
            Log.e(TAG,
                  "getVerseReferenceForToday: reference does not have delimiter, using default");
        }
        verseArray = reference.split(Constants.DELIMITER_IN_REFERENCE);
        if (verseArray.length != 3) {
            // there are not 3 parts to the reference
            reference = defaultReference;
            verseArray = reference.split(Constants.DELIMITER_IN_REFERENCE);
            Log.d(TAG,
                  "getVerseReferenceForToday: reference does not have 3 parts, using default");
        }
        return verseArray;
    }
}
