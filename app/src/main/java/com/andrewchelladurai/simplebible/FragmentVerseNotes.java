/*
 * This file 'FragmentVerseNotes.java' is part of SimpleBible :
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentVerseNotes
        extends Fragment {

    private static final String ARG_COLUMN_COUNT = "COLUMN_COUNT";
    private int mColumnCount = 1;

    public FragmentVerseNotes() {
    }

    public static FragmentVerseNotes newInstance() {
        FragmentVerseNotes fragment = new FragmentVerseNotes();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, getColumnCount());
        fragment.setArguments(args);
        return fragment;
    }

    private static int getColumnCount() {
        return 1;
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
        View view = inflater.inflate(R.layout.fragment_verse_notes_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new AdapterVerseNotes(Notes.ITEMS, this));
        }
        return view;
    }

    public void onListFragmentInteraction(final Notes.Details pItem) {

    }

    public static class Notes {

        public static final List<Details> ITEMS = new ArrayList<>();
        public static final Map<String, Details> ITEM_MAP = new HashMap<>();
        private static final int COUNT = 25;

        static {
            for (int i = 1; i <= COUNT; i++) {
                addItem(createDummyItem(i));
            }
        }

        private static void addItem(Details item) {
            ITEMS.add(item);
            ITEM_MAP.put(item.id, item);
        }

        private static Details createDummyItem(int position) {
            return new Details(String.valueOf(position), "Item " + position,
                               makeDetails(position));
        }

        private static String makeDetails(int position) {
            StringBuilder builder = new StringBuilder();
            builder.append("Details about Item: ").append(position);
            for (int i = 0; i < position; i++) {
                builder.append("\nMore details information here.");
            }
            return builder.toString();
        }

        public static class Details {

            public final String id;
            public final String content;
            public final String details;

            public Details(String id, String content, String details) {
                this.id = id;
                this.content = content;
                this.details = details;
            }

            @Override
            public String toString() {
                return content;
            }
        }
    }
}
