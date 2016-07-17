/*
 *
 * This file 'ActivityBookmark.java' is part of SimpleBible :
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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class ActivityBookmark
        extends AppCompatActivity {

    private static final String TAG = "SB_ActivityBookmark";
    private ArrayList<String> mReferences;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_bookmark_toolbar);
        setSupportActionBar(toolbar);
        mReferences = getIntent().getExtras().getStringArrayList(Utilities.REFERENCES);
    }

/*    private void updateTopPanel() {
        AppCompatTextView referenceView = (AppCompatTextView)
                findViewById(R.id.activity_bookmark_reference);
        if (referenceView == null) {
            Utilities.throwError(TAG + " referenceView == null");
        }
        ListViewCompat verseList = (ListViewCompat)
                findViewById(R.id.activity_bookmark_verse_list);
        if (verseList == null) {
            Utilities.throwError(TAG + " verseList == null");
        }

        String value = mReferences.size() + " " +
                       getString(R.string.bookmark_multiple_references);
        referenceView.setText(value);

        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        String[] parts;
        ArrayList<String> verses = new ArrayList<>(mReferences.size());
        for (String reference : mReferences) {
            parts = reference.split(":");
            verses.add(Utilities.getFormattedBookmarkVerse(
                    parts[0], parts[1], parts[2],
                    dbu.getSpecificVerse(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]))));
        }
        verseList.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, verses));
    }

    private void updateBottomPanel(boolean isEditMode) {
        mNotesInput = (AppCompatEditText) findViewById(R.id.activity_bookmark_notes);
        if (isEditMode){
            bindButton(R.id.activity_bookmark_but_save, true);
        }
        bindButton(R.id.activity_bookmark_but_edit, );
        bindButton(R.id.activity_bookmark_but_delete, isEditMode);
        bindButton(R.id.activity_bookmark_but_cancel, isEditMode);
    }

    private void bindButton(int buttonId, boolean invisible) {
        AppCompatImageButton button = (AppCompatImageButton) findViewById(buttonId);
        if (button == null) {
            Utilities.throwError(TAG + " button == null : " + buttonId);
        }
        if (invisible) {
            button.setVisibility(View.GONE);
        }
        button.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
        if (view instanceof AppCompatImageButton) {
            switch (view.getId()) {
                case R.id.activity_bookmark_but_save:
                    buttonSaveClicked();
                    break;
                case R.id.activity_bookmark_but_edit:
                    buttonEditClicked();
                    break;
                case R.id.activity_bookmark_but_delete:
                    buttonDeleteClicked();
                    break;
                case R.id.activity_bookmark_but_cancel:
                    buttonCancelClicked();
                    break;
                default:
                    Utilities.throwError(TAG + getString(R.string.how_am_i_here));
            }
        }
    }

    private void buttonSaveClicked() {
        Log.d(TAG, "buttonSaveClicked() called");
    }

    private void buttonEditClicked() {
        Log.d(TAG, "buttonEditClicked() called");
    }

    private void buttonDeleteClicked() {
        Log.d(TAG, "buttonDeleteClicked() called");
    }

    private void buttonCancelClicked() {
        Log.d(TAG, "buttonCancelClicked() called");
    }*/
}
