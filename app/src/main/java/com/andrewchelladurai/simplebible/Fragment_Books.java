/*
 *
 * This file is part of SimpleBible : A Holy Bible Application on the
 * Android Mobile platform with easy navigation and offline access.
 *
 * Copyright (c) 2015.
 * Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
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
import android.widget.TextView;

/**
 * A fragment representing a list of Items.
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * Activities containing this fragment MUST implement the {@link
 * OnFragmentInteractionListener}
 * interface.
 */
public class Fragment_Books
        extends Fragment
        implements AbsListView.OnItemClickListener {

    private static final String TAB_NUMBER = "2";
    private static final String CLASS_NAME = "Fragment_Books";
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with Views.
     */
    private ListAdapter mAdapter;

    public Fragment_Books() {
    }

    public static Fragment_Books newInstance(int position) {
        Log.i(CLASS_NAME, "Entering newInstance");
        Fragment_Books fragment = new Fragment_Books();
        Bundle args = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragment.setArguments(args);
        Log.i(CLASS_NAME, "Exiting newInstance");
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                                         " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                                      android.R.id.text1, BookList.getBooks());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(CLASS_NAME, "Entering onCreateView");
        View view = inflater.inflate(R.layout.fragment_bookentry, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        AutoCompleteTextView lookupText = (AutoCompleteTextView) view
                .findViewById(R.id.lookup_book);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(), android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.allbooks));
        lookupText.setAdapter(adapter);
        lookupText.setThreshold(1);
        Log.i(CLASS_NAME, "Exiting onCreateView");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) getActivity().findViewById(R.id.lookup_book)).setText("");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onFragmentBooksInteraction(BookList.getBooks().get(position));
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentBooksInteraction(Book book);
    }

}
