/*
 * This file 'BooksListFragment.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.v2.BookNameContent.BookNameItem;

public class BooksListFragment
        extends Fragment {

    private static final String ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT";
    private int columnCount = 1;

    public BooksListFragment() {
    }

    public static BooksListFragment newInstance(int columnCount) {
        BooksListFragment fragment = new BooksListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_listv2, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            BooksListAdapter adapter = new BooksListAdapter(BookNameContent.ITEMS, this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    public void onListFragmentInteraction(BookNameItem item) {
        Toast.makeText(getContext(),
                item.getBookNumber() + " : " + item.getName() + " : " +
                        item.getChapterCount(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getContext(), ChapterViewActivity.class);
        intent.putExtra(ChapterViewActivity.ARG_BOOK_NUMBER, item.getBookNumber() + "");
        intent.putExtra(ChapterViewActivity.ARG_CHAPTER_NUMBER, 1 + "");
        startActivity(intent);

/*
        Intent intent = new Intent(getContext(), ActivityChapterVerses.class);
        intent.putExtra(ActivityChapterVerses.ARG_BOOK_NUMBER, item.getBookNumber() + "");
        intent.putExtra(ActivityChapterVerses.ARG_CHAPTER_NUMBER, 1 + "");

        startActivity(intent);
*/
    }

}
