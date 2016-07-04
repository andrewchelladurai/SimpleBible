/*
 *
 * This file 'ItemDetailFragment.java' is part of SimpleBible :
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

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentChapterVerses
        extends Fragment {

    private static final String TAG = "SB_FragChapterVerses";

    public FragmentChapterVerses() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CollapsingToolbarLayout appBar = (CollapsingToolbarLayout) getActivity()
                .findViewById(R.id.activity_chapter_detail_toolbar_layout);

        BooksList.Entry book = getArguments().getParcelable(Utilities.CURRENT_BOOK);
        if (book == null) {
            Utilities.showError(TAG + " onCreate : book == null");
        }
        ChapterList.Entry chapter = getArguments().getParcelable(Utilities.CURRENT_CHAPTER);
        if (chapter == null) {
            Utilities.showError(TAG + " onCreate : chapter == null");
        }
        String chapterNumber = chapter.getChapterNumber();
        if (chapterNumber == null) {
            Utilities.showError(TAG + " onCreate : chapterNumber == null");
        }
        StringBuilder title = new StringBuilder(book.getName())
                .append(" : ").append(getString(R.string.chapter_list_prepend_text))
                .append(" ").append(chapterNumber);
        if (appBar != null) {
            appBar.setTitle(title);
        }
        getActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verse_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_verse_list);
        recyclerView.setAdapter(new AdapterVerseList(VerseList.getEntries(), this));
        return view;
    }
}
