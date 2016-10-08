package com.andrewchelladurai.simplebible.presentation;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.BooksTabOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.ChapterList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 09-Sep-2016 @ 1:09 AM
 */
public class BooksTabPresenter {

    private static final String TAG = "SB_BLF_Presenter";
    private BooksTabOperations mOperations;

    public BooksTabPresenter(BooksTabOperations operations) {
        mOperations = operations;
    }

    public void refresh() {
        Log.d(TAG, "refresh() called");
    }

    public void init() {
        Log.d(TAG, "init() called");
    }

    public boolean bookItemClicked(BooksList.BookItem item, String prependText) {
        return ChapterList.populateListItems(item.getChapterCount(), prependText);
    }
}
