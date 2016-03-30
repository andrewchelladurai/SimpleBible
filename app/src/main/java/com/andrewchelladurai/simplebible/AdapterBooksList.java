/*
 * This file 'BooksListAdapter.java' is part of SimpleBible :
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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.BookNameContent.BookItem;

import java.util.List;

public class AdapterBooksList
        extends RecyclerView.Adapter<AdapterBooksList.BookName> {

    private final List<BookItem> mValues;
    private final FragmentBooksList mListener;

    public AdapterBooksList(List<BookItem> items, FragmentBooksList listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public BookName onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookname, parent, false);
        return new BookName(view);
    }

    @Override
    public void onBindViewHolder(final BookName holder, int position) {
        holder.bookItem = mValues.get(position);
        holder.book_name.setText(mValues.get(position).toString());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.bookItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookName
            extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView book_name;
        public BookItem bookItem;

        public BookName(View view) {
            super(view);
            this.view = view;
            book_name = (TextView) view.findViewById(R.id.fragment_bookname_content);
        }

        @Override
        public String toString() {
            return book_name.getText() + "";
        }

    }
}
