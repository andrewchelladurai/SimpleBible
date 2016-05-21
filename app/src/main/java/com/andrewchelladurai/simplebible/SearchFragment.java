/*
 *
 * This file 'SearchFragment.java' is part of SimpleBible :  SimpleBible
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

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.SearchResult.Verse;

import java.util.ArrayList;

public class SearchFragment
        extends Fragment
        implements View.OnClickListener {

    private static final String TAG = "SearchFragment";
    private AppCompatEditText mTextInput;
    private AppCompatTextView mLabel;
    private SearchViewAdapter mAdapter;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new SearchViewAdapter(SearchResult.getITEMS(), this);
        recyclerView.setAdapter(mAdapter);

        AppCompatButton button =
                (AppCompatButton) view.findViewById(R.id.fragment_search_button);
        button.setOnClickListener(this);
        mTextInput = (AppCompatEditText) view.findViewById(R.id.fragment_search_text);
        mLabel = (AppCompatTextView) view.findViewById(R.id.fragment_search_label);

        return view;
    }

    private void searchButtonClicked() {
        Log.i(TAG, "searchButtonClicked");
        String input = mTextInput.getText().toString();
        if (input.isEmpty()) {
            Snackbar.make(mTextInput, R.string.search_text_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (input.length() <= 2) {
            Snackbar.make(mTextInput, R.string.search_text_length, Snackbar.LENGTH_SHORT).show();
            return;
        }

        DatabaseUtility dbu = DatabaseUtility.getInstance(getContext());
        final ArrayList<String> results = dbu.findText(input);

        if (results != null) {
            if (results.size() < 1) {
                Snackbar.make(mTextInput, R.string.search_no_results, Snackbar.LENGTH_SHORT).show();
            } else {
                SearchResult.refreshList(results);
            }
        } else {
            Snackbar.make(mTextInput, R.string.search_no_results, Snackbar.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    public void searchResultLongClicked(final Verse pItem) {
        Log.i(TAG, "searchResultLongClicked: " + pItem.toString());
    }

    @Override public void onClick(final View view) {
        if (view instanceof AppCompatButton) {
            searchButtonClicked();
        }
    }
}
