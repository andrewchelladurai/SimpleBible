/*
 *
 * This file 'ChapterListAdapter.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.ChapterListActivityOperations;
import com.andrewchelladurai.simplebible.model.ChapterList.ChapterItem;

import java.util.List;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 18-Sep-2016 @ 3:51 PM
 */
public class ChapterListAdapter
        extends RecyclerView.Adapter<ChapterListAdapter.ChapterItemViewHolder> {

    private final List<ChapterItem>             mValues;
    private final ChapterListActivityOperations mOperations;

    public ChapterListAdapter(ChapterListActivityOperations operations, List<ChapterItem> items) {
        mOperations = operations;
        mValues = items;
    }

    @Override
    public ChapterItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_chapter_entry, parent, false);
        return new ChapterItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChapterItemViewHolder holder, int position) {
        holder.updateItem(mValues.get(position));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ChapterItemViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final View        mView;
        private final TextView    mContentView;
        private       ChapterItem mItem;

        ChapterItemViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.chapter_entry_number);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        void updateItem(ChapterItem chapterItem) {
            mItem = chapterItem;
            mContentView.setText(mItem.getLabel());
            mView.setOnClickListener(this);
        }

        @Override public void onClick(View v) {
            mOperations.chapterItemClicked(mItem);
        }
    }
}
