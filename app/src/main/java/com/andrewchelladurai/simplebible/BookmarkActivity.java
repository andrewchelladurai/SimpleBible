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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.presentation.BookmarkActivityPresenter;

import java.util.ArrayList;

public class BookmarkActivity
        extends AppCompatActivity
        implements BookmarkActivityOperations, View.OnClickListener {

    // FIXME: 9/17/16 Rotating the Screen shows all button again.

    private static final String TAG = "SB_BActivity";
    private AppCompatTextView         mLabelReference;
    private AppCompatTextView         mLabelNote;
    private ListViewCompat            mList;
    private AppCompatEditText         mNote;
    private BookmarkActivityPresenter mPresenter;
    private AppCompatButton           mButtonSave;
    private AppCompatButton           mButtonEdit;
    private AppCompatButton           mButtonDelete;
    private AppCompatButton           mButtonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_bookmark_toolbar);
        setSupportActionBar(toolbar);

        // Is the UP / Back in the ActionBar required?
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Placeholder for entries
        ArrayList<String> items = new ArrayList<>(7);
        for (int i = 1; i <= 1; i++) {
            items.add("Entry " + i);
        }

        mLabelReference = (AppCompatTextView) findViewById(R.id.activity_bookmark_label_reference);

        mList = (ListViewCompat) findViewById(R.id.activity_bookmark_list);
        mList.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                                            R.layout.content_activity_bookmark_reference_entry,
                                            items));
        mLabelNote = (AppCompatTextView) findViewById(R.id.activity_bookmark_label_note);
        mNote = (AppCompatEditText) findViewById(R.id.activity_bookmark_note);

        mButtonSave = (AppCompatButton) findViewById(R.id.activity_bookmark_button_save);
        mButtonSave.setOnClickListener(this);
        mButtonEdit = (AppCompatButton) findViewById(R.id.activity_bookmark_button_edit);
        mButtonEdit.setOnClickListener(this);
        mButtonDelete = (AppCompatButton) findViewById(R.id.activity_bookmark_button_delete);
        mButtonDelete.setOnClickListener(this);
        mButtonShare = (AppCompatButton) findViewById(R.id.activity_bookmark_button_share);
        mButtonShare.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        if (v instanceof AppCompatButton) {
            AppCompatButton button = (AppCompatButton) v;
            if (button.equals(mButtonSave)) {
                boolean status = mPresenter.buttonSaveClicked();
                if (status) {
                    mButtonSave.setVisibility(View.GONE);
                    mButtonEdit.setVisibility(View.VISIBLE);
                    mButtonDelete.setVisibility(View.VISIBLE);
                    mButtonShare.setVisibility(View.VISIBLE);
                    mNote.setFocusable(false);
                }
            } else if (button.equals(mButtonEdit)) {
                boolean status = mPresenter.buttonEditClicked();
                if (status) {
                    mButtonSave.setVisibility(View.VISIBLE);
                    mButtonEdit.setVisibility(View.GONE);
                    mButtonDelete.setVisibility(View.GONE);
                    mButtonShare.setVisibility(View.GONE);
                    mNote.setFocusable(true);
                }
            } else if (button.equals(mButtonDelete)) {
                mPresenter.buttonDeleteClicked();
            } else if (button.equals(mButtonShare)) {
                mPresenter.buttonShareClicked();
            } else {
                Log.d(TAG, "onClick: " + getString(R.string.how_am_i_here));
            }
        }
    }

    @Override public void init() {
        if (null == mPresenter) {
            mPresenter = new BookmarkActivityPresenter(this);
        }
    }

    @Override public void refresh() {
        Log.d(TAG, "refresh() called");
    }
}
