/*
 * This file 'BookUnit.java' is part of SimpleBible :  An Android Bible application
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

public class BookUnit {

    private static final String TAG = "BookUnit";
    private int bookNumber;
    private int totalChapters;
    private String bookName;

    public BookUnit(int bookNumber, String bookName, int totalChapters) {
        Log.d(TAG, "BookUnit() called with: bookNumber = [" + bookNumber + "], bookName = ["
                   + bookName + "], totalChapters = [" + totalChapters + "]");
        this.bookNumber = bookNumber;
        this.bookName = bookName;
        this.totalChapters = totalChapters;
        Log.d(TAG, "BookUnit() Exited");
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
