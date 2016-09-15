package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.BookmarkListAdapter;
import com.andrewchelladurai.simplebible.interaction.BookmarksTabOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;
import com.andrewchelladurai.simplebible.presentation.BookmarksTabPresenter;

public class BookmarksFragment
        extends Fragment
        implements BookmarksTabOperations {

    private static final String TAG              = "SB_BM_Fragment";
    private static final String ARG_COLUMN_COUNT = "COLUMN_COUNT";
    private              int    mColumnCount     = 1;
    private BookmarksTabPresenter mPresenter;

    public BookmarksFragment() {
    }

    public static BookmarksFragment newInstance(int columnCount) {
        BookmarksFragment fragment = new BookmarksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new BookmarkListAdapter(BookmarkList.getItems(), this));
        }
        init();
        return view;
    }

    @Override public void init() {
        if (mPresenter == null) {
            mPresenter = new BookmarksTabPresenter(this);
        }
    }

    @Override public void refresh() {

    }

    @Override public void bookmarkClicked(BookmarkItem item) {
        Log.d(TAG, "bookmarkClicked() called with: " + "item = [" + item + "]");
        mPresenter.bookmarkClicked(item);
    }

    @Override public void deleteButtonClicked(BookmarkItem item) {
        Log.d(TAG, "deleteButtonClicked() called with: " + "item = [" + item + "]");
        mPresenter.deleteButtonClicked(item);
    }

    @Override public void shareButtonClicked(BookmarkItem item) {
        Log.d(TAG, "shareButtonClicked() called with: " + "item = [" + item + "]");
        mPresenter.shareButtonClicked(item);
    }

    @Override public String getDeleteButtonLabel() {
        return getString(R.string.bookmark_item_label_delete);
    }

    @Override public String getShareButtonLabel() {
        return getString(R.string.bookmark_item_label_share);
    }
}
