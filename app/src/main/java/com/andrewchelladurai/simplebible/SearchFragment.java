/*
 *
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

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.SearchResultAdapter;
import com.andrewchelladurai.simplebible.interaction.SearchFragmentInterface;
import com.andrewchelladurai.simplebible.model.DummyContent;
import com.andrewchelladurai.simplebible.presentation.SearchFragmentPresenter;
import com.andrewchelladurai.simplebible.utilities.Constants;

public class SearchFragment
        extends Fragment
        implements SearchFragmentInterface, View.OnClickListener {

    private static final String TAG              = "SB_SearchFragment";
    private static final String ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT";
    private static SearchFragmentPresenter mPresenter;
    private int mColumnCount = 1;
    private TextInputEditText mInput;
    private AppCompatTextView mLabel;
    private AppCompatButton   mSearchButton;
    private AppCompatButton   mResetButton;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(int columnCount) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Context context = view.getContext();
        mInput = (TextInputEditText) view.findViewById(R.id.fragment_search_input);
        mLabel = (AppCompatTextView) view.findViewById(R.id.fragment_search_label);
        mSearchButton = (AppCompatButton) view.findViewById(R.id.fragment_search_button);
        mSearchButton.setOnClickListener(this);
        mResetButton = (AppCompatButton) view.findViewById(R.id.fragment_search_button_reset);
        mResetButton.setOnClickListener(this);
        RecyclerView recyclerView =
                (RecyclerView) view.findViewById(R.id.fragment_search_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new SearchResultAdapter(DummyContent.ITEMS, this));
        return view;
    }

    @Override
    public void showResetButton() {
        mResetButton.setVisibility(View.VISIBLE);
        mSearchButton.setVisibility(View.GONE);
    }

    @Override
    public void showSearchButton() {
        mSearchButton.setVisibility(View.VISIBLE);
        mResetButton.setVisibility(View.GONE);
    }

    @Override
    public String getEmptyInputErrorMessage() {
        return getString(R.string.fragment_search_err_msg_empty);
    }

    @Override
    public String getInputMinLengthErrorMessage() {
        return getString(R.string.fragment_search_err_msg_length_min);
    }

    @Override
    public String getInputMaxLengthErrorMessage() {
        return getString(R.string.fragment_search_err_msg_length_max);
    }

    @Override
    public void init() {
        Log.d(TAG, "init() called:");
        mPresenter = new SearchFragmentPresenter(this);
        mPresenter.init();
        Log.d(TAG, "init() returned:");
    }

    @Override
    public void refresh() {
        mInput.setText(null);
        mLabel.setText(null);
        showSearchButton();
    }

    @Override
    public boolean searchResultLongClicked(DummyContent.SearchResultItem item) {
        Log.d(TAG, "searchResultLongClicked() called with: " + "item = [" + item + "]");
        return true;
    }

    @Override
    public void searchResultClicked(DummyContent.SearchResultItem item) {
        Log.d(TAG, "searchResultClicked() called with: " + "item = [" + item + "]");
    }

    public void onClick(View view) {
        if (view.equals(mSearchButton)) {
            searchButtonClicked();
        } else if (view.equals(mResetButton)) {
            mPresenter.resetButtonClicked();
            focusInputField();
        }
    }

    private void focusInputField() {
        mInput.requestFocus();
    }

    private void searchButtonClicked() {
        String inputString = getInputText();
        String message = mPresenter.searchButtonClicked(inputString);
        if (message.equalsIgnoreCase(Constants.SUCCESS_RETURN_VALUE)) {
            // mPresenter.getSearchResultsForText(getInputText());
        }
        showError(message);
    }

    private void showError(String message) {
        mLabel.setText(message);
    }

    private String getInputText() {
        return mInput.getText().toString();
    }
}
