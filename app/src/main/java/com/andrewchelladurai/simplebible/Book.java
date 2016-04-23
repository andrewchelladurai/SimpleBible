/*
 * This file 'Book.java' is part of SimpleBible : SimpleBible
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

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 16-Apr-2016 @ 11:40 AM
 */
public class Book {

    public static final List<Details> BOOKS = new ArrayList<>();
    public static final Map<String, Details> BOOK_MAP = new HashMap<>();
    private static final String TAG = "BookDetails";

    public static void populateDetails(final String[] pStringArray) {
        if (BOOKS.size() > 0) {
            Log.d(TAG, "populateDetails: " + BOOKS.size() + " books already created");
            return;
        }

        int bookNumber = 1;
        String[] splitValue;
        Details book;
        for (String value : pStringArray) {
            splitValue = value.split(":");
            book = new Details(bookNumber + "", splitValue[0], splitValue[1]);
            BOOKS.add(book);
            BOOK_MAP.put(book.number, book);
            bookNumber++;
        }
        Log.d(TAG, "populateDetails: " + BOOKS.size() + " books created");
    }

    public static Details getDetails(int pBookNumber) {
        return BOOK_MAP.get(pBookNumber + "");
    }

    public static String[] getAllBookNames() {
        String[] values = new String[BOOKS.size()];
        int i = 0;
        for (Details d : BOOKS) {
            values[i] = d.getName();
            i++;
        }
        return values;
    }

    public static int getBookDetails(final String pName) {
        for (Details d : BOOKS) {
            if (pName.equalsIgnoreCase(d.getName())) {
                return Integer.parseInt(d.getNumber());
            }
        }
        return 0;
    }

    public static class Details {

        private final String number;
        private final String name;
        private final String chapterCount;

        public Details(String pNumber, String pName, String pChapterCount) {
            name = pName;
            number = pNumber;
            chapterCount = pChapterCount;
        }

        @Override
        public String toString() {
            return number + " : " + name + " : " + chapterCount;
        }

        public String getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public String getChapterCount() {
            return chapterCount;
        }
    }
}
