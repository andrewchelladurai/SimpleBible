package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.presenter.BooksScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.BookListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BooksScreenOps;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class BooksScreen
    extends AppCompatActivity
    implements BooksScreenOps {

    private static final String TAG = "BooksScreen";
    private static BookListAdapter               sAdapter;
    private static BooksScreenPresenter          sPresenter;
    private static BookRepository                mRepository;
    private        String                        mNameTemplate;
    private        AppCompatAutoCompleteTextView mChapterField;
    private        AppCompatAutoCompleteTextView mBookField;

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

        mNameTemplate = getString(R.string.item_book_name_template);

        RecyclerView recyclerView = findViewById(R.id.act_books_list);
        recyclerView.setAdapter(sAdapter);

        mBookField = findViewById(R.id.act_books_name);
        mChapterField = findViewById(R.id.act_books_chapter);

        mRepository.getLiveData().observe(this, this);
    }

    public void onListFragmentInteraction(final Book book) {
        Log.d(TAG, "onListFragmentInteraction called [" + book + "]");
    }

    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }

    @Override
    public String getFormattedBookListHeader(final Book book) {
        return String.format(mNameTemplate, book.getName());
    }

    @Override
    public String getFormattedBookListDetails(final Book book) {
        final int chapters = book.getChapters();
        return getResources().getQuantityString(
            R.plurals.item_book_details_template, chapters, chapters);
    }

    @Override
    public void onChanged(final List<Book> books) {
        // populate cache in repository
        mRepository.populate(books);

        // get cached list from repository
        final ArrayList<Book> list = sPresenter.getBooksList();

        // update list in adapter for recycler view
        sAdapter.updateList(list);
        sAdapter.notifyDataSetChanged();

        // extract book names from cached list for lookup in
        // auto-complete text field
        final ArrayList<String> names = new ArrayList<>();
        for (final Book book : list) {
            names.add(book.getName());
        }
        mBookField.setAdapter(new ArrayAdapter<>(this,
                                                 android.R.layout.simple_list_item_1,
                                                 names));
    }
}
