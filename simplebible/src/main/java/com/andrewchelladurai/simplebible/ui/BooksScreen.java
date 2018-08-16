package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.repository.BookRepository;
import com.andrewchelladurai.simplebible.presenter.BooksScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.BookListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BooksScreenOps;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class BooksScreen
    extends AppCompatActivity
    implements BooksScreenOps {

    private static final String TAG = "BooksScreen";

    private BookListAdapter      mAdapter;
    private BooksScreenPresenter mPresenter;
    private String               mNameTemplate;
    private AutoCompleteTextView mChapterField;
    private AutoCompleteTextView mBookField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        mPresenter = new BooksScreenPresenter(this);
        mAdapter = new BookListAdapter(this);

        mNameTemplate = getString(R.string.item_book_name_template);

        RecyclerView recyclerView = findViewById(R.id.act_books_list);
        recyclerView.setAdapter(mAdapter);

        mBookField = findViewById(R.id.act_books_name);
        mBookField.setOnFocusChangeListener(this);
        mBookField.addTextChangedListener(this);

        mChapterField = findViewById(R.id.act_books_chapter);
        mChapterField.setOnFocusChangeListener(this);

        findViewById(R.id.act_books_button).setOnClickListener(this);

        BookRepository mRepository = ViewModelProviders.of(this).get(BookRepository.class);
        final int bookCount = 66;
        final String firstBook = getString(R.string.first_book);
        final String lastBook = getString(R.string.last_book);

        mRepository.queryDatabase(bookCount, firstBook, lastBook)
                   .observe(this, this);
    }

    private void handleInteractionGoto() {
        final Book book = mPresenter.getBookUsingName(getBookInput());
        if (book == null) {
            showErrorInvalidBookInput();
            return;
        }
        final int chapter = getChapterInput();
        boolean valid = mPresenter.validateChapterForBook(chapter, book);
        if (valid) {
            showChapterActivity(book.getNumber(), chapter);
        } else {
            showErrorInvalidChapterInput();
        }
    }

    public void onListFragmentInteraction(final Book book) {
        showChapterActivity(book.getNumber(), 1);
    }

    public void resetChapterField() {
        mChapterField.setText(null);
        mChapterField.setAdapter(null);
        mChapterField.setError(null);
        resetChapterFieldHint();
    }

    public void resetChapterFieldHint() {
        mChapterField.setHint(null);
    }

    private void showChapterActivity(final int book, final int chapter) {
        resetChapterField();
        resetBookField();
        Intent intent = new Intent(this, ChapterScreen.class);
        intent.putExtra(ChapterScreen.BOOK_NUMBER, book);
        intent.putExtra(ChapterScreen.CHAPTER_NUMBER, chapter);
        startActivity(intent);
    }

    private void resetBookField() {
        mBookField.setText(null);
        mBookField.setError(null);
    }

    private void showErrorInvalidChapterInput() {
        mChapterField.setError(getString(R.string.act_main_chapter_err_invalid));
        mChapterField.requestFocus();
    }

    public void showErrorInvalidBookInput() {
        mBookField.setError(getString(R.string.act_main_book_err_invalid));
        mBookField.requestFocus();
    }

    public void prepareChapterField(int chapters) {
        resetChapterField();
        String[] values = new String[chapters];
        for (int i = 1; i <= chapters; i++) {
            values[i - 1] = String.valueOf(i);
        }
        mChapterField.setAdapter(
            new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, values));
        mChapterField.setHint(
            String.format(getString(R.string.act_main_chapter_hint_with_chapter), chapters));
    }

    public String getBookInput() {
        return mBookField.getText().toString().trim();
    }

    public int getChapterInput() {
        try {
            return Integer.parseInt(mChapterField.getText().toString().trim());
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "invalid chapter, returning default 1");
            return 1;
        }
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
        updateScreen(books);
    }

    private void updateScreen(@NonNull final List<Book> books) {
        if (books == null || books.isEmpty()) {
            Log.e(TAG, "updateScreen: empty list from LiveData");
            // FIXME: 16/8/18 shows a eye-catching error on screen to inform developer
            // FIXME: 16/8/18 hopefully it never shows to anyone
            return;
        }

        final int bookCount = 66;
        final String firstBook = getString(R.string.first_book);
        final String lastBook = getString(R.string.last_book);

        boolean success = mPresenter.populateCache(books, bookCount, firstBook, lastBook);
        if (success) {
            mAdapter.updateList(books, bookCount, firstBook, lastBook);
            mAdapter.notifyDataSetChanged();
            mBookField.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, mPresenter.getAllBookNames()));
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_books_button:
                handleInteractionGoto();
                break;
            default:
                Log.e(TAG, "onClick: unexpected event caught");
        }
    }

    @Override
    public void onFocusChange(final View v, final boolean hasFocus) {
        switch (v.getId()) {
            case R.id.act_books_name:
                Log.d(TAG, "book_field hasFocus [" + hasFocus + "]");
                break;
            case R.id.act_books_chapter:
                if (hasFocus) { /* chapter input field gained focus*/
                    handleInteractionChapterFieldGainFocus();
                } else { /* chapter input field is losing focus*/
                    resetChapterFieldHint();
                }
                break;
            default:
                Log.e(TAG, "onFocusChange: " + getString(R.string.msg_unexpected));
        }
    }

    private void handleInteractionChapterFieldGainFocus() {
        final String bookName = getBookInput();
        final Book book = mPresenter.getBookUsingName(bookName);
        if (book != null) {
            resetChapterField();
            prepareChapterField(book.getChapters());
        } else {
            showErrorInvalidBookInput();
        }
    }

    @Override
    public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1,
                                  final int i2) {
    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int i, final int i1,
                              final int i2) {
    }

    @Override
    public void afterTextChanged(final Editable editable) {
        resetChapterField();
    }

}
