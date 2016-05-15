package com.andrewchelladurai.simplebible;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {

    private static final String TAG = "Book";
    public static final List<Details> ENTRIES = new ArrayList<>();
    public static final Map<Integer, Details> ENTRY_MAP = new HashMap<>();

    public static boolean populateBooks(String[] values) {
        if (ENTRIES.size() == 66) {
            Log.i(TAG, "populateBooks: Books already Populated [Size=66]");
            return true;
        }
        Details item;
        for (int i = 0; i < values.length; i++) {
            String[] str = values[i].split(":");
            item = new Details((i + 1), // BOOK NUMBER
                               str[0], // BOOK NAME
                               Integer.parseInt(str[1])); // BOOK CHAPTER COUNT
            ENTRIES.add(item);
            ENTRY_MAP.put(item.number, item);
        }
        Log.i(TAG, "populateBooks: All Books created");
        return true;
    }

    public static String[] getAllBookNamed() {
        String[] names = new String[ENTRY_MAP.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = ENTRY_MAP.get((i + 1)).name;
        }
        return names;
    }

    public static Details getBookDetails(final String pBookName) {
        for (Details d : ENTRIES) {
            if (d.name.equalsIgnoreCase(pBookName)) {
                return d;
            }
        }
        return null;
    }

    public static class Details {
        final int number;
        final String name;
        final int chapterCount;

        public Details(int position, String name, int count) {
            number = position;
            this.name = name;
            chapterCount = count;
        }

        @Override
        public String toString() {
            return number + ":" + name + ":" + chapterCount;
        }
    }
}
