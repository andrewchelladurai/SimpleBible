/*
 *
 * This file 'BookViewAdapter.java' is part of SimpleBible :
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
 *
 */

package com.andrewchelladurai.simplebible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.ListBooks.Entry;

import java.util.List;

public class AdapterBookList
        extends RecyclerView.Adapter<AdapterBookList.BookView> {

    private final List<Entry>   mValues;
    private final FragmentBooks mListener;

    public AdapterBookList(List<Entry> items, FragmentBooks listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public BookView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.entry_book, parent, false);
        return new BookView(view);
    }

    @Override
    public void onBindViewHolder(final BookView holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(holder.mItem.getName());
        String chapterText = holder.mItem.getChapterCount() +
                             mListener.getString(R.string.book_details_append_chapters);
        holder.mChapter.setText(chapterText);

        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.bookEntryClicked(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookView
            extends RecyclerView.ViewHolder {

        public final  View     mView;
        private final TextView mName;
        private final TextView mChapter;
        public        Entry    mItem;

        public BookView(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.book_content_name);
            mChapter = (TextView) view.findViewById(R.id.book_content_chapter);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mChapter.getText() + "'";
        }

        public TextView getName() {
            return mName;
        }

        public TextView getChapter() {
            return mChapter;
        }
    }
}
