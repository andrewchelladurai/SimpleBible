/*
 *
 * This file 'SearchResultAdapter.java' is part of SimpleBible :
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

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.SearchTabOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.SearchResultList.SearchResultItem;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.util.List;

public class SearchResultAdapter
        extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private static final String TAG = "SB_SearchResultAdapter";
    private final List<SearchResultItem> mValues;
    private final SearchTabOperations    mListener;

    public SearchResultAdapter(List<SearchResultItem> items, SearchTabOperations listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_search_result_entry, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchResultViewHolder holder, int position) {
        holder.updateContent(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class SearchResultViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {

        private final View              mView;
        private final AppCompatTextView mContent;
        private       SearchResultItem  mItem;

        SearchResultViewHolder(View view) {
            super(view);
            mView = view;
            mContent = (AppCompatTextView) view.findViewById(R.id.searchresult_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContent.getText() + "'";
        }

        void updateContent(SearchResultItem item) {
            mItem = item;
            mContent.setText(getFormattedText(mItem));
            mView.setOnLongClickListener(this);

            // check if this item is selected (may happen before a config change)
            // if yes then update its color
            updateItemColor(mListener.isItemSelected(mItem));
        }

        private void updateItemColor(boolean selected) {
            int textColor, backgroundColor;
            if (selected) {
                backgroundColor = mListener.getLongClickBackgroundColor();
                textColor = mListener.getLongClickTextColor();
            } else {
                backgroundColor = mListener.getDefaultBackgroundColor();
                textColor = mListener.getDefaultTextColor();
            }
            mContent.setBackgroundColor(backgroundColor);
            mContent.setTextColor(textColor);
        }

        @Override
        public boolean onLongClick(View v) {
            String returnValue = mListener.searchResultLongClicked(mItem);
            if (returnValue.equalsIgnoreCase(Constants.ADDED)) {
                updateItemColor(true);
            } else if (returnValue.equalsIgnoreCase(Constants.REMOVED)) {
                updateItemColor(false);
            } else if (returnValue.equalsIgnoreCase(Constants.ERROR)) {
                Log.d(TAG, "onLongClick: returnValue = " + Constants.ERROR);
            }
            return true;
        }
    }

    private SpannableString getFormattedText(SearchResultItem item) {
        BooksList.BookItem bookItem = BooksList.getBookItem(item.getBookNumber());
        if (bookItem != null) {
            String reference = String.format(mListener.getSearchResultReferenceTemplate(),
                                             bookItem.getBookName(),
                                             item.getChapterNumber(),
                                             item.getVerseNumber()) + " ";
            String text = reference + item.getVerseText();
            int highlightColor = mListener.getReferenceHighlightColor();

            return Utilities.getHighlightedText(reference, text, highlightColor);
        } else {
            Log.d(TAG, "getFormattedText: returning null");
            return null;
        }
    }
}
