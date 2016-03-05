/*
 * This file 'FragmentChapterVerses.java' is part of SimpleBible :
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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentChapterVerses
        extends Fragment
        implements AdapterView.OnItemLongClickListener {

    private static final String TAG = "FragmentChapterVerses";
    private static final String ARG_VERSES_LIST = "VERSES_LIST";
    private static FragmentChapterVerses staticInstance;
    private static ArrayAdapter<String>  verseListAdapter;
    private ArrayList<String> versesList = new ArrayList<>(0);

    private InteractionListener mListener;

    public FragmentChapterVerses() {
        // Required empty public constructor
    }

    public static FragmentChapterVerses getInstance() {
        if (staticInstance == null) {
            staticInstance = new FragmentChapterVerses();
            Bundle args = new Bundle();
            args.putStringArrayList(ARG_VERSES_LIST, new ArrayList<String>(0));
            staticInstance.setArguments(args);
        }
        return staticInstance;
    }

    @Override
    public void onResume() {
        verseListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            versesList = getArguments().getStringArrayList(ARG_VERSES_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verses, container, false);
        ArrayList<String> verseList = new ArrayList<>(1);
        verseListAdapter = new VerseListAdapter(getContext(),
                                                android.R.layout.simple_list_item_1,
                                                verseList);
        ListViewCompat listViewCompat =
                (ListViewCompat) view.findViewById(R.id.fragment_v2_verses_list);
        listViewCompat.setAdapter(verseListAdapter);
        listViewCompat.setOnItemLongClickListener(this);
        return view;
    }

    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.handleVersesFragmentInteraction(view);
        }
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

    public void refreshVersesList(int bookNumber, int chapterNumber) {
        Log.d(TAG, "refreshVersesList() called with bookNumber = ["
                   + bookNumber + "], chapterNumber = [" + chapterNumber + "]");
        versesList = DatabaseUtility.getInstance(getContext())
                                    .getAllVerseOfChapter(bookNumber, chapterNumber);
        verseListAdapter.clear();
        verseListAdapter.addAll(versesList);
        verseListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        String title = getActivity().getTitle().toString();
        title = title.replace("Chapter ", "");
        String verse = ((TextView) view).getText()
                       + " -- The Holy Bible (New International Version)";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, title + ":" + verse);
        startActivity(intent);
        return true;
    }

    public interface InteractionListener {

        void handleVersesFragmentInteraction(View view);
    }
}
