package com.andrewchelladurai.simplebible.presentation;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.BooksListFragmentInterface;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 09-Sep-2016 @ 1:09 AM
 */
public class BooksListFragmentPresenter {

    private static final String TAG = "SB_BLF_Presenter";
    private BooksListFragmentInterface mFragment;

    public BooksListFragmentPresenter(BooksListFragmentInterface fragment) {
        mFragment = fragment;
    }

    public void refresh() {
        Log.d(TAG, "refresh() called");
    }

    public void init() {
        Log.d(TAG, "init() called");
    }
}
