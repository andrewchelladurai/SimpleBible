package com.andrewchelladurai.simplebible.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkList {

    private static final List<BookmarkItem>        ITEMS    = new ArrayList<>();
    private static final Map<String, BookmarkItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            BookmarkItem item = new BookmarkItem(String.valueOf(i), "Item " + i);
            ITEMS.add(item);
            ITEM_MAP.put(item.getReferences(), item);
        }
    }

    public static List<BookmarkItem> getItems() {
        return ITEMS;
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
