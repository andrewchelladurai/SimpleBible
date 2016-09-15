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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.SearchTabOperations;
import com.andrewchelladurai.simplebible.model.SearchResultList.SearchResultItem;
import com.andrewchelladurai.simplebible.utilities.Constants;

import java.util.List;

public class SearchResultAdapter
        extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private final List<SearchResultItem> mValues;
    private final SearchTabOperations    mListener;

    public SearchResultAdapter(List<SearchResultItem> items, SearchTabOperations listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.content_searchresult, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.updateContent(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnLongClickListener {

        private final View              mView;
        private final AppCompatTextView mContent;
        private       SearchResultItem  mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContent = (AppCompatTextView) view.findViewById(R.id.searchresult_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContent.getText() + "'";
        }

        public void updateContent(SearchResultItem item) {
            mItem = item;
            mContent.setText(mItem.mContent);
            mView.setOnLongClickListener(this);
            boolean selected = mListener.isItemSelected(mItem);
            updateItemColor(selected);
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
            }
            return true;
        }
    }
}
