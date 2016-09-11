package com.andrewchelladurai.simplebible.presentation;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.BooksTabOperations;
import com.andrewchelladurai.simplebible.model.BooksList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 09-Sep-2016 @ 1:09 AM
 */
public class BooksListPresenter {

    private static final String TAG = "SB_BLF_Presenter";
    private BooksTabOperations mFragment;

    public BooksListPresenter(BooksTabOperations fragment) {
        mFragment = fragment;
    }

    public void refresh() {
        Log.d(TAG, "refresh() called");
    }

    public boolean init() {
        Log.d(TAG, "init() called");
        String array[] = mFragment.getBookNameChapterCountArray();
        return BooksList.populateBooksList(array);
    }
}
