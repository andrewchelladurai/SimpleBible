/*
 *
 * This file 'BooksList.java' is part of SimpleBible :
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
 *
 */

package com.andrewchelladurai.simplebible.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.andrewchelladurai.simplebible.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksList {

    private static final String TAG = "SB_BooksList";

    private static final List<BookItem>        ITEMS              = new ArrayList<>();
    private static final Map<String, BookItem> ITEM_MAP_BY_NUMBER = new HashMap<>();
    private static final Map<String, BookItem> ITEM_MAP_BY_NAME   = new HashMap<>();

    public static boolean clearList() {
        ITEMS.clear();
        ITEM_MAP_BY_NUMBER.clear();
        ITEM_MAP_BY_NAME.clear();
        return true;
    }

    public static int getListCount() {
        return ITEMS.size();
    }

    public static boolean populateBooksList(String[] array) {
        Log.d(TAG, "populateBooksList called");
        if (getListCount() == 66) {
            Log.d(TAG, "populateBooksList: returning : ITEMS.size() == 66");
            return false;
        }
        if (null == array || array.length == 0 || array.length > 66) {
            Log.d(TAG, "populateBooksList: returning : null == array || array.length == 0");
            return false;
        }
        BookItem item;
        String parts[], bookName;
        int bookNumber = 0, chapterCount;
        for (int i = 0; i < array.length; i++) {
            parts = array[i].split(Constants.DELIMITER_IN_REFERENCE);
            if (parts.length != 2) {
                Log.d(TAG, "populateBooksList: skipping entry " + array[i]);
                continue;
            }

            bookNumber = i + 1;
            bookName = parts[0];
            chapterCount = Integer.parseInt(parts[1]);

            item = new BookItem(bookNumber, bookName, chapterCount);
            ITEMS.add(item);
            ITEM_MAP_BY_NUMBER.put(String.valueOf(item.mBookNumber), item);
            ITEM_MAP_BY_NAME.put(String.valueOf(item.mBookName), item);
        }
        Log.d(TAG, "populateBooksList returned: " + bookNumber + " Books populated");
        return true;
    }

    public static List<BookItem> getListItems() {
        if (ITEMS.size() != 66) {
            Log.e(TAG, "getListItems: ITEMS.size() != 66");
            Exception exception = new Exception("ITEMS.size() != 66");
            Log.wtf(TAG, "getListItems: ", exception);
        }
        return ITEMS;
    }

    public static BookItem getBookItem(String bookName) {
        return (ITEM_MAP_BY_NAME.containsKey(bookName)) ? ITEM_MAP_BY_NAME.get(bookName) : null;
    }

    public static class BookItem
            implements Parcelable {

        private final int    mBookNumber;
        private final int    mChapterCount;
        private final String mBookName;

        BookItem(int position, String name, int count) {
            mBookNumber = position;
            mChapterCount = count;
            mBookName = name;
        }

        protected BookItem(Parcel in) {
            mBookNumber = in.readInt();
            mChapterCount = in.readInt();
            mBookName = in.readString();
        }

        public static final Creator<BookItem> CREATOR = new Creator<BookItem>() {
            @Override
            public BookItem createFromParcel(Parcel in) {
                return new BookItem(in);
            }

            @Override
            public BookItem[] newArray(int size) {
                return new BookItem[size];
            }
        };

        public int getBookNumber() {
            return mBookNumber;
        }

        public int getChapterCount() {
            return mChapterCount;
        }

        public String getBookName() {
            return mBookName;
        }

        @Override
        public String toString() {
            return mBookNumber + " : " + mBookName;
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mBookNumber);
            dest.writeInt(mChapterCount);
            dest.writeString(mBookName);
        }
    }
}
