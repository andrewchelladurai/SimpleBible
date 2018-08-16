package com.andrewchelladurai.simplebible.presenter;

import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.repository.BookRepository;
import com.andrewchelladurai.simplebible.ui.ops.BooksScreenOps;

import java.util.ArrayList;
import java.util.List;

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

    @Nullable
    public Book getBookUsingName(@NonNull final String bookName) {
        return (Book) BookRepository.getInstance().getCachedRecordUsingValue(bookName);
    }

    public boolean validateChapterForBook(@IntRange(from = 1) final int chapter,
                                          @NonNull final Book book) {
        return (chapter > 0 && chapter <= book.getChapters());
    }

    public boolean populateCache(@NonNull final List<Book> list,
                                 @IntRange(from = 1, to = 66) final int count,
                                 @NonNull final String firstBook,
                                 @NonNull final String lastBook) {
        if (list == null
            || list.isEmpty()
            || count < 0
            || count > 66
            || firstBook == null
            || firstBook.isEmpty()
            || lastBook == null
            || lastBook.isEmpty()) {
            Log.e(TAG, "populateCache: invalid params passed");
            return false;
        }

        return BookRepository.getInstance().populateCache(list, count, firstBook, lastBook);
    }

    @NonNull
    public ArrayList<String> getAllBookNames() {
        final List<?> list = BookRepository.getInstance().getCachedList();

        if (list == null || list.isEmpty()) {
            Log.e(TAG, "getAllBookNames: empty list from repository");
            return null;
        }

        final ArrayList<String> names = new ArrayList<>();
        for (final Object object : list) {
            names.add(((Book) object).getName());
        }

        return names;
    }
}
