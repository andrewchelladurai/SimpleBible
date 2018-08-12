package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.ui.ops.BooksScreenOps;

import java.util.ArrayList;

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

    public ArrayList<Book> getBooksList() {
        return BookRepository.getInstance().getList();
    }
}
