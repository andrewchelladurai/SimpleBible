/*
 * This file 'FragmentBooks.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Copyright (c) Andrew Chelladurai - 2015.
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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

/**
 * A fragment representing a list of Items.
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * Activities containing this fragment MUST implement the {@link
 * OnFragmentInteractionListener}
 * interface.
 */
public class FragmentBooks
        extends Fragment
        implements AbsListView.OnItemClickListener {

    private static final String TAB_NUMBER = "2";
    private static final String TAG = "FragmentBooks";
    private static AutoCompleteTextView booknameLookupText;
    private OnFragmentInteractionListener listener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with Views.
     */
    private ListAdapter mAdapter;

    public FragmentBooks() {
    }

    public static FragmentBooks newInstance(int position) {
        Log.d(TAG, "newInstance() Entered");
        FragmentBooks fragment = new FragmentBooks();
        Bundle args = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragment.setArguments(args);
        Log.d(TAG, "newInstance() Exited");
        return fragment;
    }

    public static String getLookupBookName() {
        Log.d(TAG, "getLookupBookName() Entered");
        String bookName = booknameLookupText.getText().toString();
        if (bookName.length() > 0 && BookSList.getBookID(bookName) > -1) {
            Log.d(TAG, "getLookupBookName() returned: " + bookName);
            return bookName;
        } else {
            Log.d(TAG, "getLookupBookName() returned: " + null);
            return null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, BookSList.getBooks());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() Exited");
        View view = inflater.inflate(R.layout.fragment_bookentry, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        booknameLookupText = (AutoCompleteTextView) view
                .findViewById(R.id.autocomplete_bookname_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(), android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.allbooks));
        booknameLookupText.setAdapter(adapter);
        booknameLookupText.setThreshold(1);
        Log.d(TAG, "onCreateView() Exited");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        booknameLookupText.setText("");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != listener) {
            listener.onFragmentBooksInteraction(BookSList.getBooks().get(position));
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentBooksInteraction(BookUnit book);
    }

}
