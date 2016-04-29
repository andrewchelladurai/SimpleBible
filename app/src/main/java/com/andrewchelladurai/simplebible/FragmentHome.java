/*
 * This file 'FragmentHome.java' is part of SimpleBible :
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class FragmentHome
        extends Fragment
        implements View.OnClickListener,
                   AdapterView.OnItemClickListener {

    public static final String DAILY_VERSE_ID = "DAILY_VERSE_ID";
    private static final String TAG = "FragmentHome";
    private String mDailyVerseId = null;
    private AppCompatAutoCompleteTextView mBookInput;
    private AppCompatAutoCompleteTextView mChapterInput;
    private int mBookNumber = 0;
    private int mChapterNumber = 0;

    public FragmentHome() {
    }

    public static FragmentHome newInstance(String pdDailyVerseId) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(DAILY_VERSE_ID, pdDailyVerseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle pSavedState) {
        super.onCreate(pSavedState);
        if (getArguments() != null) {
            mDailyVerseId = getArguments().getString(DAILY_VERSE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mBookInput = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_home_book);
        mBookInput.setAdapter(new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line,
                Book.getAllBookNames()));
        mBookInput.setOnItemClickListener(this);

        mChapterInput = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_home_chapter);
        mChapterInput.setAdapter(new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line, new String[]{""}));

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.fragment_home_button);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetValues();
    }

    public String getDailyVerseId() {
        return mDailyVerseId;
    }

    public void setDailyVerseId(final String pDailyVerseId) {
        mDailyVerseId = pDailyVerseId;
    }

    @Override
    public void onClick(final View pView) {
        switch (pView.getId()) {
            case R.id.fragment_home_button:
                handleGotoClicked();
        }
    }

    private void handleGotoClicked() {
        Log.i(TAG, "handleGotoClicked: ");
        mBookNumber = validateBook();
        mChapterNumber = validateChapter();
        boolean validated = ((mBookNumber > 0) & (mChapterNumber > 0));
        if (validated) {
            Intent intent = new Intent(getContext(), ActivityChapter.class);
            intent.putExtra(ActivityChapter.ARG_BOOK_NUMBER, mBookNumber + "");
            intent.putExtra(ActivityChapter.ARG_CHAPTER_NUMBER, mChapterNumber + "");
            startActivity(intent);
            resetValues();
        }
    }

    private int validateBook() {
        String input = mBookInput.getText() + "".trim();
        if (input.isEmpty()) {
            Log.d(TAG, "validateBook: Empty Book Name");
            mBookInput.setError("Empty Book Name");
            return 0;
        }
        mChapterInput.setAdapter(null);
        mBookNumber = Book.getBookDetails(input);
        if (mBookNumber > 0 & mBookNumber < 67) {
            String[] values;
            int count = Integer.parseInt(Book.getBookDetails(mBookNumber).getChapterCount());
            values = new String[count];
            for (int i = 0; i < count; i++) {
                values[i] = (i + 1) + "";
            }
            mChapterInput.setAdapter(new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_dropdown_item_1line, values));
            String hint = getString(R.string.label_input_chapter_count) + " " + count;
            mChapterInput.setHint(hint);
            mChapterInput.requestFocus();
        } else {
            Log.d(TAG, "validateBook: Invalid BookNumber " + mBookNumber);
            mBookInput.setError("Invalid Book");
            mBookInput.requestFocus();
        }
        return mBookNumber;
    }

    private int validateChapter() {
        String input = mChapterInput.getText() + "".trim();
        if (input.isEmpty()) {
            return 1;
        }
        try {
            mChapterNumber = Integer.parseInt(input);
        } catch (NumberFormatException pE) {
            mChapterNumber = 0;
        }
        int maxCount = Integer.parseInt(Book.getBookDetails(mBookNumber).getChapterCount());
        if (mChapterNumber > 0 & mChapterNumber <= maxCount) {
            return mChapterNumber;
        } else {
            Log.d(TAG, "validateChapter: Invalid Chapter " + mChapterNumber);
            mChapterInput.setError("Invalid Chapter");
            mChapterInput.requestFocus();
            return 0;
        }
    }

    private void resetValues() {
        mBookNumber = mChapterNumber = 0;
        mBookInput.setText("");
        mChapterInput.setText("");
        mChapterInput.setHint("");
        mBookInput.requestFocus();
    }

    @Override
    public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
        validateBook();
    }
}
