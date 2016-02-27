package com.andrewchelladurai.simplebible;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllBooks {

    public static final  Map<String, Book> OT_BOOKS_MAP  = new HashMap<String, Book>();
    public static final  Map<String, Book> NT_BOOKS_MAP  = new HashMap<String, Book>();
    private static final String            TAG           = "AllBooks";
    private static final List<Book>        OT_BOOKS_LIST = new ArrayList<Book>();
    private static final List<Book>        NT_BOOKS_LIST = new ArrayList<Book>();

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
            return OT_BOOKS_LIST.get(bookNumber - 1);
        } else {
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
            Log.d(TAG,
                  "Book() called with: id = [" + bookNumber + "], content = [" + bookName + "]");
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
