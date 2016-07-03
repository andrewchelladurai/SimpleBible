/*
 *
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
 *
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHome
        extends Fragment
        implements View.OnClickListener {

    private static final String TAG = "SB_FragmentHome";

    public FragmentHome() {
        // FIXME: Make the layout better for landscape position
        // FIXME: make the Verse show text
    }

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        DatabaseUtility dbu = DatabaseUtility.getInstance(fragment.getContext());
        String todayVerseRef = dbu.getVerseReferenceForToday();

        args.putString(Utilities.TODAY_VERSE_REFERENCE, todayVerseRef);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.frag_home_but_goto);
        button.setOnClickListener(this);

        AppCompatTextView textView =
                (AppCompatTextView) view.findViewById(R.id.frag_home_daily_verse);
        displayVerse(textView);

        return view;
    }

    private void displayVerse(AppCompatTextView textView) {
        String verseRef = getArguments().getString(Utilities.TODAY_VERSE_REFERENCE);
        textView.setText(verseRef);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof AppCompatButton & v.getId() == R.id.frag_home_but_goto) {
            buttonGotoClicked();
        }
    }

    private String getBookName() {
        View view = getView();
        if (view == null) throw new AssertionError(TAG + " view == null");

        AppCompatAutoCompleteTextView input =
                (AppCompatAutoCompleteTextView) view.findViewById(R.id.frag_home_book_name);
        String bookName = input.getText().toString().trim();

        Log.d(TAG, "getBookName() returned: " + bookName);
        return bookName;
    }

    private String getChapterNumber() {
        View view = getView();
        if (view == null) throw new AssertionError(TAG + " view == null");

        AppCompatAutoCompleteTextView input =
                (AppCompatAutoCompleteTextView) view.findViewById(R.id.frag_home_chapter_number);
        String chapterNumber = input.getText().toString().trim();

        Log.d(TAG, "getChapterNumber() returned: " + chapterNumber);
        return chapterNumber;
    }

    private void buttonGotoClicked() {
    }
}
