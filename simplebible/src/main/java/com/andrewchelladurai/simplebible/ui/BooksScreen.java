/*
 *
 * This file 'BooksScreen.java' is part of SimpleBible :
 *
 * Copyright (c) 2018.
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

package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.ops.BooksScreenOps;
import com.andrewchelladurai.simplebible.repository.BookRepository;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class BooksScreen
    extends Fragment
    implements BooksScreenOps {

    private static final String TAG = "BooksScreen";

    private BooksRecyclerViewAdapter mAdapter;

    public BooksScreen() {
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.books_screen, container, false);

        BookRepository repository = ViewModelProviders.of(this).get(BookRepository.class);
        repository.getAllBooks().observe(this, new Observer<List<Book>>() {

            @Override
            public void onChanged(final List<Book> books) {
                if (books == null || books.isEmpty()) {
                    showErrorScreen();
                } else {
                    updateScreen(books);
                }
            }
        });

        return view;
    }

    private void updateScreen(final List<Book> books) {
        Log.d(TAG, "updateScreen:");

        if (mAdapter == null) {
            mAdapter = new BooksRecyclerViewAdapter(this);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.books_list);
        recyclerView.setAdapter(mAdapter);
        if (!mAdapter.isCacheValid(getString(R.string.bible_first_book_name),
                                   getString(R.string.bible_last_book_name))) {
            mAdapter.updateList(books);
        }
        mAdapter.notifyDataSetChanged();

    }

    private void showErrorScreen() {
        Log.d(TAG, "showErrorScreen:");
    }

    @Override
    public void handleInteractionBook(final Book book) {
        Log.d(TAG, "handleInteractionBook: " + book.getBookName());
    }
}
