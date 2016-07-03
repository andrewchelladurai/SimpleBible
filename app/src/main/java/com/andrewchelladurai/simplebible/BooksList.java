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

package com.andrewchelladurai.simplebible;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksList {

    private static final String             TAG      = "SB_BooksList";
    private static final List<Entry>        ITEMS    = new ArrayList<>();
    private static final Map<String, Entry> ITEM_MAP = new HashMap<>();

    public static List<Entry> getItems() {
        return ITEMS;
    }

    public static Entry getItem(String chapterNumber) {
        return ITEM_MAP.get(chapterNumber);
    }

    public static void populateBooks(String[] bookArray) {
        if (ITEM_MAP.get("66") != null & ITEMS.size() == 66) {
            Log.d(TAG, "populateBooks: Books already populated");
            return;
        }
        String[] values;
        Entry item;
        for (int i = 0; i < bookArray.length; i++) {
            values = bookArray[i].split(":");
            item = new Entry((i + 1), values[0], values[1]);
            ITEMS.add(item);
            ITEM_MAP.put(item.mName, item);
        }
        Log.d(TAG, "populateBooks: All " + ITEMS.size() + " books created");
    }

    public static class Entry
            implements Parcelable {

        private final String mName;
        private final String mChapterCount;
        private final String mBookNumber;

        public Entry(int bookNumber, String mName, String mChapter) {
            this.mName = mName;
            this.mChapterCount = mChapter;
            this.mBookNumber = String.valueOf(bookNumber);
        }

        protected Entry(Parcel in) {
            mName = in.readString();
            mChapterCount = in.readString();
            mBookNumber = in.readString();
        }

        public static final Creator<Entry> CREATOR = new Creator<Entry>() {

            @Override
            public Entry createFromParcel(Parcel in) {
                return new Entry(in);
            }

            @Override
            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };

        @Override
        public String toString() {
            return mBookNumber + ":" + mName + ":" + mChapterCount;
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mName);
            dest.writeString(mChapterCount);
            dest.writeString(mBookNumber);
        }

        public String getName() {
            return mName;
        }

        public String getChapterCount() {
            return mChapterCount;
        }

        public String getBookNumber() {
            return mBookNumber;
        }
    }
}
