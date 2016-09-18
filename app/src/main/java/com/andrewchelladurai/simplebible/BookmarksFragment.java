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
import android.widget.Toast;

import com.andrewchelladurai.simplebible.adapter.BookmarkListAdapter;
import com.andrewchelladurai.simplebible.interaction.BookmarksTabOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;
import com.andrewchelladurai.simplebible.presentation.BookmarksTabPresenter;
import com.andrewchelladurai.simplebible.utilities.Constants;

public class BookmarksFragment
        extends Fragment
        implements BookmarksTabOperations {

    private static final String TAG              = "SB_BM_Fragment";
    private static final String ARG_COLUMN_COUNT = "COLUMN_COUNT";
    private              int    mColumnCount     = 1;
    private BookmarksTabPresenter mPresenter;
    private BookmarkListAdapter   mListAdapter;
    private RecyclerView          mList;

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
            mList = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mList.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mList.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
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
    }

    @Override
    public void refresh() {
        Log.d(TAG, "refresh() called");
        mList.invalidate();
        mListAdapter.notifyDataSetChanged();
        mList.requestLayout();
    }

    @Override
    public void bookmarkClicked(BookmarkItem item) {
        Log.d(TAG, "bookmarkClicked() called");
        String returnValue = mPresenter.isBookmarkAlreadyPresentInDatabase(item);
        if (returnValue.equalsIgnoreCase(Constants.ERROR)) {
            Toast.makeText(getContext(), "Invalid Bookmark Entry", Toast.LENGTH_SHORT).show();
            Exception ex = new Exception("Invalid Bookmark Entry" + item);
            Log.w(TAG, "bookmarkClicked: ", ex);
            return;
        }
        Intent intent = new Intent(getContext(), BookmarkActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
//        bundle.putParcelable(Constants.BUNDLE_ARG_BOOKMARK_ITEM, item);

        if (returnValue.equalsIgnoreCase(Constants.PRESENT_IN_DATABASE)) {
//            bundle.putString(Constants.BOOKMARK_MODE, Constants.VIEW);
        } else if (returnValue.equalsIgnoreCase(Constants.ABSENT_IN_DATABASE)) {
//            bundle.putString(Constants.BOOKMARK_MODE, Constants.EDIT);
        } else {
            Log.d(TAG, "bookmarkClicked: " + getString(R.string.how_am_i_here));
        }
        startActivity(intent);
    }

    @Override
    public void deleteButtonClicked(BookmarkItem item) {
        Log.d(TAG, "deleteButtonClicked() called with: " + "item = [" + item + "]");
        boolean status = mPresenter.deleteButtonClicked(item);
        if (status) {
            BookmarksTabPresenter.refreshList();
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
}
