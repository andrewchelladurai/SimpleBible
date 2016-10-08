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
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.VerseListAdapter;
import com.andrewchelladurai.simplebible.interaction.ChapterFragmentOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.ChapterList;
import com.andrewchelladurai.simplebible.model.ChapterList.ChapterItem;
import com.andrewchelladurai.simplebible.model.VerseList;
import com.andrewchelladurai.simplebible.model.VerseList.VerseItem;
import com.andrewchelladurai.simplebible.presentation.ChapterFragmentPresenter;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.util.List;

import static com.andrewchelladurai.simplebible.R.id.chapter_detail_verse_actions_bar;

public class ChapterFragment
        extends Fragment
        implements ChapterFragmentOperations, View.OnClickListener {

    public static final  String  ARG_BOOK_ITEM      = "ARG_BOOK_ITEM";
    public static final  String  ARG_CHAPTER_NUMBER = "ARG_CHAPTER_NUMBER";
    private static final String  TAG                = "SB_ChapterFragment";
    private              boolean isAllSet           = false;
    private        ChapterItem              mChapterItem;
    private        BooksList.BookItem       mBookItem;
    private        VerseListAdapter         mListAdapter;
    private        LinearLayoutCompat       mActions;
    private static ChapterFragmentPresenter mPresenter;
    private        FloatingActionButton     mBookmarkButton;
    private        FloatingActionButton     mShareButton;

    public ChapterFragment() {
    }

    public static ChapterFragment newInstance() {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        init();
        if (isAllSet) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                    activity.findViewById(R.id.chapter_detail_toolbar_layout);
            String title = mBookItem.getBookName() + " : " + mChapterItem.getLabel();
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
            getActivity().setTitle(title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);

        mBookmarkButton = (FloatingActionButton) getActivity().findViewById(
                R.id.chapter_detail_verse_action_bookmark);
        mBookmarkButton.setOnClickListener(this);
        mShareButton = (FloatingActionButton) getActivity().findViewById(
                R.id.chapter_detail_verse_action_share);
        mShareButton.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_verse_list);
        recyclerView.setAdapter(mListAdapter);
        refresh();
        return view;
    }

    @Override public void init() {
        // prepare to set all these values
        isAllSet = false;
        mBookItem = null;
        mChapterItem = null;
        if (null == mPresenter) {
            mPresenter = new ChapterFragmentPresenter(this);
        }
        mPresenter.init();

        Bundle args = getArguments();

        if (args.containsKey(ARG_BOOK_ITEM)) { // Book Item was passed
            mBookItem = args.getParcelable(ARG_BOOK_ITEM);
            // required for display purposes
            String prependText = getString(R.string.chapter);
            // populate List based on Book Item
            isAllSet = ChapterList.populateListItems(mBookItem.getChapterCount(), prependText);
            // get Chapter Item
            int chapterNumber = (isAllSet && args.containsKey(ARG_CHAPTER_NUMBER))
                                ? args.getInt(ARG_CHAPTER_NUMBER) : 1;
            mChapterItem = ChapterList.getChapterItem(chapterNumber);
        }
        List<VerseItem> list = mPresenter.getAllVersesForChapter(mBookItem, mChapterItem);
        if (list != null) {
            mListAdapter = new VerseListAdapter(list, this);
        }

        isAllSet = (mChapterItem != null & mBookItem != null & list != null);
    }

    @Override public void refresh() {
        toggleActionBar(VerseList.isSelectedItemsEmpty());
    }

    @Override public int getReferenceHighlightColor() {
        return ContextCompat.getColor(getContext(), R.color.reference_highlight_color);
    }

    @Override public int getDefaultBackgroundColor() {
        return ContextCompat.getColor(getContext(), R.color.cardBackground);
    }

    @Override public int getLongClickBackgroundColor() {
        return ContextCompat.getColor(getContext(), R.color.long_press_background);
    }

    @Override public int getLongClickTextColor() {
        return ContextCompat.getColor(getContext(), R.color.long_press_text_color);
    }

    @Override public int getDefaultTextColor() {
        return ContextCompat.getColor(getContext(), R.color.textColor);
    }

    @Override public void toggleActionBar(boolean isSelectedItemsEmpty) {
        if (mActions == null) {
            mActions = (LinearLayoutCompat)
                    getActivity().findViewById(chapter_detail_verse_actions_bar);
        }
        if (mActions == null) {
            Log.w(TAG, "toggleActionBar: component chapter_detail_verse_actions_bar not found");
            return;
        }
        if (isSelectedItemsEmpty) {
            mActions.setVisibility(View.GONE);
        } else {
            mActions.setVisibility(View.VISIBLE);
        }

    }

    @Override public String getShareTemplate() {
        return getString(R.string.share_verse_template);
    }

    @Override public void shareSelectedVerses(String stringToShare) {
        startActivity(Utilities.shareVerse(stringToShare));
    }

    @Override public void onClick(View v) {
        if (v.equals(mBookmarkButton)) {
            mPresenter.bookmarkButtonClicked();
        } else if (v.equals(mShareButton)) {
            mPresenter.shareButtonClicked();
        }
    }
}
