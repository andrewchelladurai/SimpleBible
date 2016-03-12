/*
 * This file 'FragmentGotoLocation.java' is part of SimpleBible :
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.Arrays;

public class FragmentGotoLocation
        extends Fragment
        implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "FragmentGotoLocation";

    private AppCompatAutoCompleteTextView bookNameTxtView;
    private AppCompatAutoCompleteTextView chapterNameTxtView;
    private ArrayAdapter<String>          chapterNamesAdapter;
    private int bookNumber    = -1;
    private int chapterNumber = -1;
    private int chapterCount  = -1;

    public FragmentGotoLocation() {
        // Required empty public constructor
    }

    public static FragmentGotoLocation newInstance() {
        FragmentGotoLocation fragment = new FragmentGotoLocation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        View view = inflater.inflate(R.layout.fragment_goto_location, container, false);

        bookNameTxtView = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_goto_book_input);
        bookNameTxtView.setAdapter(new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, createBooksList()));
        bookNameTxtView.setOnFocusChangeListener(this);

        chapterNameTxtView = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_goto_chapter_input);
        chapterNamesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_list_item_1, new String[0]);
        chapterNameTxtView.setAdapter(chapterNamesAdapter);

        Button gotoButton = (Button) view.findViewById(R.id.fragment_goto_button);
        gotoButton.setOnClickListener(this);

        return view;
    }

    private String[] createBooksList() {
        String booknames[] = new String[66];
        int count = 0;
        for (AllBooks.Book book : AllBooks.getOTBooksList()) {
            booknames[count] = book.getName();
            count++;
        }
        for (AllBooks.Book book : AllBooks.getNTBooksList()) {
            booknames[count] = book.getName();
            count++;
        }
        return booknames;
    }

    @Override
    public void onClick(View view) {
        String buttonLabel = ((AppCompatButton) view).getText().toString();
        if (buttonLabel.equalsIgnoreCase(getString(R.string.fragment_goto_button_text))) ;
        {
            handleGotoButtonClick(view);
        }
    }

    private void handleGotoButtonClick(View view) {

        if (bookNumber < 1 || bookNumber > 66) {
            Snackbar.make(view, "Typed Book is Incorrect", Snackbar.LENGTH_SHORT);
            return;
        }

        try {
            chapterNumber = Integer.parseInt(chapterNameTxtView.getText().toString());
        } catch (NumberFormatException nfe) {
            chapterNumber = 0;
            Snackbar.make(view, "Chapter Number is Incorrect", Snackbar.LENGTH_SHORT);
            return;
        }

        if (chapterNumber < 1 || chapterNumber > chapterCount) {
            Snackbar.make(view, "Chapter Number is Incorrect", Snackbar.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(
                getActivity().getApplicationContext(), ActivityChapterVerses.class);
        intent.putExtra(ActivityChapterVerses.ARG_BOOK_NUMBER, bookNumber + "");
        intent.putExtra(ActivityChapterVerses.ARG_CHAPTER_NUMBER, chapterNumber + "");
        Log.d(TAG, "handleGotoButtonClick() called with : "
                   + bookNumber + " : " + chapterNumber + " of " + chapterCount);
        startActivity(intent);

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        AutoCompleteTextView actv = ((AutoCompleteTextView) view);
        chapterCount = -1;
        if (actv.equals(bookNameTxtView)) {
            chapterCount = validateBookName(actv);
        }
        if (chapterCount > 0) {
            String chapterNames[] = new String[chapterCount];
            for (int i = 0; i < chapterCount; i++) {
                chapterNames[i] = "" + (i + 1);
            }
            chapterNamesAdapter.clear();
            chapterNamesAdapter.addAll(chapterNames);
            chapterNamesAdapter.notifyDataSetChanged();
        } else {
            chapterCount = -1;
        }
    }

    private int validateBookName(AutoCompleteTextView actv) {
        String enteredText = actv.getText().toString();
        String books[] = createBooksList();

        Arrays.sort(books);

        if (Arrays.binarySearch(books, enteredText) < 0) { // Entered Text Not a Book
            resetValues();
            return -1;
        }

        books = createBooksList();
        bookNumber = -1;
        for (bookNumber = 0; bookNumber < books.length; bookNumber++) {
            if (books[bookNumber].equalsIgnoreCase(enteredText)) { break; }
        }
        bookNumber += 1;
        return AllBooks.getBook(bookNumber).getChapterCount();
    }

    private void resetValues() {
        bookNumber = -1;
        chapterCount = -1;
        chapterNumber = -1;
        bookNameTxtView.setText("");
        chapterNameTxtView.setText("");
    }

}
