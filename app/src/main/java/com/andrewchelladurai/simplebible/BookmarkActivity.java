/*
 *
 * This file 'BookmarkEntryActivity.java' is part of SimpleBible :
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.presentation.BookmarkActivityPresenter;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.DBUtility;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.util.ArrayList;

public class BookmarkActivity
        extends AppCompatActivity
        implements BookmarkActivityOperations, View.OnClickListener {

    private static final String TAG = "SB_BActivity";
    private BookmarkActivityPresenter mPresenter;
    private AppCompatEditText         mNote;
    private AppCompatButton           mButtonSave;
    private AppCompatButton           mButtonEdit;
    private AppCompatButton           mButtonDelete;
    private AppCompatButton           mButtonShare;
    private String                    mMode;
    private String                    mReference;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        init();
        if (mReference.isEmpty()) {
            mReference = Constants.DEFAULT_REFERENCE;
        }

        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_bookmark_toolbar);
        setSupportActionBar(toolbar);

        // Is the UP / Back in the ActionBar required?
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Placeholder for entries
        ListViewCompat mList = (ListViewCompat) findViewById(R.id.activity_bookmark_list);
        mList.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                            R.layout.content_activity_bookmark_reference_entry,
                                            prepareVersesList()));
        mNote = (AppCompatEditText) findViewById(R.id.activity_bookmark_note);

        mNote.setText(getSavedNote());

        mButtonSave = (AppCompatButton) findViewById(R.id.activity_bookmark_button_save);
        mButtonSave.setOnClickListener(this);
        mButtonEdit = (AppCompatButton) findViewById(R.id.activity_bookmark_button_edit);
        mButtonEdit.setOnClickListener(this);
        mButtonDelete = (AppCompatButton) findViewById(R.id.activity_bookmark_button_delete);
        mButtonDelete.setOnClickListener(this);
        mButtonShare = (AppCompatButton) findViewById(R.id.activity_bookmark_button_share);
        mButtonShare.setOnClickListener(this);

        showCorrectMode();
    }

    private void showCorrectMode() {
        if (mMode.equals(CREATE)) {
            mButtonSave.setVisibility(View.VISIBLE);
            mButtonEdit.setVisibility(View.GONE);
            mButtonDelete.setVisibility(View.GONE);
            mButtonShare.setVisibility(View.GONE);
            mNote.setEnabled(true);
        } else if (mMode.equals(VIEW)) {
            mButtonSave.setVisibility(View.GONE);
            mButtonEdit.setVisibility(View.VISIBLE);
            mButtonDelete.setVisibility(View.VISIBLE);
            mButtonShare.setVisibility(View.VISIBLE);
            mNote.setEnabled(false);
        }
    }

    private String getSavedNote() {
        if (mMode.equalsIgnoreCase(CREATE)) {
            return "";
        }
        return mPresenter.getSavedNote(mReference);
    }

    private ArrayList<String> prepareVersesList() {
        String references[] = mReference.split(Constants.DELIMITER_BETWEEN_REFERENCE);
        String parts[];
        int bookNumber, chapterNumber, verseNumber;
        String verseRefText, verseText;
        String template = getString(R.string.fragment_search_reference_template);
        ArrayList<String> list = new ArrayList<>();
        DBUtilityOperations dbu = DBUtility.getInstance();

        for (int i = 0; i < references.length; i++) {
            parts = Utilities.getReferenceParts(references[i]);
            if (null == parts) {
                continue;
            }
            bookNumber = Integer.parseInt(parts[0]);
            chapterNumber = Integer.parseInt(parts[1]);
            verseNumber = Integer.parseInt(parts[2]);

            verseRefText = Utilities.getFormattedReferenceText(
                    bookNumber, chapterNumber, verseNumber, template);
            verseText = dbu.getVerseForReference(bookNumber, chapterNumber, verseNumber);
            list.add(verseRefText + " " + verseText);
        }
        return list;
    }

    @Override public void onClick(View v) {
        if (v instanceof AppCompatButton) {
            AppCompatButton button = (AppCompatButton) v;
            if (button.equals(mButtonSave)) {
                buttonSaveClicked();
            } else if (button.equals(mButtonEdit)) {
                buttonEditClicked();
            } else if (button.equals(mButtonDelete)) {
                buttonDeleteClicked();
            } else if (button.equals(mButtonShare)) {
                buttonShareClicked();
            } else {
                Log.d(TAG, "onClick: " + getString(R.string.how_am_i_here));
            }
        }
    }

    private void buttonShareClicked() {
        String shareText = mPresenter.buttonShareClicked(mReference);
        if (shareText != null) {
            Intent intent = Utilities.shareVerse(shareText);
            startActivity(intent);
        }
    }

    private void buttonDeleteClicked() {
        boolean isDeleted = mPresenter.buttonDeleteClicked(mReference);
        if (isDeleted) {
            Toast.makeText(this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void buttonEditClicked() {
        boolean status = mPresenter.buttonEditClicked();
        mMode = CREATE;
        showCorrectMode();
    }

    private void buttonSaveClicked() {
        String message;
        if (mPresenter.buttonSaveClicked()) {
            mMode = VIEW;
            showCorrectMode();
            message = getString(R.string.created);
        } else {
            message = getString(R.string.failed);
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override public void init() {
        if (null == mPresenter) {
            mPresenter = new BookmarkActivityPresenter(this);
            Bundle arguments = getIntent().getExtras();
            mReference = arguments.getString(ARG_REFERENCE, "");
            mMode = arguments.getString(ARG_MODE, "");
            Log.d(TAG, "init: variables initialized");
        }
        Log.d(TAG, "onCreate() mReference = [" + mReference + "], mMode = [" + mMode + "]");
    }

    @Override public void refresh() {
        Log.d(TAG, "refresh() called");
    }

    @Override public String getPassedReference() {
        return mReference;
    }

    @Override public String getInputNote() {
        return mNote.getText().toString().trim();
    }

    @Override public String getVerseTemplate() {
        return getString(R.string.share_verse_template);
    }

    @Override public String getShareBookmarkTemplate() {
        return getString(R.string.share_bookmark_template);
    }
}
