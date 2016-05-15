package com.andrewchelladurai.simplebible;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {

    private static final String TAG = "Book";
    private static final List<Details> ENTRIES = new ArrayList<>();
    private static final Map<Integer, Details> ENTRY_MAP = new HashMap<>();

    public static void populateBooks(String[] values) {
        if (getENTRIES().size() == 66) {
            Log.i(TAG, "populateBooks: Books already Populated [Size=66]");
        }
        Details item;
        for (int i = 0; i < values.length; i++) {
            String[] str = values[i].split(":");
            item = new Details((i + 1), // BOOK NUMBER
                               str[0], // BOOK NAME
                               Integer.parseInt(str[1])); // BOOK CHAPTER COUNT
            getENTRIES().add(item);
            ENTRY_MAP.put(item.number, item);
        }
        Log.i(TAG, "populateBooks: All Books created");
    }

    public static List<Details> getENTRIES() {
        return ENTRIES;
    }

    public static String[] getAllBookNamed() {
        String[] names = new String[ENTRY_MAP.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = ENTRY_MAP.get((i + 1)).name;
        }
        return names;
    }

    public static Details getBookDetails(final String pBookName) {
        for (Details d : getENTRIES()) {
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
