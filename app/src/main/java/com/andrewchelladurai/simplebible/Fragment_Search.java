/*
 * This file 'Fragment_Search.java' is part of SimpleBible :  An Android Bible application
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
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Search.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Search
        extends Fragment
        implements AbsListView.OnItemClickListener,
                   AdapterView.OnItemLongClickListener,
                   TextWatcher {

    private static final String CLASS_NAME = "Fragment_Search";
    private static final String TAB_NUMBER = "3";
    private OnFragmentInteractionListener mListener;

    private ListView             resultList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String>    arrayList;
    private EditText             searchTextView;
    private TextView             messageHeader;

    private StringBuilder currentSearchText = new StringBuilder("");

    public Fragment_Search() {
        // Required empty public constructor
    }

    public static Fragment_Search newInstance(int position) {
        Log.i(CLASS_NAME, "Entering newInstance");
        Fragment_Search fragment = new Fragment_Search();
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
            throw new ClassCastException(activity.toString()
                                         + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(CLASS_NAME, "Entering onCreateView");
        View v = inflater.inflate(R.layout.fragment__search, container, false);
        updateVariables(v);
        Log.i(CLASS_NAME, "Exiting onCreateView");
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (null != mListener) {
            mListener.onFragmentSearchInteraction(BookSList.getBookNumber(position) + "");
        }
    }

    public void searchForResults(View view) {
        Button button = (Button) view;
        currentSearchText.delete(0, currentSearchText.length());
        currentSearchText.append(searchTextView.getText().toString());
        Log.d(CLASS_NAME, "Entering searchForResults" + currentSearchText);

        if ((button.getText() + "").equalsIgnoreCase(
                getString(R.string.fragment_serach_button_search_reset))
            || currentSearchText.length() == 0) {
            Log.d(CLASS_NAME, "searchForResults - Inside first IF");
            ((EditText) getActivity().findViewById(R.id.fragmentsearch_input_text_field))
                    .setText("");
            arrayList.clear();
            listAdapter.clear();
            messageHeader.setText("");
            button.setText(R.string.fragment_serach_button_search);
            return;
        }
        Log.d(CLASS_NAME, "searchForResults - Immediately after first IF");
        Cursor cursor = ActivityWelcome.getDataBaseHelper()
                                       .getDBRecords(currentSearchText.toString());

        arrayList.clear();
        listAdapter.clear();

        int verseNo, chapterNo, bookID;
        StringBuilder book = new StringBuilder();
        StringBuilder verse = new StringBuilder();
        StringBuilder result = new StringBuilder();

        if (cursor.moveToFirst()) {
            Log.d(CLASS_NAME, "searchForResults - returned at least one result.");
            do {
                verse.append(cursor.getString(cursor.getColumnIndex("Verse")));
                verseNo = cursor.getInt(cursor.getColumnIndex("VerseId"));
                chapterNo = cursor.getInt(cursor.getColumnIndex("ChapterId"));
                bookID = cursor.getInt(cursor.getColumnIndex("BookId"));

                book.append(BookSList.getBookName(bookID - 1));
                result.append(book)
                      .append(" (")
                      .append(chapterNo).append(":")
                      .append(verseNo).append(") ")
                      .append(verse);
                arrayList.add(result.toString());

                result.delete(0, result.length());
                book.delete(0, book.length());
                verse.delete(0, verse.length());
            } while (cursor.moveToNext());
            messageHeader.setText(cursor.getCount() + " Verses found");
            if (!cursor.isClosed()) {
                cursor.close();
            }
        } else {
            messageHeader.setText("No Verses found");
        }
//        resultList.refreshDrawableState();
        listAdapter.notifyDataSetChanged();
        button.setText(R.string.fragment_serach_button_search_reset);
        Log.d(CLASS_NAME, "Exiting searchForResults.");
    }

    private void updateVariables(View view) {
        Log.i(CLASS_NAME, "Entering updateVariables");

        arrayList = new ArrayList<>(1);
        listAdapter = new ArrayAdapter<String>(view.getContext(),
                                               android.R.layout.simple_list_item_1,
                                               android.R.id.text1, arrayList);
        resultList = (ListView) view.findViewById(R.id.fragmentsearch_resultlist);
        resultList.setDivider(null);
        resultList.setOnItemLongClickListener(this);
        resultList.setAdapter(listAdapter);

        searchTextView = (EditText) view.findViewById(R.id.fragmentsearch_input_text_field);
        searchTextView.addTextChangedListener(this);
        messageHeader = (TextView) view.findViewById(R.id.fragmentsearch_resultlabel);

        Log.d(CLASS_NAME, "Exiting updateVariables");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(CLASS_NAME, "Entering onItemLongClick");
        String verse = ((TextView) view).getText()
                       + " -- The Holy Bible (New International Version)";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, verse);
        startActivity(intent);
        Log.d(CLASS_NAME, "Exiting onItemLongClick");
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        Button button = (Button) getActivity().findViewById(R.id.fragmentsearch_searchbutton);
        button.setText(getString(R.string.fragment_serach_button_search));
        messageHeader.setText("");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentSearchInteraction(String id);
    }
}
