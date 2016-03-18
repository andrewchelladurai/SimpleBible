/*
 * This file 'HomeFragment.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.v2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andrewchelladurai.simplebible.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_VERSE_ID = "ARG_VERSE_ID";

    private String verseID;
    private AppCompatAutoCompleteTextView bookTV;
    private AppCompatAutoCompleteTextView chapterTV;
    private AppCompatButton gotoButton;
    private AppCompatTextView dailyVerseTV;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String verseID) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VERSE_ID, verseID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }
        if (getArguments() != null) {
            verseID = getArguments().getString(ARG_VERSE_ID);
        }
        loadVerse();
    }

    private void loadVerse() {
        Toast.makeText(getActivity(), "Verse " + verseID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homev2, container, false);
        dailyVerseTV = (AppCompatTextView) view.findViewById(R.id.fragment_homev2_verse);
        gotoButton = (AppCompatButton) view.findViewById(R.id.goto_fragment_button);
        bookTV = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_book);
        chapterTV = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_chapter);

        gotoButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof AppCompatButton) {
            AppCompatButton button = (AppCompatButton) view;
            Toast.makeText(getActivity(), button.getText() + " clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
