/*
 *
 * This file is part of SimpleBible : A Holy Bible Application on the
 * Android Mobile platform with easy navigation and offline access.
 *
 * Copyright (c) 2015.
 * Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
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
 *
 */
package com.andrewchelladurai.simplebible;

import android.util.Log;

public class Book {

    private static final String CLASS_NAME = "Book";
    private int    bookNumber;
    private int    totalChapters;
    private String bookName;

    public Book(int bookNumber, String bookName, int totalChapters) {
        this.bookNumber = bookNumber;
        this.bookName = bookName;
        this.totalChapters = totalChapters;
    }

    public int getTotalChapters() {
        Log.i(CLASS_NAME, "Entering getTotalChapters");
        return totalChapters;
    }

    public String getBookName() {
        Log.i(CLASS_NAME, "Entering getBookName");
        return bookName;
    }

    public int getBookNumber() {
        Log.i(CLASS_NAME, "Entering getBookNumber");
        return bookNumber;
    }

    @Override
    public String toString() {
        return getBookName() + " : " + getTotalChapters() + " Chapters";
    }

}
