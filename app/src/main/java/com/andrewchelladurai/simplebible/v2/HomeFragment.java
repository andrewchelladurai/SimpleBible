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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.andrewchelladurai.simplebible.AllBooks;
import com.andrewchelladurai.simplebible.R;

public class HomeFragment
        extends Fragment {

    private static final String ARG_VERSE_ID = "ARG_VERSE_ID";

    private String verseID;
    private AppCompatAutoCompleteTextView bookTV;
    private AppCompatAutoCompleteTextView chapterTV;
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

        AppCompatButton gotoButton = (AppCompatButton) view.findViewById(R.id.goto_fragment_button);
        gotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleGotoButtonClick();
            }
        });

        bookTV = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_book);
        bookTV.setAdapter(new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, AllBooks.getAllBooks()));
        bookTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int j, long k) {
                int position = BookNameContent.getBookPosition(
                        bookTV.getText().toString().trim());
                int chapterCount = BookNameContent.getBookChapterCount(position);

                String items[] = new String[chapterCount];
                for (int i = 0; i < chapterCount; i++) {
                    items[i] = "" + (i + 1);
                }

                chapterTV.setAdapter(new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_list_item_1, items));
                chapterTV.requestFocus();
            }
        });

        chapterTV = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_chapter);

        return view;
    }

    private void handleGotoButtonClick() {
        String input = bookTV.getText().toString().trim();
        if (input.isEmpty()) {
            Snackbar.make(bookTV, "Enter Book Name", Snackbar.LENGTH_SHORT).show();
            bookTV.requestFocus();
            return;
        }

        AllBooks.Book book = AllBooks.getBook(input);
        if (book == null) {
            bookTV.requestFocus();
            Snackbar.make(bookTV, "Book Name Incorrect", Snackbar.LENGTH_SHORT).show();
            return;
        }

        input = chapterTV.getText().toString().trim();
        int chapterCount;
        try {
            chapterCount = (input.isEmpty()) ? 1 : Integer.valueOf(input);
        } catch (NumberFormatException e) {
            chapterCount = 0;
        }
        if (chapterCount < 1 || chapterCount > book.getChapterCount()) {
            Snackbar.make(chapterTV, "Chapter number is Incorrect", Snackbar.LENGTH_SHORT).show();
            chapterTV.requestFocus();
            return;
        }

        Intent intent = new Intent(getContext(), ActivityChapterVerses.class);
        intent.putExtra(ActivityChapterVerses.ARG_BOOK_NUMBER, book.getBookNumber());
        intent.putExtra(ActivityChapterVerses.ARG_CHAPTER_NUMBER, input);
        resetValues();
        startActivity(intent);
    }

    private void resetValues() {
        bookTV.setText("");
        chapterTV.setText("");

    }
}
