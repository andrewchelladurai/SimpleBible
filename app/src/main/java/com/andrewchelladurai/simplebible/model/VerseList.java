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

package com.andrewchelladurai.simplebible.model;

import com.andrewchelladurai.simplebible.utilities.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerseList {

    private static final List<VerseItem>        ITEMS    = new ArrayList<>();
    private static final Map<String, VerseItem> ITEM_MAP = new HashMap<>();

    public static List<VerseItem> getItems() {
        if (ITEMS.isEmpty() || ITEMS.size() == 0) {
            return null;
        }
        return ITEMS;
    }

    public static boolean populateList(int bookNumber, int chapterNumber,
                                       ArrayList<String> versesList) {
        ITEMS.clear();
        ITEM_MAP.clear();
        VerseItem item;
        for (int verseNumber = 1; verseNumber <= versesList.size(); verseNumber++) {
            item = new VerseItem(
                    bookNumber, chapterNumber, verseNumber, versesList.get((verseNumber - 1)));
            ITEMS.add(item);
            ITEM_MAP.put(item.getReference(), item);
        }
        return true;
    }

    public static class VerseItem {

        private       int    mBookNumber;
        private final int    mChapterNumber;
        private final int    mVerseNumber;
        private final String mVerseText;

        VerseItem(int bookNumber, int chapterNumber, int verseNumber, String verseText) {
            mBookNumber = bookNumber;
            mChapterNumber = chapterNumber;
            mVerseNumber = verseNumber;
            mVerseText = verseText;
        }

        String getReference() {
            return mBookNumber + Constants.DELIMITER_IN_REFERENCE +
                   mChapterNumber + Constants.DELIMITER_IN_REFERENCE +
                   mVerseNumber + Constants.DELIMITER_IN_REFERENCE;
        }

        @Override
        public String toString() {
            return getReference() + " - " + getVerseText();
        }

        public String getVerseText() {
            return mVerseText;
        }
    }
}
