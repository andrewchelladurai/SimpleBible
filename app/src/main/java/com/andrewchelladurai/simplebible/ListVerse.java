/*
 *
 * This file 'VerseList.java' is part of SimpleBible :
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

public class ListVerse {

    private static final String                      TAG      = "SB_ListVerse";
    private static final CopyOnWriteArrayList<Entry> ITEMS    = new CopyOnWriteArrayList<>();
    private static final Map<String, Entry>          ITEM_MAP = new HashMap<>();
    private static final ArrayList<Entry>            SELECTED = new ArrayList<>();

    public static void populateEntries(ArrayList<String> verseList, int bookNumber, int
            chapterNumber) {
        Utilities.log(TAG, "populateEntries() called");
        truncate();
        Entry entry;
        for (int i = 0; i < verseList.size(); i++) {
            entry = new Entry(bookNumber, chapterNumber, (i + 1), verseList.get(i));
            ITEMS.add(entry);
            ITEM_MAP.put(entry.getReference(), entry);
        }
    }

    public static void truncate() {
        ITEMS.clear();
        ITEM_MAP.clear();
        SELECTED.clear();
    }

    public static List<Entry> getEntries() {
        return ITEMS;
    }

    public static void addSelectedEntry(Entry entry) {
        SELECTED.add(entry);
    }

    public static void removeSelectedEntry(Entry entry) {
        if (SELECTED.contains(entry)) {
            SELECTED.remove(entry);
        } else {
            Utilities.log(TAG, "removeSelectedEntry: " + entry.getReference() + " not present");
        }
    }

    public static boolean isSelectedEntriesEmpty() {
        return SELECTED.isEmpty();
    }

    public static ArrayList<Entry> getSelectedEntries() {
        return SELECTED;
    }

    public static boolean isEntrySelected(Entry entry) {
        return SELECTED.contains(entry);
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

        private static final String TAG = "SB_ListVerse.Entry";
        private final String mBookNumber;
        private final String mChapterNumber;
        private final String mVerseNumber;
        private final String mVerseText;

        public Entry(int bookNumber, int chapterNumber, int verseNumber, String verseText) {
            mBookNumber = String.valueOf(bookNumber);
            mChapterNumber = String.valueOf(chapterNumber);
            mVerseNumber = String.valueOf(verseNumber);
            mVerseText = verseText;
        }

        protected Entry(Parcel in) {
            mBookNumber = in.readString();
            mChapterNumber = in.readString();
            mVerseNumber = in.readString();
            mVerseText = in.readString();
        }

        @Override
        public String toString() {
            return getVerseText();
        }

        public String getVerseText() {
            return mVerseText;
        }

        public String getReference() {
            return getBookNumber() + ":" + getChapterNumber() + ":" + getVerseNumber();
        }

        public String getBookNumber() {
            return mBookNumber;
        }

        public String getChapterNumber() {
            return mChapterNumber;
        }

        public String getVerseNumber() {
            return mVerseNumber;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mBookNumber);
            dest.writeString(mChapterNumber);
            dest.writeString(mVerseNumber);
            dest.writeString(mVerseText);
        }
    }
}
