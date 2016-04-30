/*
 * This file 'FragmentSearch.java' is part of SimpleBible :
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
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FragmentSearch
        extends Fragment
        implements View.OnClickListener {

    private static final String TAG = "FragmentSearch";
    private TextInputEditText mSearchText;
    private ListViewCompat mResultsList;
    private ArrayAdapter<String> mResultsAdapter;
    private ArrayList<String> mResultsArray;

    public FragmentSearch() {
    }

    public static FragmentSearch newInstance() {
        FragmentSearch fragment = new FragmentSearch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchText = (TextInputEditText) view.findViewById(R.id.fragment_search_text);
        mResultsList = (ListViewCompat) view.findViewById(R.id.fragment_search_results);
        mResultsArray = new ArrayList<>(1);
        mResultsAdapter = new AdapterVerseItem<>(
                getContext(), android.R.layout.simple_list_item_1, mResultsArray);
        mResultsList.setAdapter(mResultsAdapter);

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.fragment_search_button);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(final View pView) {
        switch (pView.getId()) {
            case R.id.fragment_search_button:
                AppCompatButton button = (AppCompatButton) pView;
                String label = button.getText().toString();
                if (label.equalsIgnoreCase(getString(R.string.label_search))) {
                    handleSearchClicked(button);
                } else if (label.equalsIgnoreCase(getString(R.string.label_reset))) {
                    handleResetClicked(button);
                } else {
                    Log.d(TAG, "onClick:" + getString(R.string.how_am_i_here));
                }
                break;
            default:
                Log.d(TAG, "onClick:" + getString(R.string.how_am_i_here));
        }
    }

    private void handleSearchClicked(AppCompatButton pButton) {
        Log.i(TAG, "handleSearchClicked: ");
        String input = mSearchText.getText().toString();
        if (input.isEmpty()) {
            mSearchText.setError(getString(R.string.message_search_empty_text));
            return;
        }
        if (input.length() < 3) {
            mSearchText.setError(getString(R.string.message_search_length));
            return;
        }
        DatabaseUtility dbu = DatabaseUtility.getInstance(getContext());
        ArrayList<String> results = dbu.findText(input);
        Log.d(TAG, "handleSearchClicked: " + results.size() + " results returned");
        mResultsArray.clear();
        if (!results.isEmpty()) {
            mSearchText.setError(
                    results.size() + " " + getString(R.string.message_search_results_found));
            mResultsArray.addAll(results);
            pButton.setText(getString(R.string.label_reset));
        } else {
            mSearchText.setError("No " + getString(R.string.message_search_results_found));
        }
        mResultsAdapter.notifyDataSetChanged();
        mResultsList.setSelectionAfterHeaderView();
    }

    private void handleResetClicked(AppCompatButton pButton) {
        mSearchText.setText("");
        mSearchText.setError(null);
        mResultsArray.clear();
        mResultsAdapter.notifyDataSetChanged();
        mResultsList.setSelectionAfterHeaderView();
        pButton.setText(getString(R.string.label_search));
    }
}
