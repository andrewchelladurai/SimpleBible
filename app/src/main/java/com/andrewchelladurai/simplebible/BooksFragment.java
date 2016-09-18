package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andrewchelladurai.simplebible.adapter.BooksListAdapter;
import com.andrewchelladurai.simplebible.interaction.BooksTabOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.presentation.BooksTabPresenter;

import java.util.List;

public class BooksFragment
        extends Fragment
        implements BooksTabOperations {

    private static final String TAG              = "SB_BLFragment";
    private static final String ARG_COLUMN_COUNT = "COLUMN_COUNT";
    private static BooksTabPresenter mPresenter;
    private int mColumnCount = 2;

    public BooksFragment() {
    }

    public static BooksFragment newInstance(int columnCount) {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init() {
        Log.d(TAG, "init() called");
        mPresenter = new BooksTabPresenter(this);
        boolean value = mPresenter.init();
        Log.d(TAG, "init: " + value);
    }

    @Override
    public void refresh() {
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
        init();
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            // Try to create the adapter using the list
            List<BooksList.BookItem> items = BooksList.getListItems();
            if (items.size() != 66) {
                Toast.makeText(getContext(), "BooksList could not be populated",
                               Toast.LENGTH_SHORT).show();
            } else {
                recyclerView.setAdapter(new BooksListAdapter(items, this));
            }

            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        return view;
    }

    public void bookItemClicked(BooksList.BookItem item) {
        Log.d(TAG, "bookItemClicked() called for item [" + item.getBookName() + "]");
        Intent intent = new Intent(getContext(), ChapterListActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public String getBookNameTemplateString() {
        String value = getString(R.string.fragment_books_book_name_template);
        if (value == null) {
            return "%s";
        }
        return value;
    }

    @Override
    public String chapterCountTemplateString() {
        String value = getString(R.string.fragment_books_chapter_count_template);
        if (value == null) {
            return "%d";
        }
        return value;
    }

    /**
     * This will return the resource array books_n_chapter_count_array The format of the items must
     * be like this : Book_Name:Chapter_Count Example Genesis:50
     *
     * @return String array
     */
    @Override
    public String[] getBookNameChapterCountArray() {
        String array[] = getResources().getStringArray(R.array.books_n_chapter_count_array);
        if (null != array && array.length > 0) {
            return array;
        }
        return new String[0];
    }
}
