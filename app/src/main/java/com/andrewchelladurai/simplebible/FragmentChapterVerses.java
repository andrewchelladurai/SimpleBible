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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FragmentChapterVerses
        extends Fragment
        implements View.OnClickListener {

    private static final String TAG = "SB_FragChapterVerses";
    private ListBooks.Entry   mBook;
    private ListChapter.Entry mChapter;

    public FragmentChapterVerses() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CollapsingToolbarLayout appBar = (CollapsingToolbarLayout) getActivity()
                .findViewById(R.id.activity_chapter_detail_toolbar_layout);

        mBook = getArguments().getParcelable(Utilities.CURRENT_BOOK);
        if (mBook == null) {
            Utilities.throwError(TAG + " onCreate : mBook == null");
        }
        mChapter = getArguments().getParcelable(Utilities.CURRENT_CHAPTER);
        if (mChapter == null) {
            Utilities.throwError(TAG + " onCreate : mChapter == null");
        }
        String chapterNumber = mChapter.getChapterNumber();
        if (chapterNumber == null) {
            Utilities.throwError(TAG + " onCreate : chapterNumber == null");
        }
        StringBuilder title = new StringBuilder(mBook.getName())
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

        DatabaseUtility dbu = DatabaseUtility.getInstance(getContext());
        int bookNumber = Integer.parseInt(mBook.getBookNumber());
        int chapterNumber = Integer.parseInt(mChapter.getChapterNumber());
        ArrayList<String> verseList = dbu.getAllVersesOfChapter(bookNumber, chapterNumber);
        if (verseList == null || verseList.size() < 1) {
            Utilities.throwError(TAG + " onCreateView : verseList == null || size() < 1");
        }

        ListVerse.populateEntries(verseList, bookNumber, chapterNumber);
        recyclerView.setAdapter(new AdapterVerseList(ListVerse.getEntries(), this));

        FloatingActionButton button =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_chapter_fab_save);
        button.setOnClickListener(this);

        button = (FloatingActionButton) getActivity().findViewById(R.id.activity_chapter_fab_share);
        button.setOnClickListener(this);

        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ListVerse.clearEntries();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof FloatingActionButton) {
            if (ListVerse.isSelectedEntriesEmpty()) {
                Log.d(TAG, "onClick: isSelectedEntriesEmpty = true, but button was clicked");
                return;
            }
            switch (v.getId()) {
                case R.id.activity_chapter_fab_save:
                    buttonSaveClicked();
                    break;
                case R.id.activity_chapter_fab_share:
                    buttonShareClicked();
                    break;
                default:
                    Utilities.throwError(TAG + " onClick() unknown Button ID" + v.getId());
            }
        }
    }

    public void buttonSaveClicked() {
        Log.d(TAG, "buttonSaveClicked() called");

        ArrayList<ListVerse.Entry> entries = ListVerse.getSelectedEntries();
        if (entries.isEmpty()) {
            Log.d(TAG, "buttonShareClicked: but no selected entries exist");
            return;
        }
        ArrayList<String> references = new ArrayList<>();
        for (ListVerse.Entry entry : entries) {
            references.add(entry.getReference());
        }

        getArguments().putStringArrayList(Utilities.REFERENCES, references);
        getArguments().putString(Utilities.BOOKMARK_MODE, Utilities.BOOKMARK_VIEW);

        Intent intent = new Intent(getContext(), ActivityBookmark.class);
        intent.putExtras(getArguments());
        startActivity(intent);
    }

    public void buttonShareClicked() {
        Log.d(TAG, "buttonShareClicked() called");
        ArrayList<ListVerse.Entry> entries = ListVerse.getSelectedEntries();
        if (entries.isEmpty()) {
            Log.d(TAG, "buttonShareClicked: but no selected entries exist");
            return;
        }
        StringBuilder shareText = new StringBuilder();
        String text;
        for (ListVerse.Entry entry : entries) {
            text = mBook.getName() + " (" +
                   mChapter.getChapterNumber() + ":" +
                   entry.getVerseNumber() + ") " +
                   entry.getVerseText() + "\n";
            shareText.append(text);
        }
        shareText.append(getString(R.string.share_append_text));
        startActivity(Utilities.shareVerse(shareText.toString()));
    }

    public void showActionBar() {
        LinearLayout view = (LinearLayout) getActivity().findViewById(R.id.verse_actions);
        view.setVisibility(ListVerse.isSelectedEntriesEmpty() ? View.GONE : View.VISIBLE);
    }
}
