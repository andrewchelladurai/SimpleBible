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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ActivityBookmark
        extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "SB_ActivityBookmark";
    private ArrayList<String> mReferences;
    private String            mViewNode;
    private AppCompatEditText mNotes;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_bookmark_toolbar);
        setSupportActionBar(toolbar);
        mReferences = getIntent().getExtras().getStringArrayList(Utilities.REFERENCES);
        if (mReferences == null) {
            Utilities.throwError(TAG + " mReferences == null");
        }
        mViewNode = getIntent().getExtras().getString(
                Utilities.BOOKMARK_MODE, Utilities.BOOKMARK_VIEW);

        // // FIXME: 24/7/16 OVER RIDING FOR TESTING
        mViewNode = Utilities.BOOKMARK_EDIT;

        Log.d(TAG,
              "onCreate: mReferences.size [" + mReferences.size() + "] mode [" + mViewNode + "]");

        mNotes = (AppCompatEditText) findViewById(R.id.activity_bookmark_notes);
        populateContent();
        prepareScreen();
    }

    private void populateContent() {
        Log.d(TAG, "populateContent() : Populate references in list");
        ListViewCompat verseList = (ListViewCompat)
                findViewById(R.id.activity_bookmark_list);
        if (verseList == null) {
            Utilities.throwError(TAG + " verseList == null");
        }

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

        if (mReferences.size() == 1) {
            populateSingleReference();
        } else {
            populateMultipleReference();
        }

    }

    private void prepareScreen() {
        switch (mViewNode) {
            case Utilities.BOOKMARK_EDIT:
                bindButton(R.id.activity_bookmark_but_save, View.VISIBLE);
                bindButton(R.id.activity_bookmark_but_edit, View.GONE);
                bindButton(R.id.activity_bookmark_but_delete, View.GONE);
                bindButton(R.id.activity_bookmark_but_share, View.GONE);
                mNotes.setEnabled(true);
                break;
            case Utilities.BOOKMARK_VIEW:
                bindButton(R.id.activity_bookmark_but_save, View.GONE);
                bindButton(R.id.activity_bookmark_but_edit, View.VISIBLE);
                bindButton(R.id.activity_bookmark_but_delete, View.VISIBLE);
                bindButton(R.id.activity_bookmark_but_share, View.VISIBLE);
                mNotes.setEnabled(false);
                break;
            default:
                Log.d(TAG, "prepareScreen: " + getString(R.string.how_am_i_here));
        }
    }

    private void populateSingleReference() {
        Log.d(TAG, "populateSingleReference() called");
        String reference = mReferences.get(0);
        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        switch (Integer.parseInt(dbu.isReferencePresent(reference))) {
            case 0:// NO reference found
                mNotes.setHint(getString(R.string.activity_bookmark_reference_absent));
                break;
            default:// Reference found
                // FIXME: 4/8/16 handle when the reference is alredy present
                mNotes.setHint(getString(R.string.activity_bookmark_reference_present));
        }
    }

    private void populateMultipleReference() {
        Log.d(TAG, "populateMultipleReference() called");
    }

    private void bindButton(int buttonId, int visibilityMode) {
        AppCompatButton button = (AppCompatButton) findViewById(buttonId);
        if (button == null) {
            Utilities.throwError(TAG + " button == null : " + buttonId);
        }
        button.setOnClickListener(this);
        button.setVisibility(visibilityMode);
    }

    @Override public void onClick(View view) {
        if (view instanceof AppCompatButton) {
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
                case R.id.activity_bookmark_but_share:
                    buttonShareClicked();
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
        mViewNode = Utilities.BOOKMARK_EDIT;
        prepareScreen();
    }

    private void buttonDeleteClicked() {
        Log.d(TAG, "buttonDeleteClicked() called");
    }

    private void buttonShareClicked() {
        Log.d(TAG, "buttonShareClicked called");
    }
}
