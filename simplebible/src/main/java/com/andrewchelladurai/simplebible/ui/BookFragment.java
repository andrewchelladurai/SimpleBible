package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.ui.ops.BookFragmentOps;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class BookFragment
    extends Fragment
    implements BookFragmentOps {

    private static final String TAG = "BookFragment";
    private static BookRecyclerViewAdapter mAdapter;

    private BookFragment() {
    }

    public static BookFragment newInstance() {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        if (mAdapter == null) {
            mAdapter = new BookRecyclerViewAdapter(this);
            mAdapter.updateList();
        }

        RecyclerView recyclerView = view.findViewById(R.id.fragment_book_list);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public void onListFragmentInteraction(final Book book) {
        Log.d(TAG, "onListFragmentInteraction called [" + book + "]");
    }
}
