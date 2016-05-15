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

import com.andrewchelladurai.simplebible.Book.Details;

public class BookListFragment
        extends Fragment {

    private static final String TAG = "BookListFragment";

    public BookListFragment() {
    }

    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        final int count = Utilities.getInstance(null).getBooksColumnCount();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (count <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, count));
            }
            recyclerView.setAdapter(new BookListAdapter(Book.ENTRIES, this));
        }
        return view;
    }

    public void bookItemClicked(final Details pItem) {
        Log.i(TAG, "bookItemClicked: " + pItem.toString());
    }
}
