package com.andrewchelladurai.simplebible.adapter;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksList {

    private static final String TAG = "SB_BooksList";

    private static final List<BookItem> ITEMS = new ArrayList<>();
    private static final Map<String, BookItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        BookItem item;
        for (int i = 1; i <= COUNT; i++) {
            item = new BookItem(i, "Book Name " + i, (i + 1));
            ITEMS.add(item);
            ITEM_MAP.put(String.valueOf(item.mBookNumber), item);
        }
        Log.d(TAG, "static initializer: Books populated");
    }

    public static List<BookItem> getListItems() throws Exception {
        if (ITEMS.size() != 66) {
            Exception exception = new Exception("ITEMS.size() != 66");
            Log.wtf(TAG, "getListItems: ", exception);
            throw exception;
//            Log.e(TAG, "getListItems: ITEMS.size() != 66");
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
