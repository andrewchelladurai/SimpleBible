package com.andrewchelladurai.simplebible.presentation;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Calendar;
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

    public String validateBookInput(String bookInput) {
        if (null == bookInput || bookInput.isEmpty()) {
            Log.d(TAG, "validateBookInput() : null == bookInput || bookInput.isEmpty()");
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

    private String validateChapterInput(String bookInput, String chapterInput) {
        if (null == chapterInput || chapterInput.isEmpty()) {
            mOperations.handleEmptyChapterNumberValidationFailure();
            return Constants.FAILURE;
        }

        int count = getChapterCountForBookName(bookInput);
        int chapter = Integer.parseInt(chapterInput);
        if (chapter < 1 || chapter > count) {
            mOperations.handleIncorrectChapterNumberValidationFailure();
            return Constants.FAILURE;
        }

        return Constants.SUCCESS;
    }

    private String[] getVerseReferenceForToday() {
        Log.d(TAG, "getVerseReferenceForToday() called");
        final String defaultReference = Constants.DEFAULT_REFERENCE;
        // get verse reference to use for today
        int dayOfTheYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        Log.d(TAG, "getVerseReferenceForToday: dayOfTheYear = " + dayOfTheYear);
        String[] array = mOperations.getDailyVerseArray();
        String reference;
        if (array == null) {
            reference = defaultReference;
        } else {
            reference = (dayOfTheYear > array.length) ? defaultReference : array[dayOfTheYear];
        }
        Log.d(TAG, "getVerseReferenceForToday: reference = " + reference);

        // check if the reference is correct
        if (!reference.contains(Constants.DELIMITER_IN_REFERENCE)) {
            // reference does not have delimiter
            reference = defaultReference;
            Log.d(TAG,
                  "getVerseReferenceForToday: reference does not have delimiter, using default");
        }
        array = reference.split(Constants.DELIMITER_IN_REFERENCE);
        if (array.length != 3) {
            // there are not 3 parts to the reference
            reference = defaultReference;
            array = reference.split(Constants.DELIMITER_IN_REFERENCE);
            Log.d(TAG, "getVerseReferenceForToday: reference does not have 3 parts, using default");
        }
        return array;
    }

    public String getVerseContentForToday() {
        Log.d(TAG, "getVerseContentForToday() called");
        String array[] = getVerseReferenceForToday();
        int bookNumber, chapterNumber, verseNumber;
        try {
            bookNumber = Integer.parseInt(array[0]);
            chapterNumber = Integer.parseInt(array[1]);
            verseNumber = Integer.parseInt(array[2]);
        } catch (NumberFormatException npe) {
            // the reference could not be parsed correctly
            String defaultReference = Constants.DEFAULT_REFERENCE;
            array = defaultReference.split(Constants.DELIMITER_IN_REFERENCE);
            bookNumber = Integer.parseInt(array[0]);
            chapterNumber = Integer.parseInt(array[1]);
            verseNumber = Integer.parseInt(array[2]);
            Log.d(TAG, "getVerseContentForToday: NPE when converting reference, using default");
        }

        // now get the verseText for the reference
        DBUtilityOperations dbUtility = DBUtility.getInstance();
        String verseText = dbUtility.getVerseForReference(bookNumber, chapterNumber, verseNumber);

        if (verseText == null) {
            verseText = "No verse found for reference";
            return verseText;
        }

        // Beautify the Verse
        BookItem bookItem = BooksList.getBookItem(bookNumber);
        String bookName = (null == bookItem) ? "" : bookItem.getBookName();
        String formattedText = mOperations.getDailyVerseTemplate();

        return String.format(formattedText, verseText, bookName, chapterNumber, verseNumber);
    }

    public String[] getBookNamesList() {
        List<BookItem> items = BooksList.getListItems();
        int count = items.size();
        if (count == 0) {
            Log.d(TAG, "getBookNamesList: got Zero results, returning null");
            return null;
        }
        Log.d(TAG, "getBookNamesList: got " + count + " results");
        String list[] = new String[count];
        for (int i = 0; i < count; i++) {
            list[i] = items.get(i).getBookName();
        }
        return list;
    }

    private int getChapterCountForBookName(String bookName) {
        BookItem bookItem = getBookItemUsingName(bookName);
        return (bookItem == null) ? 0 : bookItem.getChapterCount();
    }

    public String validateInput(String bookInput, String chapterInput) {
        String returnValue = validateBookInput(bookInput);
        if (returnValue.equalsIgnoreCase(Constants.SUCCESS)) {
            return validateChapterInput(bookInput, chapterInput);
        } else {
            return Constants.FAILURE;
        }
    }

    public BookItem getBookItemUsingName(String bookName) {
        return BooksList.getBookItem(bookName);
    }

    public boolean loadChapterList(BookItem item, String prependText) {
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
            Log.d(TAG, "bookmarkVerseForToday: reference is empty");
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
                Log.d(TAG, "bookmarkVerseForToday: setting ARG_MODE = CREATE");
        }
        Intent intent = new Intent(mOperations.getFragmentContext(), BookmarkActivity.class);
        intent.putExtras(args);
        return intent;
    }
}
