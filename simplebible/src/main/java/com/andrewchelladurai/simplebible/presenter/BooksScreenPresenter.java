package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.ui.ops.BookFragmentOps;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 9:53 PM.
 */
public class BooksScreenPresenter {

    private static final String TAG = "BooksScreenPresenter";
    private final BookFragmentOps mOps;

    public BooksScreenPresenter(final BookFragmentOps ops) {
        mOps = ops;
    }
}
