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

package com.andrewchelladurai.simplebible;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class FragmentHome
        extends Fragment {

    private static final String TAG = "FragmentHome";
    private static final String ARG_VERSE_ID = "ARG_VERSE_ID";
    private String verseID;
    private AppCompatAutoCompleteTextView bookInput;
    private AppCompatAutoCompleteTextView chapterInput;
    private AppCompatTextView dailyVerseLabel;

    public FragmentHome() {
        // Required empty public constructor
    }

    public static FragmentHome newInstance(String verseID) {
        FragmentHome fragment = new FragmentHome();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dailyVerseLabel = (AppCompatTextView) view.findViewById(R.id.fragment_home_daily_verse);

        AppCompatButton gotoButton = (AppCompatButton) view.findViewById(R.id.goto_fragment_button);
        gotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleGotoButtonClick();
            }
        });

        bookInput = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_book);
        bookInput.setAdapter(new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, BookNameContent.getAllBookLabels()));
        bookInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int j, long k) {
                loadChaptersForBook(BookNameContent.getBookPosition(
                        bookInput.getText().toString().trim()));
            }
        });

        chapterInput = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_chapter);

        return view;
    }

    private void loadChaptersForBook(int position) {
        BookNameContent.BookItem item = BookNameContent.getBookItem(position);

        if (item == null) {
            Log.e(TAG, "loadChaptersForBook :", new RuntimeException(
                    "No Book found at Position " + position));
            return;
        }

        int chapterCount = item.getChapterCount();
        if (chapterCount < 1) {
            Log.e(TAG, "loadChaptersForBook :", new RuntimeException(
                    "Zero Chapters for Book at Position " + position));
            return;
        }

        String items[] = new String[chapterCount];
        for (int i = 0; i < chapterCount; i++) {
            items[i] = "" + (i + 1);
        }

        chapterInput.setAdapter(new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, items));
        chapterInput.requestFocus();
    }

    private void handleGotoButtonClick() {
        String input = bookInput.getText().toString().trim();
        if (input.isEmpty()) {
            Snackbar.make(bookInput, "Enter Book Name", Snackbar.LENGTH_SHORT).show();
            bookInput.requestFocus();
            return;
        }

//        AllBooks.Book book = AllBooks.getBook(input);
        BookNameContent.BookItem book = BookNameContent.getBookItem(input);
        if (book == null) {
            bookInput.requestFocus();
            Snackbar.make(bookInput, "Book Name Incorrect", Snackbar.LENGTH_SHORT).show();
            return;
        }

        input = chapterInput.getText().toString().trim();
        int chapterCount;
        try {
            chapterCount = (input.isEmpty()) ? 1 : Integer.valueOf(input);
        } catch (NumberFormatException e) {
            chapterCount = 0;
        }
        if (chapterCount < 1 || chapterCount > book.getChapterCount()) {
            Snackbar.make(chapterInput, "Chapter number is Incorrect", Snackbar.LENGTH_SHORT).show();
            chapterInput.requestFocus();
            return;
        }

        Intent intent = new Intent(getContext(), ActivityChapterView.class);
        intent.putExtra(ActivityChapterView.ARG_BOOK_NUMBER, book.getBookNumber() + "");
        intent.putExtra(ActivityChapterView.ARG_CHAPTER_NUMBER, chapterCount + "");
        startActivity(intent);
        resetValues();

    }

    private void resetValues() {
        bookInput.setText("");
        chapterInput.setText("");
        chapterInput.setAdapter(null);
    }
}
