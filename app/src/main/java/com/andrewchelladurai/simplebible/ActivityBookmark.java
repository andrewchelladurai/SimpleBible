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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityBookmark
        extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "SB_ActivityBookmark";
    private ArrayList<String> mReferences;
    private String            mViewNode;
    private AppCompatEditText mNotes;
    private ButtonBarLayout   actionBar;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_bookmark_toolbar);
        setSupportActionBar(toolbar);
        actionBar = (ButtonBarLayout) findViewById(R.id.activity_bookmark_button_bar);

        mReferences = getIntent().getExtras().getStringArrayList(Utilities.REFERENCES);
        if (mReferences == null) {
            Utilities.throwError(TAG, TAG + " mReferences == null");
        }
        mNotes = (AppCompatEditText) findViewById(R.id.activity_bookmark_notes);
        populateContent();
        prepareScreen();
        Utilities.log(TAG, "onCreate: mReferences.size [" + mReferences.size() +
                           "] mode [" + mViewNode + "]");
    }

    private void populateContent() {
        Utilities.log(TAG, "populateContent() called");
        ListViewCompat verseList = (ListViewCompat) findViewById(R.id.activity_bookmark_list);
        if (verseList == null) {
            Utilities.throwError(TAG, TAG + " verseList == null");
        }

        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        String[] parts;
        ArrayList<String> verses = new ArrayList<>(mReferences.size());
        for (String reference : mReferences) {
            parts = reference.split(Utilities.DELIMITER_IN_REFERENCE);
            verses.add(Utilities.getFormattedBookmarkVerse(parts[0], parts[1], parts[2],
                                                           dbu.getSpecificVerse(
                                                                   Integer.parseInt(parts[0]),
                                                                   Integer.parseInt(parts[1]),
                                                                   Integer.parseInt(parts[2]))));
        }
        verseList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, verses));
        populateReference();
    }

    private void prepareScreen() {
        switch (mViewNode) {
            case Utilities.BOOKMARK_SAVE:
                bindButton(R.id.activity_bookmark_but_save, View.VISIBLE);
                bindButton(R.id.activity_bookmark_but_edit, View.GONE);
                bindButton(R.id.activity_bookmark_but_delete, View.GONE);
                bindButton(R.id.activity_bookmark_but_share, View.GONE);
                mNotes.setEnabled(true);
                break;
            case Utilities.BOOKMARK_EDIT:
                bindButton(R.id.activity_bookmark_but_save, View.GONE);
                bindButton(R.id.activity_bookmark_but_edit, View.VISIBLE);
                bindButton(R.id.activity_bookmark_but_delete, View.VISIBLE);
                bindButton(R.id.activity_bookmark_but_share, View.VISIBLE);
                mNotes.setEnabled(false);
                break;
            default:
                Utilities.log(TAG, "prepareScreen: " + getString(R.string.how_am_i_here));
        }
    }

    private void populateReference() {
        Utilities.log(TAG, "populateReference() called");
        StringBuilder reference = getConvertedReference();

        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        if (dbu.isAlreadyBookmarked(reference.toString())) {
            mViewNode = Utilities.BOOKMARK_EDIT;
            Utilities.log(TAG, "populateReference: ViewNode = EDIT");
            String note = dbu.getBookmarkedEntry(reference.toString());
            mNotes.setHint(note.isEmpty() ? getString(R.string.activity_bookmark_empty_note)
                                          : getString(
                                                  R.string.activity_bookmark_reference_present));
            mNotes.setText(note);
        } else {
            mViewNode = Utilities.BOOKMARK_SAVE;
            Utilities.log(TAG, "populateReference: ViewNode = SAVE");
            mNotes.setHint(getString(R.string.activity_bookmark_reference_absent));
        }
    }

    private void bindButton(int buttonId, int visibilityMode) {
        AppCompatButton button = (AppCompatButton) findViewById(buttonId);
        if (button == null) {
            Utilities.throwError(TAG, TAG + " button == null : " + buttonId);
        }
        button.setOnClickListener(this);
        button.setVisibility(visibilityMode);
    }

    private StringBuilder getConvertedReference() {
        StringBuilder reference = new StringBuilder();
        String delimiter = Utilities.DELIMITER_BETWEEN_REFERENCE;
        for (String entry : mReferences) {
            reference.append(entry).append(delimiter);
        }
        // remove the last appended DELIMITER_BETWEEN_REFERENCE
        reference.delete(reference.length() - delimiter.length(), reference.length());
        return reference;
    }

    @Override
    public void onClick(View view) {
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
                    Utilities.throwError(TAG, TAG + getString(R.string.how_am_i_here));
            }
        }
    }

    private void buttonSaveClicked() {
        Utilities.log(TAG, "buttonSaveClicked() called");
        StringBuilder reference = getConvertedReference();
        String notes = mNotes.getText().toString().trim();

        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        boolean success;
        int feedbackTextId = 0;
        switch (mViewNode) {
            case Utilities.BOOKMARK_SAVE:
                feedbackTextId = R.string.activity_bookmark_toast_save_success;
                success = dbu.createNewBookmark(reference.toString(), notes);
                if (!success) {
                    feedbackTextId = R.string.activity_bookmark_toast_save_failure;
                }
                break;
            case Utilities.BOOKMARK_EDIT:
                feedbackTextId = R.string.activity_bookmark_toast_update_success;
                success = dbu.updateExistingBookmark(reference.toString(), notes);
                if (!success) {
                    feedbackTextId = R.string.activity_bookmark_toast_update_failure;
                }
        }
        actionBar.setVisibility(View.INVISIBLE);
        mNotes.setFocusable(false);
        Utilities.hideKeyboard(this);

        Snackbar.make(mNotes, getString(feedbackTextId), Snackbar.LENGTH_LONG).show();
    }

    private void buttonEditClicked() {
        Utilities.log(TAG, "buttonEditClicked() called");
        // Hack to make the screen show Save button but update the verse
        mViewNode = Utilities.BOOKMARK_SAVE;
        prepareScreen();
        mViewNode = Utilities.BOOKMARK_EDIT;
    }

    private void buttonDeleteClicked() {
        Utilities.log(TAG, "buttonDeleteClicked() called");

        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBookmark.this,
                                                                    R.style.SBTheme_AlertDialog);
        builder.setTitle(R.string.activity_bookmark_alert_delete_title);
        builder.setMessage(R.string.activity_bookmark_alert_delete_message);

        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        builder.setPositiveButton(
                R.string.activity_bookmark_alert_delete_positive_text,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface pDialogInterface, int pI) {
                        StringBuilder reference = getConvertedReference();
                        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
                        if (dbu.deleteBookmark(reference.toString())) {
                            Toast.makeText(ActivityBookmark.this,
                                           R.string.activity_bookmark_toast_delete_success,
                                           Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ActivityBookmark.this,
                                           R.string.activity_bookmark_toast_delete_failure,
                                           Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.setNegativeButton(R.string.activity_bookmark_alert_delete_negative_text,
                                  new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(
                                              DialogInterface pDialogInterface, int pI) {
                                          dialog.dismiss();
                                      }
                                  });
        builder.create();
        builder.show();
    }

    private void buttonShareClicked() {
        Utilities.log(TAG, "buttonShareClicked called");
        ListViewCompat verseList = (ListViewCompat) findViewById(R.id.activity_bookmark_list);
        if (verseList == null) {
            Log.d(TAG, "buttonShareClicked returning coz verseList == null");
            return;
        }
        StringBuilder shareText = new StringBuilder();
        ListAdapter adapter = verseList.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            shareText.append(adapter.getItem(i)).append('\n');
        }

        if (mNotes.getText().toString().isEmpty()) {
            shareText.append(getString(R.string.activity_bookmark_empty_note)).append('\n');
        } else {
            shareText.append(getString(R.string.activity_bookmark_share_note_below_text))
                     .append('\n')
                     .append(mNotes.getText().toString().trim()).append('\n');
        }
        shareText.append(getString(R.string.share_append_text));
        startActivity(Utilities.shareVerse(shareText.toString()));
    }

}
