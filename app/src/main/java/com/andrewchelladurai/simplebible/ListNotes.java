/*
 *
 * This file 'ListNotes.java' is part of SimpleBible :
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListNotes {

    private static final List<Entry>        ITEMS    = new ArrayList<>();
    private static final Map<String, Entry> ITEM_MAP = new HashMap<>();

    public static void populate() {
        ITEM_MAP.clear();
        ITEMS.clear();
        for (int i = 1; i <= 25; i++) {
            Entry entry = new Entry(
                    String.valueOf(i),
                    String.valueOf(i),
                    String.valueOf(i), "Verse Text for position " + i, makeDetails(i));
            ITEMS.add(entry);
            ITEM_MAP.put(entry.getReference(), entry);
        }
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Notes about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static List<Entry> getITEMS() {
        return ITEMS;
    }

    public static Entry getItem(int position) {
        return ITEMS.get(position);
    }

    public static Entry getItem(String id) {
        return ITEM_MAP.get(id);
    }

    public static class Entry {

        private final String bookNumber;
        private final String chapterNumber;
        private final String verseNumber;
        private final String verse;
        private final String notes;

        public Entry(String bookN, String chapterN, String verseN, String content, String details) {
            bookNumber = bookN;
            chapterNumber = chapterN;
            verseNumber = verseN;
            verse = content;
            notes = details;
        }

        @Override
        public String toString() {
            return getNotes();
        }

        public String getNotes() {
            return notes;
        }

        public String getReference() {
            return getBookNumber() + ":" + getChapterNumber() + ":" + getVerseNumber();
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
