package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.presenter.BooksScreenPresenter;
import com.andrewchelladurai.simplebible.ui.ops.BookFragmentOps;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class BookFragment
    extends Fragment
    implements BookFragmentOps {

    private static final String TAG = "BookFragment";
    private static BookRecyclerViewAdapter sAdapter;
    private static BooksScreenPresenter    sPresenter;

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

        if (sPresenter == null) {
            sPresenter = new BooksScreenPresenter(this);
        }

        if (sAdapter == null) {
            boolean repositoryIsNotValidated = !sPresenter.validateBookRepository(
                getString(R.string.first_book), getString(R.string.last_book));
            if (repositoryIsNotValidated) {
                sPresenter.populateBookRepository();
            }

            sAdapter = new BookRecyclerViewAdapter(this);
            sAdapter.updateList(sPresenter.getBooksList());
        }

        RecyclerView recyclerView = view.findViewById(R.id.fragment_book_list);
        recyclerView.setAdapter(sAdapter);
        sAdapter.notifyDataSetChanged();

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
