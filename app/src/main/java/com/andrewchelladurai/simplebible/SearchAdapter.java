/*
 * This file 'SearchAdapter.java' is part of SimpleBible :
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

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.SearchResult.Entry;

import java.util.List;

public class SearchAdapter
        extends RecyclerView.Adapter<SearchAdapter.ResultView> {

    private final List<Entry>    mEntries;
    private final SearchFragment mListener;

    public SearchAdapter(List<Entry> items, SearchFragment listener) {
        mEntries = items;
        mListener = listener;
    }

    @Override
    public ResultView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_search, parent, false);
        return new ResultView(view);
    }

    @Override
    public void onBindViewHolder(final ResultView holder, int position) {
        position = holder.getAdapterPosition();
        holder.mItem = mEntries.get(position);

        String vText = mEntries.get(position).getVerseText();
        String vId[] = mEntries.get(position).getVerseReference().split(":");
        int bookNumber = Integer.parseInt(vId[0]);
//        int chapterNumber = Integer.parseInt(vId[1]);
//        int verseNumber = Integer.parseInt(vId[2]);

        Book.Details book = Book.getBookDetails(bookNumber);
        String bookName = book != null ? book.name : "";

        String template = getString(R.string.search_result_template);
        template = template.replace(getString(R.string.search_result_template_text), vText);
        template = template.replace(getString(R.string.search_result_template_book), bookName);
        template = template.replace(getString(R.string.search_result_template_chapter), vId[1]);
        template = template.replace(getString(R.string.search_result_template_verse), vId[2]);

        holder.mVerse.setText(Html.fromHtml(template));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    private String getString(final int pStringId) {
        return (mListener != null) ? mListener.getString(pStringId) : "";
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    class ResultView
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private static final String TAG = "ResultView";
        private final TextView        mVerse;
        private       View            mView;
        private       ButtonBarLayout mActionBar;
        private       Entry           mItem;

        public ResultView(View view) {
            super(view);
            mView = view;
            mVerse = (TextView) view.findViewById(R.id.fragment_search_result_entry);
            mVerse.setOnClickListener(this);

            mActionBar = (ButtonBarLayout) view.findViewById(R.id.fragment_search_result_actions);
            mActionBar.setVisibility(View.GONE);

            AppCompatButton button = (AppCompatButton) view.findViewById(
                    R.id.fragment_search_result_button_save);
            button.setOnClickListener(this);

            button = (AppCompatButton) view.findViewById(
                    R.id.fragment_search_result_button_share);
            button.setOnClickListener(this);
        }

        @Override protected void finalize() throws Throwable {
            super.finalize();
            mActionBar.setOnClickListener(null);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mVerse.getText();
        }

        @Override public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_search_result_button_save:
                    mListener.handleSaveButtonClick(mItem);
                    break;
                case R.id.fragment_search_result_button_share:
                    mListener.handleShareButtonClick(mItem);
                    break;
                case R.id.fragment_search_result_entry:
                    mActionBar.setVisibility(
                            (mActionBar.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                    break;
                default:
                    Log.d(TAG, "onClick: " + getString(R.string.how_am_i_here));
            }
        }
    }
}
