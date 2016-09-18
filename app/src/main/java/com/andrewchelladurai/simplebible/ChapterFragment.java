/*
 *
 * This file 'VerseFragment.java' is part of SimpleBible :
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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.VerseListAdapter;
import com.andrewchelladurai.simplebible.model.ChapterList;
import com.andrewchelladurai.simplebible.model.DummyContent;
import com.andrewchelladurai.simplebible.model.DummyContent.DummyItem;

public class ChapterFragment
        extends Fragment {

    private static final String TAG              = "SB_ChapterFragment";
    public static final  String ARG_COLUMN_COUNT = "column-count";
    public static final  String ARG_ITEM_ID      = "item_id";
    private ChapterList.ChapterItem mItem;
    private int mColumnCount = 1;

    public ChapterFragment() {
    }

    public static ChapterFragment newInstance(int columnCount) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        Log.d(TAG, "newInstance() called with: " + "columnCount = [" + columnCount + "]");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ChapterList.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                    activity.findViewById(R.id.chapter_detail_toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.toString());
            }
        }
        if (getArguments().containsKey(ARG_COLUMN_COUNT)){
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            Log.d(TAG, "onCreate: inside if mColumnCount = " + mColumnCount);
        }else{
            mColumnCount = 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_verse_list);
        Log.d(TAG, "onCreateView: mColumnCount = " + mColumnCount);
        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        recyclerView.setAdapter(new VerseListAdapter(DummyContent.ITEMS, this));

        return view;
    }

    public void verseClicked(DummyItem item) {
        Log.d(TAG, "verseClicked() called with: " + "item = [" + item + "]");
    }
}
