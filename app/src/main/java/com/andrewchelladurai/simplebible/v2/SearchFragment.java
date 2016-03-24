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
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
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

    private AppCompatTextView resultsLabel;
    private AppCompatEditText searchText;
    private ArrayAdapter<String> searchListAdapter;
    private DatabaseUtility databaseUtility;

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
        resultsLabel = (AppCompatTextView) view.findViewById(R.id.fragment_searchv2_results_label);
        searchListAdapter = new AdapterVerseList(getContext(), android.R.layout.simple_list_item_1,
                new ArrayList<String>(1));

        ListViewCompat lvc = (ListViewCompat) view.findViewById(R.id.fragment_searchv2_results_list);
        lvc.setAdapter(searchListAdapter);

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.fragment_searchv2_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchButtonClick();
            }
        });
        databaseUtility = DatabaseUtility.getInstance(getContext());
        return view;
    }

    private void handleSearchButtonClick() {
        String displayStr = searchText.getText() + " Searched";
        resultsLabel.setText(displayStr);

        searchListAdapter.clear();
        searchListAdapter.addAll(databaseUtility.searchForText(searchText.getText().toString()));
        searchListAdapter.notifyDataSetChanged();
    }

}
