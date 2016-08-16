/*
 *
 * This file 'ChapterList.java' is part of SimpleBible :
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListChapter {

    private static final String                      TAG          = "SB_ListChapter";
    private static final StringBuilder               mPrependText = new StringBuilder();
    private static final CopyOnWriteArrayList<Entry> ITEMS        = new CopyOnWriteArrayList<>();
    private static final Map<String, Entry>          ITEM_MAP     = new HashMap<>();

    public static void populateList(int chapterCount, String prependText) {
        Utilities.log(TAG,
                      "populateList() called with: [" + chapterCount + "], [" + prependText + "]");
        if (mPrependText.length() < 1) {
            mPrependText.append(prependText);
        }
        ITEMS.clear();
        ITEM_MAP.clear();
        Entry entry;
        for (int i = 1; i <= chapterCount; i++) {
            entry = new Entry(String.valueOf(i));
            ITEMS.add(entry);
            ITEM_MAP.put(entry.chapterNumber, entry);
        }
        Utilities.log(TAG, "populateList() returned");
    }

    public static int getCount() {
        return ITEMS.size();
    }

    public static List<Entry> getList() {
        return ITEMS;
    }

    public static Entry getItem(String keyValue) {
        return ITEM_MAP.get(keyValue);
    }

    public static class Entry
            implements Parcelable {

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

        private static final String TAG = "SB_ListChapter.Entry";
        private final String chapterNumber;

        public Entry(String position) {
            chapterNumber = position;
        }

        protected Entry(Parcel in) {
            chapterNumber = in.readString();
        }

        @Override
        public String toString() {
            return getContent();
        }

        public String getContent() {
            return mPrependText + " " + chapterNumber;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(chapterNumber);
        }

        public String getChapterNumber() {
            return chapterNumber;
        }
    }
}
