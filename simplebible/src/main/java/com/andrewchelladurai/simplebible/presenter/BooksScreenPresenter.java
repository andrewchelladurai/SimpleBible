package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.ui.ops.BooksScreenOps;

import java.util.ArrayList;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 9:53 PM.
 */
public class BooksScreenPresenter {

    private static final String TAG = "BooksScreenPresenter";
    private final BooksScreenOps mOps;

    public BooksScreenPresenter(final BooksScreenOps ops) {
        mOps = ops;
    }

    @NonNull
    public ArrayList<Book> getBooksList() {
        return BookRepository.getInstance().getCachedList();
    }

    @Nullable
    public Book getBookUsingName(@NonNull final String bookName) {
        return BookRepository.getInstance().getCachedRecordUsingValue(bookName);
    }

    public boolean validateChapterForBook(@IntRange(from = 1) final int chapter,
                                          @NonNull final Book book) {
        return (chapter >= 1 && chapter <= book.getChapters());
    }

    public boolean validateRepository(@NonNull final String firstBook,
                                      @NonNull final String lastBook) {
        return BookRepository.getInstance().isCacheValid(firstBook, lastBook);
    }

    @NonNull
    public ArrayList<String> getAllBookNames() {
        final ArrayList<Book> list = BookRepository.getInstance().getCachedList();
        final ArrayList<String> names = new ArrayList<>();
        for (final Book book : list) {
            names.add(book.getName());
        }
        return names;
    }
}
