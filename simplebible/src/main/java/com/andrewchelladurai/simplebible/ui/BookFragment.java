package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.presenter.BooksScreenPresenter;
import com.andrewchelladurai.simplebible.ui.ops.BookFragmentOps;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class BookFragment
    extends Fragment
    implements BookFragmentOps {

    private static final String TAG = "BookFragment";
    private static BookRecyclerViewAdapter sAdapter;
    private static BooksScreenPresenter    sPresenter;
    private static BookRepository          mRepository;

    public BookFragment() {
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

        if (sPresenter == null) {
            sPresenter = new BooksScreenPresenter(this);
        }

        if (sAdapter == null) {
            sAdapter = new BookRecyclerViewAdapter(this);
        }

        if (mRepository == null) {
            mRepository = ViewModelProviders.of(this).get(BookRepository.class);
        }

        RecyclerView recyclerView = view.findViewById(R.id.fragment_book_list);
        recyclerView.setAdapter(sAdapter);

        mRepository.getList().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(final List<Book> books) {
                sAdapter.updateList(books);
                sAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public void onListFragmentInteraction(final Book book) {
        Log.d(TAG, "onListFragmentInteraction called [" + book + "]");
    }

    @Override
    public Context getSystemContext() {
        return getContext();
    }
}
