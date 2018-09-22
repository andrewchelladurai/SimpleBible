/*
 *
 * This file 'BooksRecyclerViewAdapter.java' is part of SimpleBible :
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.ops.BooksScreenOps;
import com.andrewchelladurai.simplebible.ops.ViewHolderOps;
import com.andrewchelladurai.simplebible.ui.BooksRecyclerViewAdapter.BookView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BooksRecyclerViewAdapter
    extends RecyclerView.Adapter<BookView> {

    private static final String TAG = "BooksRecyclerViewAdapte";

    private final List<Book> mCache = new ArrayList<>();
    private final BooksScreenOps mOps;

    BooksRecyclerViewAdapter(BooksScreenOps ops) {
        mOps = ops;
    }

    @Override
    public BookView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_book, parent, false);
        return new BookView(view);
    }

    @Override
    public void onBindViewHolder(final BookView holder, int position) {
        holder.updateView(mCache.get(position));
    }

    @Override
    public int getItemCount() {
        return mCache.size();
    }

    public boolean isCacheValid(@NonNull final String first_book_name,
                                @NonNull final String last_book_name) {
        return (!mCache.isEmpty()
                && mCache.size() == Book.EXPECTED_BOOK_COUNT
                && mCache.get(0).getBookName().equalsIgnoreCase(first_book_name)
                && mCache.get(mCache.size()).getBookName().equalsIgnoreCase(last_book_name));
    }

    public void updateList(@NonNull final List<Book> books) {
        mCache.clear();
        mCache.addAll(books);
        Log.d(TAG, "updateList : populated [" + getItemCount() + "] books");
    }

    class BookView
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        private final View mView;
        private final TextView mName;
        private final TextView mDetails;
        private Book mBook;

        BookView(View view) {
            super(view);
            mView = view;
            mName = view.findViewById(R.id.book_name);
            mDetails = view.findViewById(R.id.book_details);
        }

        @Override
        public String toString() {
            return mBook.toString();
        }

        @Override
        public void updateView(final Object object) {
            mBook = (Book) object;
            mName.setText(mBook.getBookName());
            mDetails.setText(String.valueOf(mBook.getBookChapterCount()));

            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mOps) {
                        mOps.handleInteractionBook(mBook);
                    }
                }
            });
        }
    }
}
