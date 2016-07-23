/*
 *
 * This file 'VerseViewAdapter.java' is part of SimpleBible :
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

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AdapterVerseList
        extends RecyclerView.Adapter<AdapterVerseList.VerseView> {

    private final List<ListVerse.Entry> mValues;
    private final FragmentChapterVerses mListener;

    public AdapterVerseList(List<ListVerse.Entry> items, FragmentChapterVerses listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public VerseView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.entry_verse, parent, false);
        return new VerseView(view);
    }

    @Override
    public void onBindViewHolder(final VerseView holder, int position) {
        holder.update(mValues.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class VerseView
            extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {

        private static final String TAG = "SB_ViewHolder";
        public final  View              mView;
        private final AppCompatTextView mContent;
        public        ListVerse.Entry   mEntry;
        private boolean isSelected = false;

        public VerseView(View view) {
            super(view);
            mView = view;
            mContent = (AppCompatTextView) view.findViewById(R.id.verse_content);
            mContent.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (v instanceof AppCompatTextView) {
                isSelected = !isSelected;
                if (isSelected) {
                    ListVerse.addSelectedEntry(mEntry);
                } else {
                    ListVerse.removeSelectedEntry(mEntry);
                }
                Log.i(TAG, mEntry.getReference() + " selected = " + isSelected);
                mListener.showActionBar();
            }
            return true;
        }

        @Override
        public String toString() {
            return getContent();
        }

        public String getContent() {
            return mContent.getText().toString();
        }

        public void update(ListVerse.Entry entry, int position) {
            mEntry = entry;
            setContent(position, mEntry.getVerseText());
        }

        public void setContent(int position, String newContent) {
            mContent.setText(
                    Html.fromHtml(Utilities.getFormattedChapterVerse(position + 1, newContent)));
        }
    }
}
