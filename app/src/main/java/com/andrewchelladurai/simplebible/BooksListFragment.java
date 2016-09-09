package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.adapter.BooksListAdapter;
import com.andrewchelladurai.simplebible.adapter.BooksList;
import com.andrewchelladurai.simplebible.adapter.BooksList.BookItem;
import com.andrewchelladurai.simplebible.interaction.BooksListFragmentInterface;
import com.andrewchelladurai.simplebible.presentation.BooksListFragmentPresenter;

public class BooksListFragment
        extends Fragment
        implements BooksListFragmentInterface {

    private static final String ARG_COLUMN_COUNT = "COLUMN_COUNT";
    private static BooksListFragmentPresenter mPresenter;
    private int mColumnCount = 2;
    private BooksListFragment mListener;

    public BooksListFragment() {
    }

    public static BooksListFragment newInstance(int columnCount) {
        BooksListFragment fragment = new BooksListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        fragment.init();
        return fragment;
    }

    @Override public void init() {
        mPresenter = new BooksListFragmentPresenter(this);
        mPresenter.init();
    }

    @Override public void refresh() {
        mPresenter.refresh();
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
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new BooksListAdapter(BooksList.ITEMS, mListener));
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void handleInteraction(BookItem item) {

    }
}
