package com.andrewchelladurai.simplebible.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksList {

    public static final List<BookItem> ITEMS = new ArrayList<BookItem>();
    public static final Map<String, BookItem> ITEM_MAP = new HashMap<String, BookItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(BookItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static BookItem createDummyItem(int position) {
        return new BookItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class BookItem {

        public final String id;
        public final String content;
        public final String details;

        public BookItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
