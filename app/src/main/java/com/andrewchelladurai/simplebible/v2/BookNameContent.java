/*
 * This file 'DummyContent.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 */

package com.andrewchelladurai.simplebible.v2;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookNameContent {

    public static final List<BookNameItem> ITEMS = new ArrayList<BookNameItem>();
    public static final Map<String, BookNameItem> ITEM_MAP = new HashMap<String, BookNameItem>();
    private static final String TAG = "BookNameContent";

    public static void populateBooks(String allBooks[]) {
        if (ITEMS.size() == 66) {
            Log.d(TAG, "populateBooks: Lists already populated");
        } else {
            String values[];
            for (int i = 0; i < allBooks.length; i++) {
                values = allBooks[i].split(":");
                addItem(i, new BookNameItem(i, values[0], Integer.valueOf(values[1])));
            }
            Log.d(TAG, "populateBooks: Completed successfully");
        }
    }

    public static BookNameItem getBookItem(int bookPosition) {
        try {
            return ITEMS.get(bookPosition);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getBookChapterCount(int bookPosition) {
        BookNameItem item = getBookItem(bookPosition);
        if (item != null) {
            return item.chapterCount;
        } else {
            return 0;
        }
    }

    public static int getBookPosition(String bookName) {
        for (BookNameItem item : ITEMS) {
            if (item.bookName.equalsIgnoreCase(bookName)) {
                return item.bookNumber;
            }
        }
        return 0;
    }

    private static void addItem(int book_number, BookNameItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(book_number + "", item);
    }

    public static class BookNameItem {
        private final String bookName;
        private final int bookNumber, chapterCount;

        public BookNameItem(int number, String name, int count) {
            bookName = name;
            bookNumber = number;
            chapterCount = count;
        }

        @Override
        public String toString() {
            return bookName + " : " + chapterCount + " Chapters";
        }

        public int getBookNumber() {
            return bookNumber;
        }

        public int getChapterCount() {
            return chapterCount;
        }

        public String getName() {
            return bookName;
        }
    }
}
