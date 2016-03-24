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
 */

package com.andrewchelladurai.simplebible.v2;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.andrewchelladurai.simplebible.DatabaseUtility;
import com.andrewchelladurai.simplebible.R;

import java.util.ArrayList;

public class SearchFragment
        extends Fragment {

    //    private AppCompatTextView resultsLabel;
    private AppCompatEditText searchText;
    private ArrayAdapter<String> searchListAdapter;
    private DatabaseUtility databaseUtility;
    private TextInputLayout hintLayout;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_searchv2, container, false);
        searchText = (AppCompatEditText) view.findViewById(R.id.fragment_searchv2_search_text);
        hintLayout = (TextInputLayout) view.findViewById(R.id.fragment_searchv2_hint_layout);
        searchListAdapter = new AdapterVerseList(getContext(), android.R.layout.simple_list_item_1,
                new ArrayList<String>(1));

        ListViewCompat lvc = (ListViewCompat) view.findViewById(R.id.fragment_searchv2_results_list);
        lvc.setAdapter(searchListAdapter);

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.fragment_searchv2_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchButtonClick(view);
            }
        });
        databaseUtility = DatabaseUtility.getInstance(getContext());
        return view;
    }

    private void handleSearchButtonClick(View view) {
        String input = String.valueOf(searchText.getText());

        if (input.isEmpty() || input.length() < 2) {
            hintLayout.setError(getString(R.string.fragment_v2_search_results_label_length));
            searchText.requestFocus();
            return;
        }

        String label = (String) ((AppCompatButton) view).getText();
        if (label.equalsIgnoreCase(getString(R.string.fragment_v2_search_button_label_default))) {
            ArrayList<String> results = databaseUtility.searchForText(input);
            searchListAdapter.clear();
            if (results.size() > 0) {
                searchListAdapter.addAll(results);
                hintLayout.setHint(results.size() + " " +
                        getString(R.string.fragment_v2_search_button_label_results_found));
                hintLayout.setError("");
            } else {
                label = getString(R.string.fragment_v2_search_button_label_no_results_found);
                hintLayout.setError(label);
            }
            searchListAdapter.notifyDataSetChanged();
            ((AppCompatButton) view).setText(getString(R.string.fragment_v2_search_button_label_reset));
        } else if (label.equalsIgnoreCase(getString(R.string.fragment_v2_search_button_label_reset))) {
            searchListAdapter.clear();
            ((AppCompatButton) view).setText(getString(R.string.fragment_v2_search_button_label_default));
            searchListAdapter.notifyDataSetChanged();
            hintLayout.setHint(getString(R.string.fragment_v2_search_edit_text_hint));
            hintLayout.setError("");
            searchText.setText("");
        }

    }
}
