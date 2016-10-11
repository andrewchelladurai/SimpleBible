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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.SearchResultAdapter;
import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.interaction.SearchTabOperations;
import com.andrewchelladurai.simplebible.model.SearchResultList;
import com.andrewchelladurai.simplebible.presentation.SearchTabPresenter;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.util.Collection;

public class SearchFragment
        extends Fragment
        implements SearchTabOperations, View.OnClickListener, TextWatcher {

    private static final String TAG = "SB_SearchFragment";
    private static SearchTabPresenter mPresenter;
    private int mColumnCount = 1;
    private TextInputEditText   mInput;
    private AppCompatTextView   mLabel;
    private AppCompatButton     mSearchButton;
    private AppCompatButton     mResetButton;
    private SearchResultAdapter mListAdapter;
    private RecyclerView        mRecyclerView;
    private AppCompatTextView   mHelpLabel;
    private AppCompatButton     mBookmarkButton;
    private AppCompatButton     mShareButton;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, 1); // Backup
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mInput = (TextInputEditText) view.findViewById(R.id.fragment_search_input);
        mInput.addTextChangedListener(this);
        mLabel = (AppCompatTextView) view.findViewById(R.id.fragment_search_label);

        mSearchButton = (AppCompatButton) view.findViewById(R.id.fragment_search_button_search);
        mSearchButton.setOnClickListener(this);

        mResetButton = (AppCompatButton) view.findViewById(R.id.fragment_search_button_reset);
        mResetButton.setOnClickListener(this);

        mBookmarkButton = (AppCompatButton) view.findViewById(R.id.fragment_search_button_bookmark);
        mBookmarkButton.setOnClickListener(this);

        mShareButton = (AppCompatButton) view.findViewById(R.id.fragment_search_button_share);
        mShareButton.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));

        mHelpLabel = (AppCompatTextView) view.findViewById(R.id.fragment_search_label_help);
        mHelpLabel.setText(Html.fromHtml(getString(R.string.fragment_search_label_help)));

        init();
        mRecyclerView.setAdapter(mListAdapter);

        refreshList();
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
        if (null == mPresenter) {
            mPresenter = new SearchTabPresenter(this);
            mPresenter.init();
            mColumnCount = getResources().getInteger(R.integer.column_count_search_result_list);
        }
        if (null == mListAdapter) {
            mListAdapter = new SearchResultAdapter(SearchResultList.getItems(), this);
        }
        Log.d(TAG, "init() returned:");
    }

    @Override
    public void refresh() {
        resetFields();
        refreshList();
        focusInputField();
    }

    @Override public int getDefaultBackgroundColor() {
        return ContextCompat.getColor(getContext(), R.color.cardBackground);
    }

    @Override public int getLongClickBackgroundColor() {
        return ContextCompat.getColor(getContext(), R.color.long_press_background);
    }

    @Override public int getLongClickTextColor() {
        return ContextCompat.getColor(getContext(), R.color.long_press_text_color);
    }

    @Override public int getDefaultTextColor() {
        return ContextCompat.getColor(getContext(), R.color.textColor);
    }

    @Override public void resetFields() {
        toggleActionButtons(SearchResultList.isSelectedItemsEmpty());
        mInput.setText("");
        showSearchButton();
        refreshList();
        showError("");
        focusInputField();
    }

    @Override public String getResultsCountString(int count) {
        return getResources().getQuantityString(
                R.plurals.fragment_search_result_count_template, count, count);
    }

    @Override public void showMessage(String message) {
        showError(message);
    }

    @Override public String getSearchResultReferenceTemplate() {
        return getString(R.string.fragment_search_reference_template);
    }

    @Override public int getReferenceHighlightColor() {
        return ContextCompat.getColor(getContext(), R.color.reference_highlight_color);
    }

    @Override public void refreshList() {
        toggleActionButtons(SearchResultList.isSelectedItemsEmpty());
        mListAdapter.notifyDataSetChanged();
        if (mListAdapter.getItemCount() == 0) {
            showSearchButton();
            mRecyclerView.setVisibility(View.GONE);
            mHelpLabel.setVisibility(View.VISIBLE);
        } else {
            showResetButton();
            mRecyclerView.setVisibility(View.VISIBLE);
            mHelpLabel.setVisibility(View.GONE);
        }
    }

    @Override public void toggleActionButtons(boolean isSelectedItemsEmpty) {
        int viewMode = (isSelectedItemsEmpty) ? View.GONE : View.VISIBLE;
        mBookmarkButton.setVisibility(viewMode);
        mShareButton.setVisibility(viewMode);
    }

    @Override public String getShareTemplate() {
        return getString(R.string.share_verse_template);
    }

    @Override public void shareSelectedVerses(String stringToShare) {
        startActivity(Utilities.shareVerse(stringToShare));
    }

    public void onClick(View view) {
        if (view.equals(mSearchButton)) {
            searchButtonClicked();
        } else if (view.equals(mResetButton)) {
            mPresenter.resetButtonClicked();
        } else if (view.equals(mBookmarkButton)) {
            bookmarkButtonClicked();
        } else if (view.equals(mShareButton)) {
            mPresenter.shareButtonClicked();
        } else {
            Log.d(TAG, "onClick: " + getString(R.string.how_am_i_here));
        }
    }

    private void bookmarkButtonClicked() {
        Collection<SearchResultList.SearchResultItem> items = SearchResultList.getSelectedItems();
        if (items.isEmpty()) {
            Log.d(TAG, "bookmarkButtonClicked: No Selected Entries :\n"
                       + getString(R.string.how_am_i_here));
            return;
        }
        String reference = Utilities.prepareBookmarkReferenceFromSearchResults(items);
        if (reference.isEmpty()) {
            Log.d(TAG, "bookmarkButtonClicked: reference is empty");
            return;
        }
        String returnValue = mPresenter.bookmarkButtonClicked(reference);
        Bundle args = new Bundle();
        args.putString(BookmarkActivityOperations.ARG_REFERENCE, reference);
        switch (returnValue) {
            case BookmarkActivityOperations.UPDATE:
                args.putString(BookmarkActivityOperations.ARG_MODE,
                               BookmarkActivityOperations.UPDATE);
                break;
            case BookmarkActivityOperations.CREATE:
            default:
                args.putString(BookmarkActivityOperations.ARG_MODE,
                               BookmarkActivityOperations.CREATE);
                Log.w(TAG, "bookmarkButtonClicked: " + getString(R.string.how_am_i_here));
                Log.d(TAG, "bookmarkButtonClicked: setting ARG_MODE = CREATE");
        }
        Intent intent = new Intent(getContext(), BookmarkActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    private void focusInputField() {
        mInput.requestFocus();
    }

    private void searchButtonClicked() {
        String inputString = getInputText();
        String returnValue = mPresenter.searchButtonClicked(inputString);

        if (returnValue.equalsIgnoreCase(Constants.SUCCESS)) {
            mPresenter.getSearchResultsForText(inputString);
        } else {
            showError(returnValue);
            focusInputField();
        }
    }

    private void showError(String message) {
        mLabel.setText(message);
    }

    private String getInputText() {
        return mInput.getText().toString();
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override public void afterTextChanged(Editable s) {
        if (!getInputText().isEmpty()) {
            showSearchButton();
        }
        showMessage("");
    }
}
