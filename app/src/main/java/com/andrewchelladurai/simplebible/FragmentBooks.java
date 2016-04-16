/*
 * This file 'FragmentBooks.java' is part of SimpleBible :
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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBooks
        extends Fragment {

    private static final String TAG = "FragmentBooks";
    private static final String ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT";
    private int mColumnCount = 1;

    public FragmentBooks() {
    }

    public static FragmentBooks newInstance() {
        FragmentBooks fragment = new FragmentBooks();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, getColumnCount());
        fragment.setArguments(args);
        return fragment;
    }

    private static int getColumnCount() {
        return 2;
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookls_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new BookListAdapter(BookDetails.BOOKS, this));
        }
        return view;
    }

    public void bookEntryClicked(final BookDetails.Book pItem) {
        //TODO : Implement Logic
        Log.d(TAG, "bookEntryClicked() called : " + "pItem = [" + pItem + "]");
    }
}
