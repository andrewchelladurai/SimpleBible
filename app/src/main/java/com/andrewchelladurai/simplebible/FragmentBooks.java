/*
 *
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
 *
 */

package com.andrewchelladurai.simplebible;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.BooksList.Entry;

public class FragmentBooks
        extends Fragment {

    private static final String TAG = "SB_FragmentBooks";

    public FragmentBooks() {
    }

    public static FragmentBooks newInstance() {
        int columnCount = 2;
        FragmentBooks fragment = new FragmentBooks();
        Bundle args = new Bundle();
        args.putInt(Utilities.BOOKS_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_books_list);
        recyclerView.setAdapter(new AdapterBookList(BooksList.getItems(), this));
        return view;
    }

    public void bookEntryClicked(Entry item) {
        Log.d(TAG, "bookEntryClicked() called with [" + item + "]");
        Bundle args = new Bundle();
        args.putParcelable(Utilities.CURRENT_BOOK, item);
        args.putString(Utilities.CURRENT_CHAPTER_NUMBER, "1");

        Intent intent = new Intent(getContext(), ActivityChapterList.class);
        intent.putExtras(args);
        startActivity(intent);
    }
}
