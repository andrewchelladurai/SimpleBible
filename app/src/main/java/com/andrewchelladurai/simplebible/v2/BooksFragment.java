/*
 * This file 'BooksFragment.java' is part of SimpleBible :
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
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewchelladurai.simplebible.AllBooks;
import com.andrewchelladurai.simplebible.R;

public class BooksFragment
        extends Fragment
        implements AdapterView.OnItemClickListener, View.OnClickListener {

    private AppCompatAutoCompleteTextView book;
    private AppCompatAutoCompleteTextView chapter;

    public BooksFragment() {
        // Required empty public constructor
    }

    public static BooksFragment newInstance() {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        super.onCreateView(inflater, container, savedState);

        View view = inflater.inflate(R.layout.fragment_booksv2, container, false);

        ListViewCompat listView = (ListViewCompat) view.findViewById(R.id.fragment_books_list);
        ArrayAdapter<String> bookListAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, AllBooks.getAllBooks());
        listView.setOnItemClickListener(this);
        listView.setAdapter(bookListAdapter);

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.goto_fragment_button);
        button.setOnClickListener(this);

        book = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_book);
        chapter = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_chapter);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String value = ((TextView) view).getText().toString();
        Toast.makeText(getActivity(), "Book " + i + " : " + value, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view instanceof AppCompatButton) {
            String value = book.getText() + " : " + chapter.getText();
            Toast.makeText(getActivity(), "Loading " + value, Toast.LENGTH_SHORT).show();
        }
    }

}
