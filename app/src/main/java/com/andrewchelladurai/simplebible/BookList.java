/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 *
 */

package com.andrewchelladurai.simplebible;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by Android template
 * wizards.
 */
public class BookList {

    //    private static final Map<String, Book> map   = new HashMap<>();
    private static final List<Book> books = new ArrayList<>();

    static {
        addBook(new Book(0, "Genesis", 50));
        addBook(new Book(1, "Exodus", 40));
        addBook(new Book(2, "Leviticus", 27));
        addBook(new Book(3, "Numbers", 36));
        addBook(new Book(4, "Deuteronomy", 34));
        addBook(new Book(5, "Joshua", 24));
        addBook(new Book(6, "Judges", 21));
        addBook(new Book(7, "Ruth", 4));
        addBook(new Book(8, "1 Samuel", 31));
        addBook(new Book(9, "2 Samuel", 24));
        addBook(new Book(10, "1 Kings", 22));
        addBook(new Book(11, "2 Kings", 25));
        addBook(new Book(12, "1 Chronicles", 29));
        addBook(new Book(13, "2 Chronicles", 36));
        addBook(new Book(14, "Ezra", 10));
        addBook(new Book(15, "Nehemiah", 13));
        addBook(new Book(16, "Esther", 10));
        addBook(new Book(17, "Job", 42));
        addBook(new Book(18, "Psalms", 150));
        addBook(new Book(19, "Proverbs", 31));
        addBook(new Book(20, "Ecclesiastes", 12));
        addBook(new Book(21, "Song Of Solomon", 8));
        addBook(new Book(22, "Isaiah", 66));
        addBook(new Book(23, "Jeremiah", 52));
        addBook(new Book(24, "Lamentations", 5));
        addBook(new Book(25, "Ezekiel", 48));
        addBook(new Book(26, "Daniel", 12));
        addBook(new Book(27, "Hosea", 14));
        addBook(new Book(28, "Joel", 3));
        addBook(new Book(29, "Amos", 9));
        addBook(new Book(30, "Obadiah", 1));
        addBook(new Book(31, "Jonah", 4));
        addBook(new Book(32, "Micah", 7));
        addBook(new Book(33, "Nahum", 3));
        addBook(new Book(34, "Habakkuk", 3));
        addBook(new Book(35, "Zephaniah", 3));
        addBook(new Book(36, "Haggai", 2));
        addBook(new Book(37, "Zechariah", 14));
        addBook(new Book(38, "Malachi", 4));
        addBook(new Book(39, "Matthew", 28));
        addBook(new Book(40, "Mark", 16));
        addBook(new Book(41, "Luke", 24));
        addBook(new Book(42, "John", 21));
        addBook(new Book(43, "Acts", 28));
        addBook(new Book(44, "Romans", 16));
        addBook(new Book(45, "1 Corinthians", 16));
        addBook(new Book(46, "2 Corinthians", 13));
        addBook(new Book(47, "Galatians", 6));
        addBook(new Book(48, "Ephesians", 6));
        addBook(new Book(49, "Philippians", 4));
        addBook(new Book(50, "Colossians", 4));
        addBook(new Book(51, "1 Thessalonians", 5));
        addBook(new Book(52, "2 Thessalonians", 3));
        addBook(new Book(53, "1 Timothy", 6));
        addBook(new Book(54, "2 Timothy", 4));
        addBook(new Book(55, "Titus", 3));
        addBook(new Book(56, "Philemon", 1));
        addBook(new Book(57, "Hebrews", 13));
        addBook(new Book(58, "James", 5));
        addBook(new Book(59, "1 Peter", 5));
        addBook(new Book(60, "2 Peter", 3));
        addBook(new Book(61, "1 John", 5));
        addBook(new Book(62, "2 John", 1));
        addBook(new Book(63, "3 John", 1));
        addBook(new Book(64, "Jude", 1));
        addBook(new Book(65, "Revelation", 22));
    }

    private static void addBook(Book book) {
        books.add(book);
//        map.put(book.getBookNumber() + "", book);
    }

    public static int getBookID(CharSequence bookName) {
        for (int i = 0; i < books.size(); i++) {
            if ((bookName + "").equalsIgnoreCase(books.get(i).getBookName())) {
                return i;
            }
        }
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

    public static List<Book> getBooks() {
        return books;
    }
}
