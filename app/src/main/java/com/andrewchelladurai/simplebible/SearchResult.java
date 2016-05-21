/*
 *
 * This file 'SearchResult.java' is part of SimpleBible :  SimpleBible
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

public class SearchResult {

    private static final List<Verse> ITEMS = new ArrayList<>();
    private static final Map<String, Verse> ITEM_MAP = new HashMap<>();
    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(i, i, i, "use R.string.no_verse_found");
        }
    }

    private static void addItem(int pBook, int pChapter, int pVerse, String noResultFound) {
        DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        String verse = dbu.getSpecificVerse(pBook, pChapter, pVerse);
        if (verse == null) {
            verse = noResultFound;
        }
        Verse item = new Verse(pBook, pChapter, pVerse, verse);
        ITEMS.add(item);
        ITEM_MAP.put(item.getVerseReference(), item);
    }

    public static List<Verse> getITEMS() {
        return ITEMS;
    }

    static class Verse {

        private final int mBookNumber;
        private final int mChapterNumber;
        private final int mVerseNumber;
        private final String mVerseText;

        public Verse(int bookNumber, int chapterNumber, int verseNumber, String verseText) {
            mBookNumber = bookNumber;
            mChapterNumber = chapterNumber;
            mVerseNumber = verseNumber;
            mVerseText = verseText;
        }

        @Override
        public String toString() {
            return getVerseReference() + " = " + getVerseText();
        }

        public String getVerseText() {
            return mVerseText;
        }

        public int getBookNumber() {
            return mBookNumber;
        }

        public int getChapterNumber() {
            return mChapterNumber;
        }

        public int getVerseNumber() {
            return mVerseNumber;
        }

        public String getVerseReference() {
            return mBookNumber + ":" + mChapterNumber + ":" + mVerseNumber;
        }
    }
}