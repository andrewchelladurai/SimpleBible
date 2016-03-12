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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentGotoLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGotoLocation
        extends Fragment {

    private static final String TAG = "FragmentGotoLocation";

    private InteractionListener           mListener;
    private AppCompatAutoCompleteTextView bookNameTxtView;
    private AppCompatAutoCompleteTextView chapterNameTxtView;
    private BookNameValidator             bookNameValidator;
    private ArrayAdapter<String>          chapterNamesAdapter;

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
        chapterNameTxtView = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_goto_chapter_input);
        Button gotoButton = (Button) view.findViewById(R.id.fragment_goto_button);

        bookNameTxtView.setAdapter(new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, createBooksList()));

        chapterNamesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout
                .simple_list_item_1, new String[0]);
        chapterNameTxtView.setAdapter(chapterNamesAdapter);

        bookNameValidator = new BookNameValidator();

//        bookNameTxtView.setValidator(bookNameValidator);
        bookNameTxtView.setOnFocusChangeListener(bookNameValidator);

        return view;
    }

    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onGotoFragmentInteraction(view);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public interface InteractionListener {

        void onGotoFragmentInteraction(View view);
    }

    class BookNameValidator
            implements View.OnFocusChangeListener {

        int bookNumber    = -1;
        int chapterNumber = -1;
        int chapterCount  = -1;

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
            return AllBooks.getBook(bookNumber + 1).getChapterCount();
        }

        private void resetValues() {
            bookNumber = -1;
            chapterCount = -1;
            chapterNumber = -1;
            bookNameTxtView.setText("");
            chapterNameTxtView.setText("");
        }
    }
}
