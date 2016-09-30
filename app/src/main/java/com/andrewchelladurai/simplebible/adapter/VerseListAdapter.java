/*
 *
 * This file 'VerseListAdapter.java' is part of SimpleBible :
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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.ChapterFragmentOperations;
import com.andrewchelladurai.simplebible.model.VerseList.VerseItem;

import java.util.List;

public class VerseListAdapter
        extends RecyclerView.Adapter<VerseListAdapter.VerseViewHolder> {

    private final List<VerseItem>           mValues;
    private final ChapterFragmentOperations mListener;

    public VerseListAdapter(List<VerseItem> items, ChapterFragmentOperations listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public VerseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_verse_entry, parent, false);
        return new VerseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VerseViewHolder holder, int position) {
        holder.updateItem(mValues.get(position));
/*
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.verseClicked(holder.mItem);
                }
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class VerseViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {

        final View     mView;
        final TextView mContentView;
        VerseItem mItem;

        VerseViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.verse_entry_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        void updateItem(VerseItem verseItem) {
            mItem = verseItem;

            String verseNumber = String.valueOf(mItem.getVerseNumber());
            int spanEnd = verseNumber.length();
            String verseText = mItem.getVerseText();

            SpannableString formattedText = new SpannableString(verseNumber + " " + verseText);

            formattedText.setSpan(
                    new ForegroundColorSpan(mListener.getReferenceHighlightColor()),
                    0, spanEnd+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            formattedText.setSpan(
                    new TextAppearanceSpan(mContentView.getContext(),
                                           android.R.style.TextAppearance_DeviceDefault_Large),
                    0, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mContentView.setText(formattedText);
            mView.setOnLongClickListener(this);
        }

        @Override public boolean onLongClick(View v) {
            mListener.verseClicked(mItem);
            return true;
        }
    }
}
