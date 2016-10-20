package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.BookmarkListAdapter;
import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.interaction.BookmarksTabOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;
import com.andrewchelladurai.simplebible.presentation.BookmarksTabPresenter;

public class BookmarksFragment
        extends Fragment
        implements BookmarksTabOperations {

    private static final String TAG          = "SB_BM_Fragment";
    //    private static final String ARG_COLUMN_COUNT = "COLUMN_COUNT";
    private              int    mColumnCount = 1;
    private BookmarksTabPresenter mPresenter;
    private BookmarkListAdapter   mListAdapter;
    private RecyclerView          mList;

    public BookmarksFragment() {
    }

    public static BookmarksFragment newInstance() {
        BookmarksFragment fragment = new BookmarksFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BookmarkList.refreshList();
        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mList = (RecyclerView) view;
            mList.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            mListAdapter = new BookmarkListAdapter(BookmarkList.getItems(), this);
            mList.setAdapter(mListAdapter);
        }
        init();
        return view;
    }

    @Override
    public void init() {
        if (mPresenter == null) {
            mPresenter = new BookmarksTabPresenter(this);
        }
        mColumnCount = getResources().getInteger(R.integer.column_count_bookmarks_list);
    }

    @Override
    public void refresh() {
        Log.d(TAG, "refresh() called");
        BookmarkList.refreshList();
        mListAdapter.notifyDataSetChanged();
//        mList.invalidate();
        mList.requestLayout();
    }

    @Override
    public void bookmarkClicked(@NonNull BookmarkItem item) {
        Log.d(TAG, "bookmarkClicked() called");
        boolean bookmarkExists = mPresenter.bookmarkClicked(item.getReferences());

        Bundle args = new Bundle();
        args.putString(BookmarkActivityOperations.ARG_REFERENCE, item.getReferences());
        String mode = (bookmarkExists) ? BookmarkActivityOperations.VIEW
                                       : BookmarkActivityOperations.CREATE;
        args.putString(BookmarkActivityOperations.ARG_MODE, mode);

        Intent intent = new Intent(getContext(), BookmarkActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void deleteButtonClicked(@NonNull BookmarkItem item) {
        boolean status = mPresenter.deleteButtonClicked(item);
        if (status) {
            refresh();
        }
    }

    @Override
    public void shareButtonClicked(BookmarkItem item) {
        Log.d(TAG, "shareButtonClicked() called with: " + "item = [" + item + "]");
        mPresenter.shareButtonClicked(item);
    }

    @Override
    public String getDeleteButtonLabel() {
        return getString(R.string.fragment_bookmark_button_label_delete);
    }

    @Override
    public String getShareButtonLabel() {
        return getString(R.string.fragment_bookmark_button_label_share);
    }

    @Override public String getResourceString(int stringReference) {
        return getString(stringReference);
    }

    @Override public String getBookmarkReferenceText(int count) {
        return getResources().getQuantityString(
                R.plurals.fragment_bookmark_template_reference, count);
    }
}
