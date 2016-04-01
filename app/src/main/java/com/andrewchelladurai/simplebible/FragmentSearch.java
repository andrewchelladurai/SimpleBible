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

package com.andrewchelladurai.simplebible;

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

import java.util.ArrayList;

public class FragmentSearch
        extends Fragment {

    private AppCompatEditText searchInput;
    private ArrayAdapter<String> searchResults;
    private TextInputLayout hint;

    public FragmentSearch() {
        // Required empty public constructor
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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchInput = (AppCompatEditText) view.findViewById(R.id.fragment_search_input);
        hint = (TextInputLayout) view.findViewById(R.id.fragment_search_hint);
        searchResults = new AdapterVerseList(getContext(), android.R.layout.simple_list_item_1,
                new ArrayList<String>(1));

        ListViewCompat lvc = (ListViewCompat) view.findViewById(R.id.fragment_search_results);
        lvc.setAdapter(searchResults);

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.fragment_search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchButtonClick(view);
            }
        });
        return view;
    }

    private void handleSearchButtonClick(View view) {
        String input = String.valueOf(searchInput.getText());

        if (input.isEmpty() || input.length() < 2) {
            hint.setError(getString(R.string.fragment_v2_search_results_label_length));
            searchInput.requestFocus();
            return;
        }

        String label = (String) ((AppCompatButton) view).getText();
        if (label.equalsIgnoreCase(getString(R.string.fragment_v2_search_button_label_default))) {
            DatabaseUtility databaseUtility = DatabaseUtility.getInstance(getContext());
            ArrayList<String> results = databaseUtility.searchForText(input);
            searchResults.clear();
            if (results.size() > 0) {
                searchResults.addAll(results);
                hint.setHint(results.size() + " " +
                        getString(R.string.fragment_v2_search_button_label_results_found));
                hint.setError("");
            } else {
                label = getString(R.string.fragment_v2_search_button_label_no_results_found);
                hint.setError(label);
            }
            searchResults.notifyDataSetChanged();
            ((AppCompatButton) view).setText(getString(R.string.fragment_v2_search_button_label_reset));
        } else if (label.equalsIgnoreCase(getString(R.string.fragment_v2_search_button_label_reset))) {
            searchResults.clear();
            ((AppCompatButton) view).setText(getString(R.string.fragment_v2_search_button_label_default));
            searchResults.notifyDataSetChanged();
            hint.setHint(getString(R.string.fragment_v2_search_edit_text_hint));
            hint.setError("");
            searchInput.setText("");
        }

    }
}
