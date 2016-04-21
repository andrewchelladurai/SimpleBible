/*
 * This file 'BookListAdapter.java' is part of SimpleBible :
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

import java.util.List;

public class AdapterBookList
        extends RecyclerView.Adapter<AdapterBookList.DetailsView> {

    private final List<Book.Details> mValues;
    private final FragmentBooksList mListener;

    public AdapterBookList(List<Book.Details> items, FragmentBooksList listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public DetailsView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_books, parent, false);
        return new DetailsView(view);
    }

    @Override
    public void onBindViewHolder(final DetailsView holder, int position) {
        holder.mItem = mValues.get(position);
        String value = mValues.get(position).getName();
        holder.mBookName.setText(value);
        value = mValues.get(position).getChapterCount() + " Chapters";
        holder.mChapterCount.setText(value);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.bookEntryClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class DetailsView
            extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mBookName;
        public final TextView mChapterCount;
        public Book.Details mItem;

        public DetailsView(View view) {
            super(view);
            mView = view;
            mBookName = (TextView) view.findViewById(R.id.fragment_books_book_name);
            mChapterCount = (TextView) view.findViewById(R.id.fragment_books_chapter_count);
        }

        @Override
        public String toString() {
            return mBookName.toString() + " : " + mChapterCount.toString();
        }
    }
}
