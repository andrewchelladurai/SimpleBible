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
 */package com.andrewchelladurai.simplebible;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
        implements AbsListView.OnItemClickListener {

    private static final String TAB_NUMBER = "3";
    private OnFragmentInteractionListener mListener;

    private ListView             resultList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String>    arrayList;

    public Fragment_Search() {
        // Required empty public constructor
    }

    public static Fragment_Search newInstance(int position) {
        Fragment_Search fragment = new Fragment_Search();
        Bundle          args     = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    "OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__search, container, false);
        updateVariables(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) getActivity().findViewById(R.id.text_to_search_fs)).setText("");
        arrayList.clear();
        listAdapter.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position,
                            long l) {
        if (null != mListener) {
            mListener.onFragmentSearchInteraction(BookList.getBookNumber(position) + "");
        }
    }

    public void searchForResults() {
        TextView tv = (TextView) getActivity().findViewById(R.id.search_header_fs);
        String textToSearch = ((EditText) getActivity()
                .findViewById(R.id.text_to_search_fs)).getText().toString();

        if (textToSearch.length() == 0) {
            arrayList.clear();
            listAdapter.clear();
            tv.setText("");
            return;
        }
        DataBaseHelper dbh = Activity_Welcome.getDataBaseHelper();
        Cursor         cursor = dbh.getDBRecords(textToSearch);

        arrayList.clear();
        listAdapter.clear();

        StringBuilder book   = new StringBuilder();
        StringBuilder verse  = new StringBuilder();
        StringBuilder result = new StringBuilder();
        int           verseNo, chapterNo, bookID;

        if (cursor.moveToFirst()) {
            do {
                verse.append(cursor.getString(cursor.getColumnIndex("Verse")));
                verseNo = cursor.getInt(cursor.getColumnIndex("VerseId"));
                chapterNo = cursor.getInt(cursor.getColumnIndex("ChapterId"));
                bookID = cursor.getInt(cursor.getColumnIndex("BookId"));

//                book.append(BookList.books.get(bookID - 1).getBookName());
                book.append(BookList.getBookName(bookID - 1));
                result.append(book).append(" (").append(chapterNo).append(":")
                        .append(verseNo).append(") ").append(verse);
                arrayList.add(result.toString());

                result.delete(0, result.length());
                book.delete(0, book.length());
                verse.delete(0, verse.length());
            } while (cursor.moveToNext());
            tv.setText(cursor.getCount() + " Verses found");
            if (!cursor.isClosed()) {
                cursor.close();
            }
        } else {
            tv.setText("No Verses found");
        }
        resultList.refreshDrawableState();
        listAdapter.notifyDataSetChanged();
    }

    private void updateVariables(View view) {
        resultList = (ListView) view.findViewById(R.id.search_results_list_fs);
        resultList.setDivider(null);
        arrayList = new ArrayList<>(1);
        listAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, arrayList);
        resultList.setAdapter(listAdapter);
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
