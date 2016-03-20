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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.ActivityChapterVerses;
import com.andrewchelladurai.simplebible.AllBooks;
import com.andrewchelladurai.simplebible.R;

public class BooksFragment
        extends Fragment
        implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String TAG = "BooksFragment";
    private AppCompatAutoCompleteTextView book;
    private AppCompatAutoCompleteTextView chapter;
    private AllBooks.Book gotoBook = null;

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

        book = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_book);
        book.setAdapter(new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, AllBooks.getAllBooks()));
        book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (loadChapters(book.getText().toString().trim())) {
                    chapter.requestFocus();
                } else {
                    Snackbar.make(book, "Incorrect Book Name", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        chapter = (AppCompatAutoCompleteTextView) view.findViewById(R.id.goto_fragment_chapter);
        chapter.setAdapter(new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1, new String[]{""}));

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.goto_fragment_button);
        button.setOnClickListener(this);

        ListViewCompat listView = (ListViewCompat) view.findViewById(R.id.fragment_books_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, AllBooks.getAllBooks()));
        return view;
    }

    private boolean loadChapters(String label) {
        Log.d(TAG, "loadChapters() called with label = [" + label + "]");
        gotoBook = AllBooks.getBook(label);

        if (gotoBook == null) {
            return false;
        }

        int chapterCount = gotoBook.getChapterCount();
        String newItems[] = new String[chapterCount];

        for (int i = 0; i < chapterCount; i++) {
            newItems[i] = (i + 1) + "";
        }
        chapter.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, newItems));
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String value = ((TextView) view).getText().toString();

/*
        Intent intent = new Intent(this, ActivityChapterVerses.class);
        intent.putExtra(ActivityChapterVerses.ARG_BOOK_NUMBER, item.getBookNumber());
        intent.putExtra(ActivityChapterVerses.ARG_CHAPTER_NUMBER, 1 + "");
        startActivity(intent);
*/
    }

    @Override
    public void onClick(View view) {
        if (view instanceof AppCompatButton) {
            handleGotoButtonClick();
        }
    }

    private void handleGotoButtonClick() {
        int chapterNumber = (chapter.getText().toString().trim().isEmpty())
                ? 1 : Integer.parseInt(chapter.getText().toString().trim());

        if (gotoBook == null) {
            Snackbar.make(book, "Incorrect Book", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (chapterNumber < 1 || chapterNumber > gotoBook.getChapterCount()) {
            Snackbar.make(book, "Incorrect Chapter", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getContext(), ActivityChapterVerses.class);
        intent.putExtra(ActivityChapterVerses.ARG_BOOK_NUMBER, gotoBook.getBookNumber());
        intent.putExtra(ActivityChapterVerses.ARG_CHAPTER_NUMBER, chapterNumber + "");

        book.setText("");
        chapter.setText("");

        startActivity(intent);
    }

}
