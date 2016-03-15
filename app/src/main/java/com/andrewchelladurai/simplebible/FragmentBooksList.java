/*
 * This file 'FragmentBooksList.java' is part of SimpleBible :
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

public class FragmentBooksList
        extends Fragment {

    public static final String ARG_OLD_TESTAMENT_LIST = "OLD_TESTAMENT_LIST";
    public static final String ARG_NEW_TESTAMENT_LIST = "NEW_TESTAMENT_LIST";
    private static final String TAG = "FragmentBooksList";
    private static final String ARG_COLUMN_COUNT = "COLUMN_COUNT";
    private static FragmentBooksList staticInstanceOT;
    private static FragmentBooksList staticInstanceNT;
    private String booksList = ARG_OLD_TESTAMENT_LIST; // setting a default value
    private InteractionListener mListener;

    public FragmentBooksList() {
    }

    public static FragmentBooksList getInstance(String booksListType) {
        if (booksListType == null) {
            booksListType = ARG_OLD_TESTAMENT_LIST;
        }

        Bundle args = new Bundle();
        if (booksListType.equalsIgnoreCase(ARG_NEW_TESTAMENT_LIST)) {
            if (staticInstanceNT == null) {
                staticInstanceNT = new FragmentBooksList();
                staticInstanceNT.setArguments(args);
                staticInstanceNT.booksList = ARG_NEW_TESTAMENT_LIST;
            }
            return staticInstanceNT;
        } else {
            if (staticInstanceOT == null) {
                staticInstanceOT = new FragmentBooksList();
                staticInstanceOT.setArguments(args);
                staticInstanceOT.booksList = ARG_OLD_TESTAMENT_LIST;
            }
            return staticInstanceOT;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        View view = inflater.inflate(R.layout.fragment_book_list_layout, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            Utilities utilities = Utilities.getInstance();
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            int columnCount = utilities.getChapterListColumnCount(rotation, getResources());

            Log.d(TAG, "onCreateView: mColumnCount = " + columnCount);
            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            if (this.booksList.equalsIgnoreCase(ARG_NEW_TESTAMENT_LIST)) {
                recyclerView.setAdapter(
                        new AdapterBooksList(AllBooks.getNTBooksList(), mListener));
            } else {
                recyclerView.setAdapter(
                        new AdapterBooksList(AllBooks.getOTBooksList(), mListener));
            }
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface InteractionListener {

        void onBooksListFragmentInteraction(AllBooks.Book item);
    }
}
