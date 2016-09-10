package com.andrewchelladurai.simplebible.adapter;

import android.util.Log;

import com.andrewchelladurai.simplebible.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksList {

    private static final String TAG = "SB_BooksList";

    private static final List<BookItem> ITEMS = new ArrayList<>();
    private static final Map<String, BookItem> ITEM_MAP = new HashMap<>();

    public static boolean clearList() {
        ITEMS.clear();
        ITEM_MAP.clear();
        return true;
    }

    public static int getListCount() {
        return ITEMS.size();
    }

    public static boolean populateBooksList(String[] array) {
        Log.d(TAG, "populateBooksList called");
        if (getListCount() == 66) {
            Log.d(TAG, "populateBooksList: returning : ITEMS.size() == 66");
            return false;
        }
        if (null == array || array.length == 0 || array.length > 66) {
            Log.d(TAG, "populateBooksList: returning : null == array || array.length == 0");
            return false;
        }
        BookItem item;
        String parts[], bookName;
        int bookNumber = 0, chapterCount;
        for (int i = 0; i < array.length; i++) {
            parts = array[i].split(Constants.DELIMITER_IN_REFERENCE);
            if (parts.length != 2) {
                Log.d(TAG, "populateBooksList: skipping entry " + array[i]);
                continue;
            }

            bookNumber = i + 1;
            bookName = parts[0];
            chapterCount = Integer.parseInt(parts[1]);

            item = new BookItem(bookNumber, bookName, chapterCount);
            ITEMS.add(item);
            ITEM_MAP.put(String.valueOf(item.mBookNumber), item);
        }
        Log.d(TAG, "populateBooksList returned: " + bookNumber + " Books populated");
        return true;
    }

    public static List<BookItem> getListItems() {
        if (ITEMS.size() != 66) {
            Log.e(TAG, "getListItems: ITEMS.size() != 66");
            Exception exception = new Exception("ITEMS.size() != 66");
            Log.wtf(TAG, "getListItems: ", exception);
        }
        return ITEMS;
    }

    public static class BookItem {

        private final int mBookNumber;
        private final int mChapterCount;
        private final String mBookName;

        public BookItem(int position, String name, int count) {
            mBookNumber = position;
            mBookName = name;
            mChapterCount = count;
        }

        public int getBookNumber() {
            return mBookNumber;
        }

        public int getChapterCount() {
            return mChapterCount;
        }

        public String getBookName() {
            return mBookName;
        }

        @Override
        public String toString() {
            return mBookNumber + " : " + mBookName;
        }
    }
}
