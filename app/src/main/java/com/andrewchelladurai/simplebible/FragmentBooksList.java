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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentBooksList
        extends Fragment {

    private static final String TAG = "FragmentBooks";
    private static final String ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT";
    private int mColumnCount = 1;

    public FragmentBooksList() {
    }

    public static FragmentBooksList newInstance() {
        FragmentBooksList fragment = new FragmentBooksList();
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
            recyclerView.setAdapter(new AdapterBookList(Book.BOOKS, this));
        }
        return view;
    }

    public void bookEntryClicked(final Book.Details pItem) {
        Intent intent = new Intent(getContext(), ActivityChapter.class);
        intent.putExtra(ActivityChapter.ARG_BOOK_NUMBER, pItem.getNumber());
        intent.putExtra(ActivityChapter.ARG_CHAPTER_NUMBER, 1 + "");
        startActivity(intent);
    }

    /**
     * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 16-Apr-2016 @ 11:40 AM
     */
    public static class Book {

        public static final List<Details> BOOKS = new ArrayList<>();
        public static final Map<String, Details> BOOK_MAP = new HashMap<>();
        private static final String TAG = "BookDetails";

        public static void populateDetails(final String[] pStringArray) {
            if (BOOKS.size() > 0) {
                Log.d(TAG, "populateDetails: " + BOOKS.size() + " books already created");
                return;
            }

            int bookNumber = 1;
            String[] splitValue;
            Details book;
            for (String value : pStringArray) {
                splitValue = value.split(":");
                book = new Details(bookNumber + "", splitValue[0], splitValue[1]);
                BOOKS.add(book);
                BOOK_MAP.put(book.number, book);
                bookNumber++;
            }
            Log.d(TAG, "populateDetails: " + BOOKS.size() + " books created");
        }

        public static Details getDetails(int pBookNumber) {
            return BOOK_MAP.get(pBookNumber + "");
        }

        public static class Details {

            private final String number;
            private final String name;
            private final String chapterCount;

            public Details(String pNumber, String pName, String pChapterCount) {
                name = pName;
                number = pNumber;
                chapterCount = pChapterCount;
            }

            @Override
            public String toString() {
                return number + " : " + name + " : " + chapterCount;
            }

            public String getNumber() {
                return number;
            }

            public String getName() {
                return name;
            }

            public String getChapterCount() {
                return chapterCount;
            }
        }
    }
}
