/*
 * This file 'VerseLongClickAlert.java' is part of SimpleBible :
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
 */

package com.andrewchelladurai.simplebible;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 03-Apr-2016 @ 9:42 PM
 */
public class VerseLongClickAlert
        implements View.OnClickListener {

    private static final String TAG = "VerseLongClickAlert";
    boolean isSearchFragment = false;
    private String title;
    private int verseNumber;
    private int chapterNumber;
    private int bookNumber;
    private String bookName;
    private String verseText;
    private Activity parentActivity;
    //    private ListViewCompat verseList;
    private AlertDialog dialog;

    private VerseLongClickAlert(String title, int verseNumber, int chapterNumber, int bookNumber,
                                String bookName, String verseText, Activity activity,
                                ListViewCompat lvc, boolean searchFragment) {
        this.title = title;
        this.verseNumber = verseNumber + 1;
        this.chapterNumber = chapterNumber;
        this.bookNumber = bookNumber;
        this.bookName = bookName;
        this.verseText = verseText;
        parentActivity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setView(R.layout.long_press_dialog);
//        verseList = lvc;
        dialog = builder.create();
        isSearchFragment = searchFragment;
    }

    public static VerseLongClickAlert newInstance(
            String title, int verseNumber, int chapterNumber, int bookNumber,
            String bookName, String verseText, Activity activity, ListViewCompat lvc) {
        return new VerseLongClickAlert(title, verseNumber, chapterNumber, bookNumber,
                bookName, verseText, activity, lvc, false);
    }

    public static VerseLongClickAlert newInstance(
            String title, int verseNumber, int chapterNumber, int bookNumber,
            String bookName, String verseText, FragmentSearch fragment, ListViewCompat lvc) {
        return new VerseLongClickAlert(title, verseNumber, chapterNumber, bookNumber,
                bookName, verseText, fragment.getActivity(), lvc, true);
    }

    public void showDialog() {
        dialog.show();

        AppCompatTextView textView = (AppCompatTextView) dialog.findViewById(
                R.id.long_click_tv_verse_id);
        if (textView != null) {
            String text = title + " : Verse " + verseNumber;
            textView.setText(text);
        }

        bindButton(R.id.long_click_but_share);
        bindButton(R.id.long_click_but_bookmark);
    }

    private void bindButton(int buttonID) {
        AppCompatButton button = (AppCompatButton) dialog.findViewById(buttonID);
        if (button != null) {
            button.setOnClickListener(this);
        }
    }

    private void dialogShareClicked() {
        String value;
        if (isSearchFragment) {
            value = verseText + " -- The Holy Bible (New International Version)";
/*
            value = verseList.getItemAtPosition(verseNumber).toString()
                    + " -- The Holy Bible (New International Version)";
*/
        } else {
            String text = title.replace("Chapter ", "");
            String verse = verseText
//            String verse = verseList.getItemAtPosition(verseNumber).toString()
                    + " -- The Holy Bible (New International Version)";
            value = text + ":" + verse;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, value);
        dialog.dismiss();
        parentActivity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick() called with: " + "viewID = [" + view.getId() + "]");
        switch (view.getId()) {
            case R.id.long_click_but_share:
                dialogShareClicked();
                break;
            case R.id.long_click_but_bookmark:
                dialogBookmarkClicked();
                break;
            default:
                Log.e(TAG, "onClick: ", new RuntimeException("ID " + view.getId() + " is unhandled."));
        }

    }

    private void dialogBookmarkClicked() {
        Log.d(TAG, "dialogBookmarkClicked() called");
        Intent intent = new Intent(parentActivity.getApplicationContext(), ActivityBookmarkedVerse.class);
        intent.putExtra(ActivityBookmarkedVerse.VERSE_NUMBER, verseNumber + "");
        intent.putExtra(ActivityBookmarkedVerse.CHAPTER_NUMBER, chapterNumber + "");
        intent.putExtra(ActivityBookmarkedVerse.BOOK_NUMBER, bookNumber + "");
        dialog.dismiss();
        parentActivity.startActivity(intent);
    }
}
