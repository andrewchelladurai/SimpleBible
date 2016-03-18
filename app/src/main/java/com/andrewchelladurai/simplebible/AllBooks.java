/*
 * This file 'AllBooks.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllBooks {

    private static final Map<String, Book> NT_BOOKS_MAP = new HashMap<>();
    private static final Map<String, Book> OT_BOOKS_MAP = new HashMap<>();
    private static final String TAG = "AllBooks";
    private static final List<Book> OT_BOOKS_LIST = new ArrayList<>();
    private static final List<Book> NT_BOOKS_LIST = new ArrayList<>();

    public static void populateBooks(String allBooks[]) {
        if (OT_BOOKS_LIST.size() == 39 || NT_BOOKS_LIST.size() == 27) {
            Log.d(TAG, "populateBooks: Lists already populated");
        } else {
            for (int i = 0; i < allBooks.length; i++) {
                if (i < 39) {
                    addOTBook(createBookItem((i + 1), allBooks[i]));
                } else {
                    addNTBook(createBookItem((i + 1), allBooks[i]));
                }
            }
            Log.d(TAG, "populateBooks: Completed successfully");
        }
    }

    public static String[] getAllBooks() {
        String books[] = new String[66];
        int i = 0;
        for (Book book : getOTBooksList()) {
            books[i] = book.getName() + " : " + book.getChapterCount() + " Chapters";
            i++;
        }
        for (Book book : getNTBooksList()) {
            books[i] = book.getName() + " : " + book.getChapterCount() + " Chapters";
            i++;
        }
        return books;
    }

    private static void addOTBook(Book item) {
        OT_BOOKS_LIST.add(item);
        OT_BOOKS_MAP.put(item.bookNumber, item);
    }

    private static void addNTBook(Book item) {
        NT_BOOKS_LIST.add(item);
        NT_BOOKS_MAP.put(item.bookNumber, item);
    }

    private static Book createBookItem(int bookNumber, String label) {
        String splits[] = label.split(":");

        return new Book(Integer.toString(bookNumber), // BOOK NUMBER
                splits[0], // BOOK NAME
                Integer.valueOf(splits[1])); // NUMBER OF CHAPTERS
    }

    public static Book getBook(int bookNumber) {
        if ((bookNumber - 1) < 39) {
            Log.d(TAG, "getBook() Returning OT Book [" + bookNumber + "]");
            return OT_BOOKS_LIST.get(bookNumber - 1);
        } else {
            Log.d(TAG, "getBook() Returning NT Book [" + bookNumber + "]");
            return NT_BOOKS_LIST.get(bookNumber - 40);
        }
    }

    public static List<Book> getNTBooksList() {
        return NT_BOOKS_LIST;
    }

    public static List<Book> getOTBooksList() {
        return OT_BOOKS_LIST;
    }

    public static class Book {

        private static final String TAG = "Book";
        final String bookNumber;
        final String bookName;
        private int chapterCount = 1;

        public Book(String id, String title, int count) {
            bookNumber = id;
            bookName = title;
            chapterCount = count;
            Log.d(TAG, "Book() called with [" + bookNumber + "], [" + bookName + "]");
        }

        @Override
        public String toString() {
            return bookName + " : " + chapterCount + " Chapters";
        }

        public int getChapterCount() {
            return chapterCount;
        }

        public String getName() {
            return bookName;
        }

        public String getBookNumber() {
            return bookNumber;
        }
    }
}
