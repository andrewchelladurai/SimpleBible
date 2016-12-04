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
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.VerseListAdapter;
import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.interaction.ChapterFragmentOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.ChapterList;
import com.andrewchelladurai.simplebible.model.ChapterList.ChapterItem;
import com.andrewchelladurai.simplebible.model.VerseList;
import com.andrewchelladurai.simplebible.model.VerseList.VerseItem;
import com.andrewchelladurai.simplebible.presentation.ChapterFragmentPresenter;
import com.andrewchelladurai.simplebible.utilities.Utilities;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.Collection;
import java.util.List;

import static com.andrewchelladurai.simplebible.R.id.chapter_detail_verse_actions_bar;

public class ChapterFragment
        extends Fragment
        implements ChapterFragmentOperations, View.OnClickListener {

    public static final  String ARG_BOOK_ITEM      = "ARG_BOOK_ITEM";
    public static final  String ARG_CHAPTER_NUMBER = "ARG_CHAPTER_NUMBER";
    private static final String TAG                = "SB_ChapterFragment";
    private static ChapterFragmentPresenter mPresenter;
    private boolean isAllSet = false;
    private ChapterItem          mChapterItem;
    private BooksList.BookItem   mBookItem;
    private VerseListAdapter     mListAdapter;
    private FloatingActionMenu   mActions;
    private FloatingActionButton fabShare, fabBookmark, fabReset;

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
        if (isAllSet) {
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                    getActivity().findViewById(R.id.chapter_detail_toolbar_layout);
            if (appBarLayout != null) {
                String title = mBookItem.getBookName() + " : " + mChapterItem.getLabel();
                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);

        Activity activity = getActivity();

        mActions = (FloatingActionMenu)
                activity.findViewById(chapter_detail_verse_actions_bar);
//        mActions.setClosedOnTouchOutside(true);
        fabShare = (FloatingActionButton) activity
                .findViewById(R.id.chapter_detail_verse_action_share);
        fabShare.setOnClickListener(this);

        fabBookmark = (FloatingActionButton) activity
                .findViewById(R.id.chapter_detail_verse_action_bookmark);
        fabBookmark.setOnClickListener(this);

        fabReset = (FloatingActionButton) activity
                .findViewById(R.id.chapter_detail_verse_action_reset);
        fabReset.setOnClickListener(this);


        init();

        if (isAllSet) {
            String title = mBookItem.getBookName() + " : " + mChapterItem.getLabel();
            activity.setTitle(title);
        }

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
            mPresenter.init();
        }

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
        mListAdapter.notifyDataSetChanged();
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
            mActions = (FloatingActionMenu)
                    getActivity().findViewById(chapter_detail_verse_actions_bar);
        }
        if (mActions == null) {
            Log.e(TAG, "toggleActionBar: component chapter_detail_verse_actions_bar not found");
            return;
        }
        if (isSelectedItemsEmpty) {
            mActions.hideMenu(true);
            mActions.close(true);
            //mActions.setVisibility(View.GONE);
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
        if (v.equals(fabBookmark)) {
            bookmarkButtonClicked();
        } else if (v.equals(fabShare)) {
            mPresenter.shareButtonClicked();
        } else if (v.equals(fabReset)) {
            boolean cleared = mPresenter.resetButtonClicked();
            if (cleared) {
                refresh();
            }
        }
    }

    private void bookmarkButtonClicked() {
        Collection<VerseItem> items = VerseList.getSelectedItems();
        if (items.isEmpty()) {
            Log.e(TAG, "bookmarkButtonClicked: returning - No Selected Entries :\n"
                       + getString(R.string.how_am_i_here));
            return;
        }
        String reference = Utilities.prepareBookmarkReferenceFromVerseList(items);
        if (reference.isEmpty()) {
            Log.e(TAG, "bookmarkButtonClicked: returning - reference is empty");
            return;
        }
        String returnValue = mPresenter.bookmarkButtonClicked(reference);
        Bundle args = new Bundle();
        args.putString(BookmarkActivityOperations.ARG_REFERENCE, reference);
        switch (returnValue) {
            case BookmarkActivityOperations.VIEW:
                args.putString(BookmarkActivityOperations.ARG_MODE,
                               BookmarkActivityOperations.VIEW);
                break;
            case BookmarkActivityOperations.CREATE:
            default:
                args.putString(BookmarkActivityOperations.ARG_MODE,
                               BookmarkActivityOperations.CREATE);
                Log.w(TAG, "bookmarkButtonClicked: " + getString(R.string.how_am_i_here));
                Log.w(TAG, "bookmarkButtonClicked: setting ARG_MODE = CREATE");
        }
        Intent intent = new Intent(getContext(), BookmarkActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }
}
