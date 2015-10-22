/*
 * This file 'BookSList.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Copyright (c) Andrew Chelladurai - 2015.
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
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by Android template
 * wizards.
 */
public class BookSList {

    private static final List<BookUnit> books = new ArrayList<>();
    private static final String TAG = "BookSList";

    static {
        addBook(new BookUnit(0, "Genesis", 50));
        addBook(new BookUnit(1, "Exodus", 40));
        addBook(new BookUnit(2, "Leviticus", 27));
        addBook(new BookUnit(3, "Numbers", 36));
        addBook(new BookUnit(4, "Deuteronomy", 34));
        addBook(new BookUnit(5, "Joshua", 24));
        addBook(new BookUnit(6, "Judges", 21));
        addBook(new BookUnit(7, "Ruth", 4));
        addBook(new BookUnit(8, "1 Samuel", 31));
        addBook(new BookUnit(9, "2 Samuel", 24));
        addBook(new BookUnit(10, "1 Kings", 22));
        addBook(new BookUnit(11, "2 Kings", 25));
        addBook(new BookUnit(12, "1 Chronicles", 29));
        addBook(new BookUnit(13, "2 Chronicles", 36));
        addBook(new BookUnit(14, "Ezra", 10));
        addBook(new BookUnit(15, "Nehemiah", 13));
        addBook(new BookUnit(16, "Esther", 10));
        addBook(new BookUnit(17, "Job", 42));
        addBook(new BookUnit(18, "Psalms", 150));
        addBook(new BookUnit(19, "Proverbs", 31));
        addBook(new BookUnit(20, "Ecclesiastes", 12));
        addBook(new BookUnit(21, "Song Of Solomon", 8));
        addBook(new BookUnit(22, "Isaiah", 66));
        addBook(new BookUnit(23, "Jeremiah", 52));
        addBook(new BookUnit(24, "Lamentations", 5));
        addBook(new BookUnit(25, "Ezekiel", 48));
        addBook(new BookUnit(26, "Daniel", 12));
        addBook(new BookUnit(27, "Hosea", 14));
        addBook(new BookUnit(28, "Joel", 3));
        addBook(new BookUnit(29, "Amos", 9));
        addBook(new BookUnit(30, "Obadiah", 1));
        addBook(new BookUnit(31, "Jonah", 4));
        addBook(new BookUnit(32, "Micah", 7));
        addBook(new BookUnit(33, "Nahum", 3));
        addBook(new BookUnit(34, "Habakkuk", 3));
        addBook(new BookUnit(35, "Zephaniah", 3));
        addBook(new BookUnit(36, "Haggai", 2));
        addBook(new BookUnit(37, "Zechariah", 14));
        addBook(new BookUnit(38, "Malachi", 4));
        addBook(new BookUnit(39, "Matthew", 28));
        addBook(new BookUnit(40, "Mark", 16));
        addBook(new BookUnit(41, "Luke", 24));
        addBook(new BookUnit(42, "John", 21));
        addBook(new BookUnit(43, "Acts", 28));
        addBook(new BookUnit(44, "Romans", 16));
        addBook(new BookUnit(45, "1 Corinthians", 16));
        addBook(new BookUnit(46, "2 Corinthians", 13));
        addBook(new BookUnit(47, "Galatians", 6));
        addBook(new BookUnit(48, "Ephesians", 6));
        addBook(new BookUnit(49, "Philippians", 4));
        addBook(new BookUnit(50, "Colossians", 4));
        addBook(new BookUnit(51, "1 Thessalonians", 5));
        addBook(new BookUnit(52, "2 Thessalonians", 3));
        addBook(new BookUnit(53, "1 Timothy", 6));
        addBook(new BookUnit(54, "2 Timothy", 4));
        addBook(new BookUnit(55, "Titus", 3));
        addBook(new BookUnit(56, "Philemon", 1));
        addBook(new BookUnit(57, "Hebrews", 13));
        addBook(new BookUnit(58, "James", 5));
        addBook(new BookUnit(59, "1 Peter", 5));
        addBook(new BookUnit(60, "2 Peter", 3));
        addBook(new BookUnit(61, "1 John", 5));
        addBook(new BookUnit(62, "2 John", 1));
        addBook(new BookUnit(63, "3 John", 1));
        addBook(new BookUnit(64, "Jude", 1));
        addBook(new BookUnit(65, "Revelation", 22));
    }

    private static void addBook(BookUnit book) {
        books.add(book);
    }

    public static int getBookID(String bookName) {
        Log.d(TAG, "getBookID() Entered");
        for (int i = 0; i < books.size(); i++) {
            if (bookName.equalsIgnoreCase(books.get(i).getBookName())) {
                Log.d(TAG, "getBookID() returned: " + i);
                return i;
            }
        }
        Log.d(TAG, "getBookID() returned: " + -1);
        return -1;
    }

    public static String getBookName(int id) {
        return books.get(id).getBookName();
    }

    public static int getBookNumber(int id) {
        return books.get(id).getBookNumber();
    }

    public static int getTotalChapters(int id) {
        return books.get(id).getTotalChapters();
    }

    public static List<BookUnit> getBooks() {
        return books;
    }
}
