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
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FragmentSearch
        extends Fragment
        implements AdapterView.OnItemLongClickListener,
        View.OnClickListener, TextWatcher {

    private AppCompatEditText input;
    private ArrayAdapter<String> results;
    private AppCompatTextView label;
    private ListViewCompat list;
    private AppCompatButton button;

    private String buttonLabelDefault, buttonLabelReset;
    private String resultsEmpty, resultsLength, resultsFound, resultsNotFound;


    public FragmentSearch() {
        setArguments(new Bundle());
    }

    public static FragmentSearch newInstance() {
        return new FragmentSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        input = (AppCompatEditText) view.findViewById(R.id.fragment_search_input);
        input.addTextChangedListener(this);

        label = (AppCompatTextView) view.findViewById(R.id.fragment_search_hint);

        results = new AdapterVerseList(getContext(), android.R.layout.simple_list_item_1,
                new ArrayList<String>(1));
        list = (ListViewCompat) view.findViewById(R.id.fragment_search_results);
        list.setAdapter(results);
        list.setOnItemLongClickListener(this);

        button = (AppCompatButton) view.findViewById(R.id.fragment_search_button);
        button.setOnClickListener(this);

        // String Labels shown on UI.
        buttonLabelDefault = getString(R.string.fragment_v2_search_button_label_default);
        buttonLabelReset = getString(R.string.fragment_v2_search_button_label_reset);
        resultsEmpty = getString(R.string.fragment_v2_search_results_label_empty);
        resultsLength = getString(R.string.fragment_v2_search_results_label_length);
        resultsFound = getString(R.string.fragment_v2_search_button_label_results_found);
        resultsNotFound = getString(R.string.fragment_v2_search_button_label_no_results_found);

        return view;
    }

    private void searchText(String input) {
        results.clear();
        DatabaseUtility databaseUtility = DatabaseUtility.getInstance(getContext());
        ArrayList<String> results = databaseUtility.searchForText(input);
        if (results.size() > 0) {
            this.results.addAll(results);
            showLabel(results.size() + " " + resultsFound);
        } else {
            showLabel(resultsNotFound);
        }
        this.results.notifyDataSetChanged();
        button.setText(buttonLabelReset);
    }

    private void resetValues() {
        results.clear();
        button.setText(buttonLabelDefault);
        results.notifyDataSetChanged();
        showLabel("");
        input.setText("");
    }

    private void showLabel(String text) {
        label.setText(text);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        String title = "Search Result";
        int verseNumber = i;
        int chapterNumber;
        int bookNumber;
        String bookName;
        String verseText;
        FragmentSearch fragment;
        ListViewCompat lvc;

//        VerseLongClickAlert alert = VerseLongClickAlert.newInstance(title, i, this, list);
//        alert.showDialog();
        return true;
    }

    @Override
    public void onClick(View view) {
        String input = String.valueOf(this.input.getText()).trim();

        if (input.isEmpty()) {
            showLabel(resultsEmpty);
            this.input.requestFocus();
            return;
        } else if (input.length() < 2) {
            showLabel(resultsLength);
            this.input.requestFocus();
            return;
        }

        String label = button.getText().toString();

        if (label.equalsIgnoreCase(buttonLabelDefault)) {
            searchText(input);
        } else if (label.equalsIgnoreCase(buttonLabelReset)) {
            resetValues();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        button.setText(buttonLabelDefault);
        showLabel("");
    }

}
