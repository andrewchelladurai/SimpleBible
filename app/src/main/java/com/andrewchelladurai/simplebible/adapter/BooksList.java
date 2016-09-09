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
        ITEM_MAP.put(item.number, item);
    }

    private static BookItem createDummyItem(int position) {
        return new BookItem(String.valueOf(position), "Book Name " + position);
    }

    public static class BookItem {

        public final String number;
        public final String name;

        public BookItem(String number, String name) {
            this.number = number;
            this.name = name;
        }

        @Override
        public String toString() {
            return number + " : " + name;
        }
    }
}
