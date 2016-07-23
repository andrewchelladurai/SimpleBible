/*
 *
 * This file 'AdapterSearchList.java' is part of SimpleBible :
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.ListSearch.Entry;

import java.util.List;

public class AdapterSearchList
        extends RecyclerView.Adapter<AdapterSearchList.SearchView> {

    private final List<Entry>    mValues;
    private final FragmentSearch mListener;

    public AdapterSearchList(List<Entry> items, FragmentSearch listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public SearchView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.entry_search, parent, false);
        return new SearchView(view);
    }

    @Override
    public void onBindViewHolder(final SearchView holder, int position) {
        holder.updateView(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class SearchView
            extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {

        private static final String TAG = "SB_SearchView";
        private final View              mView;
        private final AppCompatTextView mContent;
        private       Entry             mEntry;

        public SearchView(View view) {
            super(view);
            mView = view;
            mContent = (AppCompatTextView) view.findViewById(R.id.verse_content);
            mContent.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (v instanceof AppCompatTextView) {
                if (ListSearch.isEntrySelected(mEntry)) {
                    ListSearch.removeSelectedEntry(mEntry);
                } else {
                    ListSearch.addSelectedEntry(mEntry);
                }
                mContent.setSelected(ListSearch.isEntrySelected(mEntry));
            }
            mListener.showActionBar();
            return true;
        }

        @Override
        public String toString() {
            return getContent();
        }

        public String getContent() {
            return mContent.getText().toString();
        }

        public void updateView(Entry entry) {
            mEntry = entry;
            mContent.setText(Html.fromHtml(Utilities.getFormattedSearchVerse(mEntry)));
            mContent.setSelected(ListSearch.isEntrySelected(mEntry));
        }
    }
}
