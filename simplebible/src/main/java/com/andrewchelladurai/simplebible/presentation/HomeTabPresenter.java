package com.andrewchelladurai.simplebible.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.andrewchelladurai.simplebible.BookmarkActivity;
import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.interaction.HomeTabOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.BooksList.BookItem;
import com.andrewchelladurai.simplebible.model.ChapterList;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.DBUtility;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.util.List;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:40 AM
 */
public class HomeTabPresenter {

    private static final String TAG = "SB_HF_Presenter";
    private final HomeTabOperations mOperations;

    public HomeTabPresenter(HomeTabOperations operations) {
        mOperations = operations;
    }

    public void init() {
        Log.d(TAG, "init() called");
    }

    public String validateBookInput(@NonNull String bookInput) {
        if (bookInput.isEmpty()) {
            Log.e(TAG, "validateBookInput() : returning FAILURE : bookInput.isEmpty()");
            mOperations.handleEmptyBookNameValidationFailure();
            return Constants.FAILURE;
        }
        int count = getChapterCountForBookName(bookInput);
        if (count < 1) {
            mOperations.handleIncorrectBookNameValidationFailure();
            return Constants.FAILURE;
        }
        String[] list = new String[count];
        for (int i = 0; i < count; i++) {
            list[i] = (i + 1) + "";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                mOperations.getFragmentContext(), android.R.layout.simple_dropdown_item_1line,
                list);
        mOperations.updateChapterAdapter(adapter);

        return Constants.SUCCESS;
    }

    private String validateChapterInput(@NonNull String bookInput,
                                        @NonNull String chapterInput) {
        if (chapterInput.isEmpty()) {
            mOperations.handleEmptyChapterNumberValidationFailure();
            Log.e(TAG, "validateChapterInput: returning null - chapterInput.isEmpty()");
            return Constants.FAILURE;
        }

        int count = getChapterCountForBookName(bookInput);
        int chapter = Integer.parseInt(chapterInput);
        if (chapter < 1 || chapter > count) {
            mOperations.handleIncorrectChapterNumberValidationFailure();
            Log.e(TAG, "validateChapterInput: returning null - chapter number is incorrect");
            return Constants.FAILURE;
        }

        return Constants.SUCCESS;
    }

    private String[] getVerseReferenceForToday() {
        return Utilities.getVerseReferenceForToday();
    }

    public String getVerseContentForToday() {
        Log.d(TAG, "getVerseContentForToday() called");
        return Utilities.getVerseContentForToday(getVerseReferenceForToday(),
                                                 mOperations.getDailyVerseTemplate());
    }

    public String[] getBookNamesList() {
        Log.d(TAG, "getBookNamesList() called");
        List<BookItem> items = BooksList.getListItems();
        int count = items.size();
        if (count == 0) {
            Log.e(TAG, "getBookNamesList: got Zero results, returning null");
            return null;
        }
        Log.d(TAG, "getBookNamesList: got " + count + " results");
        String list[] = new String[count];
        for (int i = 0; i < count; i++) {
            list[i] = items.get(i).getBookName();
        }
        return list;
    }

    private int getChapterCountForBookName(@NonNull String bookName) {
        BookItem bookItem = getBookItemUsingName(bookName);
        return (bookItem == null) ? 0 : bookItem.getChapterCount();
    }

    public String validateInput(@NonNull String bookInput, @NonNull String chapterInput) {
        String returnValue = validateBookInput(bookInput);
        if (returnValue.equalsIgnoreCase(Constants.SUCCESS)) {
            return validateChapterInput(bookInput, chapterInput);
        } else {
            return Constants.FAILURE;
        }
    }

    public BookItem getBookItemUsingName(@NonNull String bookName) {
        return BooksList.getBookItem(bookName);
    }

    public boolean loadChapterList(@NonNull BookItem item, @NonNull String prependText) {
        return ChapterList.populateListItems(item.getChapterCount(), prependText);
    }

    public String getTextToShareDailyVerse() {
        Log.d(TAG, "getTextToShareDailyVerse() called");
        String text = getVerseContentForToday();
        if (text != null && !text.isEmpty()) {
            return text.substring(0, text.lastIndexOf("\n"));
        }
        return null;
    }

    public Intent bookmarkVerseForToday() {
        Log.d(TAG, "bookmarkVerseForToday() called");
        String[] array = getVerseReferenceForToday();
        String reference = Utilities.prepareReferenceString(
                Integer.parseInt(array[0]),
                Integer.parseInt(array[1]),
                Integer.parseInt(array[2]));
        if (reference.isEmpty()) {
            Log.e(TAG, "bookmarkVerseForToday: returning null - reference is empty");
            return null;
        }

        DBUtilityOperations dbu = DBUtility.getInstance();

        boolean referenceExists = dbu.doesBookmarkReferenceExist(reference);
        String bmMode = (referenceExists) ? BookmarkActivityOperations.VIEW
                                          : BookmarkActivityOperations.CREATE;

        Bundle args = new Bundle();
        args.putString(BookmarkActivityOperations.ARG_REFERENCE, reference);
        switch (bmMode) {
            case BookmarkActivityOperations.VIEW:
                args.putString(BookmarkActivityOperations.ARG_MODE,
                               BookmarkActivityOperations.VIEW);
                break;
            case BookmarkActivityOperations.CREATE:
            default:
                args.putString(BookmarkActivityOperations.ARG_MODE,
                               BookmarkActivityOperations.CREATE);
                Log.w(TAG, "bookmarkVerseForToday: setting ARG_MODE = CREATE");
        }
        Intent intent = new Intent(mOperations.getFragmentContext(), BookmarkActivity.class);
        intent.putExtras(args);
        return intent;
    }
}
