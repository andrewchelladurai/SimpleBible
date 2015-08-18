/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 *
 */
package com.andrewchelladurai.simplebible;

public class Book {

    private int    bookNumber;
    private int    totalChapters;
    private String bookName;

    public Book(int bookNumber, String bookName, int totalChapters) {
        this.bookNumber = bookNumber;
        this.bookName = bookName;
        this.totalChapters = totalChapters;
    }

    public int getTotalChapters() {
        return totalChapters;
    }

    public String getBookName() {
        return bookName;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    @Override
    public String toString() {
        return getBookName() + " : " + getTotalChapters() + " Chapters";
    }

}
