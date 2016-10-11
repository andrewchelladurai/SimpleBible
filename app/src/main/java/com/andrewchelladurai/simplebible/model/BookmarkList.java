package com.andrewchelladurai.simplebible.model;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkList {

    private static final String                    TAG      = "SB_BookmarkList";
    private static final List<BookmarkItem>        ITEMS    = new ArrayList<>();
    private static final Map<String, BookmarkItem> ITEM_MAP = new HashMap<>();

    public static List<BookmarkItem> getItems() {
        Log.d(TAG, "getItems() called");
        if (ITEMS.isEmpty() || ITEMS.size() == 0) {
            refreshList();
        }
        return ITEMS;
    }

    public static void refreshList() {
        Log.d(TAG, "refreshList() called");
        ITEMS.clear();
        ITEM_MAP.clear();

        DBUtilityOperations dbu = DBUtility.getInstance();
        ArrayList<String[]> items = dbu.getAllBookmarks();
        String[] itemParts;
        BookmarkItem item;
        for (int i = 0; i < items.size(); i++) {
            itemParts = items.get(i);
            item = new BookmarkItem(itemParts[0], itemParts[1]);
            ITEMS.add(item);
            ITEM_MAP.put(item.getReferences(), item);
        }
    }

    public static class BookmarkItem {

        private final String mReferences;
        private final String mNote;

        public BookmarkItem(String references, String note) {
            mReferences = references;
            mNote = note;
        }

        @Override
        public String toString() {
            return mReferences + " : " + mNote;
        }

        public String getReferences() {
            return mReferences;
        }

        public String getNote() {
            return mNote;
        }
    }
}
