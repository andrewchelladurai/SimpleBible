package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.presenter.BooksScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.BookListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BooksScreenOps;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class BooksScreen
    extends AppCompatActivity
    implements BooksScreenOps {

    private static final String TAG = "BooksScreen";
    private static BookListAdapter      sAdapter;
    private static BooksScreenPresenter sPresenter;
    private static BookRepository       mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        if (sPresenter == null) {
            sPresenter = new BooksScreenPresenter(this);
        }

        if (sAdapter == null) {
            sAdapter = new BookListAdapter(this);
        }

        if (mRepository == null) {
            mRepository = ViewModelProviders.of(this).get(BookRepository.class);
        }

        RecyclerView recyclerView = findViewById(R.id.act_books_list);
        recyclerView.setAdapter(sAdapter);

        mRepository.getList().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(final List<Book> books) {
                sAdapter.updateList(books);
                sAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onListFragmentInteraction(final Book book) {
        Log.d(TAG, "onListFragmentInteraction called [" + book + "]");
    }

    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }
}
