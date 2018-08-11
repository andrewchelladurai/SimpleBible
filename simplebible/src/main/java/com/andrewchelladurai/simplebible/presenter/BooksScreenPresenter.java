package com.andrewchelladurai.simplebible.presenter;

import android.content.Context;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.ui.ops.BookFragmentOps;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 9:53 PM.
 */
public class BooksScreenPresenter {

    private static final String TAG = "BooksScreenPresenter";
    private final BookFragmentOps mOps;
    private boolean isRepoPopulateRunning = false;

    public BooksScreenPresenter(final BookFragmentOps ops) {
        mOps = ops;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean populateBookRepository() {
        final StringBuilder successful = new StringBuilder();
        synchronized (BooksScreenPresenter.class) {
            isRepoPopulateRunning = true;
            final Context context = mOps.getSystemContext();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Book> allBooks = SbDatabase.getInstance(context).getBookDao()
                                                    .getAllBooks();
                    boolean populated = BookRepository.getInstance().populate(allBooks);
                    successful.append(populated);
                    Log.d(TAG, "run: " + successful);
                }
            }).start();
            isRepoPopulateRunning = false;
        }
        return successful.toString().equalsIgnoreCase("true");
    }

    @NonNull
    public List<Book> getBooksList() {
        return BookRepository.getInstance().getList();
    }

    public boolean validateBookRepository(@NonNull final String firstBook,
                                          @NonNull final String lastBook) {
        return BookRepository.getInstance().validate(firstBook, lastBook);
    }

    public boolean isRepoPopulateRunning() {
        return isRepoPopulateRunning;
    }
}
