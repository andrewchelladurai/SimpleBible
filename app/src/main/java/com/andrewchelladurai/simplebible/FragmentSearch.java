/*
 *
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
 *
 */

package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentSearch
        extends Fragment
        implements View.OnClickListener {

    private static final String TAG              = "SB_FragmentSearch";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private              int    mColumnCount     = 1;

    public FragmentSearch() {
    }

    public static FragmentSearch newInstance() {
        FragmentSearch fragment = new FragmentSearch();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.frag_search_button);
        button.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.frag_search_results);
        Context context = view.getContext();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new VerseViewAdapter(VerseList.ITEMS, this));

        return view;
    }

    @Override public void onClick(View v) {
        if (v instanceof AppCompatButton & v.getId() == R.id.frag_search_button) {
            searchButtonClicked();
        }
    }

    private void searchButtonClicked() {
        String input = getTextToSearch();
    }

    private String getTextToSearch() {
        View view = getView();
        if (view == null) throw new AssertionError(TAG + " getTextToSearch() view == null");

        TextInputEditText input = (TextInputEditText) view.findViewById(R.id.frag_search_input);
        String text = input.getText().toString().trim();

        Log.d(TAG, "getTextToSearch() returned: " + text);
        return text;
    }

    public void onListFragmentInteraction(VerseList.Entry item) {
        Log.d(TAG, "onListFragmentInteraction() called [" + item + "]");
    }
}
