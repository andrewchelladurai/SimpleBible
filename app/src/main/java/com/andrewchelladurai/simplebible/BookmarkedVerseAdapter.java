/*
 * This file 'BookmarkedVerseAdapter.java' is part of SimpleBible :
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
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BookmarkedVerseList.BookmarkedVerse} and
 * makes a call to the specified {@link BookmarkedVerseFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BookmarkedVerseAdapter
        extends RecyclerView.Adapter<BookmarkedVerseAdapter.ViewHolder> {

    private final List<BookmarkedVerseList.BookmarkedVerse> mValues;
    private final BookmarkedVerseFragment.OnListFragmentInteractionListener mListener;

    public BookmarkedVerseAdapter(
            List<BookmarkedVerseList.BookmarkedVerse> items,
            BookmarkedVerseFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookmarkedverse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bookmarkedVerse = mValues.get(position);
        holder.verse_id.setText(mValues.get(position).verseID);
        holder.verse.setText(mValues.get(position).verseText);
        holder.notes.setText(mValues.get(position).verseNotes);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.handleBookmarkedVerseInteraction(holder.bookmarkedVerse);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView verse_id;
        public final TextView verse;
        public final TextView notes;
        public final Button viewButton;
        public final Button editButton;
        public BookmarkedVerseList.BookmarkedVerse bookmarkedVerse;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            verse_id = (TextView) view.findViewById(R.id.bm_verse_id);
            verse = (TextView) view.findViewById(R.id.bm_verse);
            notes = (TextView) view.findViewById(R.id.bm_notes);
            viewButton = (Button) view.findViewById(R.id.bm_button_view);
            editButton = (Button) view.findViewById(R.id.bm_button_edit);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + notes.getText() + "'";
        }
    }
}
