/*
 * This file 'ActivityBookmarkedVerse.java' is part of SimpleBible :
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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ActivityBookmarkedVerse
        extends AppCompatActivity
        implements View.OnClickListener {

    public static final String VERSE_NUMBER = "VERSE_NUMBER";
    public static final String CHAPTER_NUMBER = "CHAPTER_NUMBER";
    public static final String BOOK_NUMBER = "BOOK_NUMBER";
    private static final String TAG = "ActivityBookmarkedVerse";
    public String verseText = "";
    public String verseNotes = "";
    private int verseNumber = -1;
    private int chapterNumber = -1;
    private int bookNumber = -1;
    //    private AppCompatTextView compVerseNotesView;
    private AppCompatEditText compVerseNotesEdit;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        verseNumber = Integer.parseInt(getIntent().getStringExtra(VERSE_NUMBER));
        chapterNumber = Integer.parseInt(getIntent().getStringExtra(CHAPTER_NUMBER));
        bookNumber = Integer.parseInt(getIntent().getStringExtra(BOOK_NUMBER));
        verseText = "Blank";
        verseNotes = "Blank";

        setContentView(R.layout.activity_bookmarked_verse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_bookmarked_verse_toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        StringBuilder tempString = new StringBuilder("");

        BookNameContent.BookItem book = BookNameContent.getBookItem(bookNumber);
        if (book != null) {
            tempString = tempString.append(book.getName()).append(" Chapter ").append(chapterNumber)
                    .append(" Verse ").append(verseNumber);
        }

        AppCompatTextView compVerseID = (AppCompatTextView) findViewById(R.id.bookmarked_verse_ID);
        AppCompatTextView compVerseText = (AppCompatTextView) findViewById(R.id.bookmarked_verse_text);
//        compVerseNotesView = (AppCompatTextView) findViewById(R.id.bookmarked_verse_notes_view);
        compVerseNotesEdit = (AppCompatEditText) findViewById(R.id.bookmarked_verse_notes_edit);

        if (null != compVerseID) {
            compVerseID.setText(tempString.toString());
            tempString.delete(0, tempString.length());
        }

        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        tempString.append(dbu.getSpecificVerse(bookNumber, chapterNumber, verseNumber));

        if (null != compVerseText) {
            compVerseText.setText(tempString);
            tempString.delete(0, tempString.length());
        }

        bindButton(R.id.bookmarked_verse_button_edit);
        bindButton(R.id.bookmarked_verse_button_delete);
        bindButton(R.id.bookmarked_verse_button_save);
    }

    private void bindButton(int button_id) {
        switch (button_id) {
            case R.id.bookmarked_verse_button_edit:
                ToggleButton tbutton = (ToggleButton) findViewById(button_id);
                if (null != tbutton) {
                    tbutton.setOnClickListener(this);
                }
                break;
            default:
                AppCompatButton button = (AppCompatButton) findViewById(button_id);
                if (null != button) {
                    button.setOnClickListener(this);
                }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bookmarked_verse_button_edit:
                ToggleButton tbutton = (ToggleButton) view;
                String label = tbutton.getText() + "";
                if (label.equalsIgnoreCase(getString(R.string.bm_button_edit_on_label))) {
                    enableNotesEditing();
                } else if (label.equalsIgnoreCase(getString(R.string.bm_button_edit_off_label))) {
                    disableNotesEditing();
                } else {
                    Log.e(TAG, "onClick()", new Throwable(
                            getString(R.string.this_must_never_show)));
                }
                break;
            case R.id.bookmarked_verse_button_delete:
                deleteButtonClicked();
                break;
            case R.id.bookmarked_verse_button_save:
                saveButtonClicked();
                break;
            default:
        }
    }

    private void saveButtonClicked() {
        Toast.makeText(ActivityBookmarkedVerse.this,
                "Save " + bookNumber + "(" + chapterNumber + " : " + verseNumber + ")",
                Toast.LENGTH_SHORT).show();
    }

    private void deleteButtonClicked() {
        Toast.makeText(ActivityBookmarkedVerse.this,
                "Delete " + bookNumber + "(" + chapterNumber + " : " + verseNumber + ")",
                Toast.LENGTH_SHORT).show();
    }

    private void disableNotesEditing() {
        compVerseNotesEdit.setEnabled(false);
//        compVerseNotesEdit.setVisibility(View.INVISIBLE);
//        compVerseNotesView.setVisibility(View.VISIBLE);
    }

    private void enableNotesEditing() {
        compVerseNotesEdit.setEnabled(true);
//        compVerseNotesEdit.setVisibility(View.VISIBLE);
//        compVerseNotesView.setVisibility(View.INVISIBLE);
    }
}
