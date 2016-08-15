/*
 *
 * This file 'DummyContent.java' is part of SimpleBible :
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

public class ListSearch {

    static final         Map<String, Entry> ITEM_MAP = new HashMap<>();
    private static final ArrayList<Entry>   ITEMS    = new ArrayList<>();
    private static final ArrayList<Entry>   SELECTED = new ArrayList<>();
    private static final String             TAG      = "SB_ListSearch";

    public static void populate(ArrayList<String> list) {
        if (list == null) {
            Utilities.log(TAG, "populate: list == null, this should happen exactly once, no more");
            return;
        }
        Utilities.log(TAG, "populate() called with list size = [" + list.size() + "]");

        String          parts[];
        Entry           entry;
        DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        for (String reference : list) {
            parts = reference.split(":");
            if (parts.length != 3) {
                continue;
            }
            entry = new Entry(parts[0], parts[1], parts[2],
                              dbu.getSpecificVerse(Integer.parseInt(parts[0]),
                                                   Integer.parseInt(parts[1]),
                                                   Integer.parseInt(parts[2])));
            ITEMS.add(entry);
            ITEM_MAP.put(entry.getReference(), entry);
        }
        Utilities.log(TAG, "populate() returned");
    }

    public static List<Entry> getEntries() {
        return ITEMS;
    }

    public static void truncate() {
        ITEMS.clear();
        ITEM_MAP.clear();
        SELECTED.clear();
        Utilities.log(TAG, "truncate() called");
    }

    public static void addSelectedEntry(Entry entry) {
        SELECTED.add(entry);
    }

    public static void removeSelectedEntry(Entry entry) {
        Utilities.log(TAG, "removeSelectedEntry() called reference [" + entry.getReference() + "]");
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

        private static final String TAG = "SB_ListSearch.Entry";
        private final String bookNumber;
        private final String chapterNumber;
        private final String verseNumber;
        private final String verse;

        public Entry(String bookNum, String chapterNum, String verseNum, String text) {
            bookNumber = bookNum;
            chapterNumber = chapterNum;
            verseNumber = verseNum;
            verse = text;
        }

        protected Entry(Parcel in) {
            bookNumber = in.readString();
            chapterNumber = in.readString();
            verseNumber = in.readString();
            verse = in.readString();
        }

        @Override
        public String toString() {
            return verse;
        }

        public String getReference() {
            return bookNumber + ":" + chapterNumber + ":" + verseNumber;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(bookNumber);
            dest.writeString(chapterNumber);
            dest.writeString(verseNumber);
            dest.writeString(verse);
        }

        public String getBookNumber() {
            return bookNumber;
        }

        public String getChapterNumber() {
            return chapterNumber;
        }

        public String getVerseNumber() {
            return verseNumber;
        }

        public String getVerse() {
            return verse;
        }
    }
}
