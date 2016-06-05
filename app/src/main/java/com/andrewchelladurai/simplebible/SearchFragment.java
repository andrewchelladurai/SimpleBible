/*
 * This file 'SearchFragment.java' is part of SimpleBible :
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SearchFragment
        extends Fragment
        implements View.OnClickListener, TextWatcher {

    private static final String TAG = "SearchFragment";
    private AppCompatEditText mTextInput;
    private AppCompatTextView mLabel;
    private SearchAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AppCompatButton mButton;

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new SearchAdapter(SearchResult.getITEMS(), this);
        mRecyclerView.setAdapter(mAdapter);

        mButton = (AppCompatButton) view.findViewById(R.id.fragment_search_button);
        mButton.setOnClickListener(this);
        mTextInput = (AppCompatEditText) view.findViewById(R.id.fragment_search_text);
        mTextInput.addTextChangedListener(this);
        mLabel = (AppCompatTextView) view.findViewById(R.id.fragment_search_label);

        return view;
    }

    @Override
    public void onClick(final View view) {
        if (view instanceof AppCompatButton) {
            String buttonLabel = ((AppCompatButton) view).getText().toString();
            if (buttonLabel.equals(getString(R.string.button_search_text))
                    || buttonLabel.equals(getString(R.string.button_search_reset))) {
                handleSearchButtonClick();
            } else if (buttonLabel.equals(getString(R.string.button_save))) {
                handleSaveButtonClick();
            } else if (buttonLabel.equals(getString(R.string.button_share))) {
                handleShareButtonClick();
            }
        }
    }

    private void handleShareButtonClick() {
        Log.i(TAG, "handleShareButtonClick: ");
    }

    private void handleSaveButtonClick() {
        Log.i(TAG, "handleSaveButtonClick: ");
    }

    private void handleSearchButtonClick() {
        if (mButton.getText().toString().equalsIgnoreCase(
                getString(R.string.button_search_text))) {
            Log.i(TAG, "handleSearchButtonClick: Search Text");
            String input = mTextInput.getText().toString();
            if (input.isEmpty()) {
                Snackbar.make(mTextInput, R.string.search_text_empty, Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (input.length() <= 2) {
                Snackbar.make(mTextInput, R.string.search_text_length, Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            mRecyclerView.removeAllViews();
            DatabaseUtility dbu = DatabaseUtility.getInstance(getContext());
            final ArrayList<String> results = dbu.findText(input);

            if (results != null) {
                if (results.size() < 1) {
                    Snackbar.make(mTextInput, R.string.search_no_results, Snackbar.LENGTH_SHORT)
                            .show();
                }
                SearchResult.refreshList(results, getString(R.string.no_verse_found));
                String text = results.size() + getString(R.string.search_text_results_found);
                mLabel.setText(text);
                mButton.setText(getString(R.string.button_search_reset));
            } else {
                Snackbar.make(mTextInput, R.string.search_no_results, Snackbar.LENGTH_SHORT).show();
            }
        } else if (mButton.getText().toString().equalsIgnoreCase(
                getString(R.string.button_search_reset))) {
            Log.i(TAG, "handleSearchButtonClick: Reset Search");
            SearchResult.refreshList(new ArrayList<String>(0), getString(R.string.no_verse_found));
            mLabel.setText("");
            mButton.setText(getString(R.string.button_search_text));
            mTextInput.setText("");
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count,
                                  final int after) {
    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before,
                              final int count) {
    }

    @Override
    public void afterTextChanged(final Editable s) {
        mButton.setText(getString(R.string.button_search_text));
    }
}
