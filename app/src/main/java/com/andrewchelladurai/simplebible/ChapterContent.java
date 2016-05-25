/*
 * This file 'ChapterContent.java' is part of SimpleBible :
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

public class ChapterContent {

    public static final List<VerseEntry>        ITEMS    = new ArrayList<>();
    public static final Map<String, VerseEntry> ITEM_MAP = new HashMap<>();

    public static List<VerseEntry> refreshList(int bookNumber, int chapterNumber) {
        ITEMS.clear();
        DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        ArrayList<String> list = dbu.getAllVersesOfChapter(bookNumber, chapterNumber);

        if (list != null) {
            int verseNumber;
            String[] parts;
            for (String result : list) {
                parts = result.split(" - ");
                verseNumber = Integer.parseInt(parts[0]);
                VerseEntry entry = new VerseEntry(bookNumber, chapterNumber, verseNumber, parts[1]);
                ITEMS.add(entry);
                ITEM_MAP.put(entry.getReference(), entry);
            }
        }

        return ITEMS;
    }

    public static class VerseEntry {
        public final String bookNumber;
        public final String chapterNumber;
        public final String verseNumber;
        public final String verseText;

        public VerseEntry(int bookNum, int chapterNum, int verseNum, String verseTxt) {
            this.bookNumber = String.valueOf(bookNum);
            this.chapterNumber = String.valueOf(chapterNum);
            this.verseNumber = String.valueOf(verseNum);
            this.verseText = verseTxt;
        }

        @Override
        public String toString() {
            return getReference() + "=" + verseText;
        }

        public String getReference() {
            return bookNumber + ":" + chapterNumber + ":" + verseNumber;
        }

        public CharSequence getVerseText() {
            return verseText;
        }

        public String getVerseNumber() {
            return verseNumber;
        }
    }
}
