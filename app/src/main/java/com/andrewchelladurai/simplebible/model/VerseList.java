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

import android.util.Log;

import com.andrewchelladurai.simplebible.utilities.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerseList {

    private static final String                 TAG            = "SB_VerseList";
    private static final List<VerseItem>        ITEMS          = new ArrayList<>();
    private static final Map<String, VerseItem> ITEM_MAP       = new HashMap<>();
    private static final Map<String, VerseItem> SELECTED_ITEMS = new HashMap<>();
    private static       int                    mBookNumber    = 0;
    private static       int                    mChapterNumber = 0;

    public static List<VerseItem> getItems() {
        if (ITEMS.isEmpty() || ITEMS.size() == 0) {
            return null;
        }
        return ITEMS;
    }

    public static boolean populateList(int bookNumber, int chapterNumber,
                                       ArrayList<String> versesList) {
        Log.d(TAG, "populateList() called with: bookNumber = [" + bookNumber + "],"
                   + " chapterNumber = [" + chapterNumber + "],"
                   + " versesList of size = [" + versesList.size() + "]");

        if (VerseList.mBookNumber == bookNumber && VerseList.mChapterNumber == chapterNumber) {
            Log.d(TAG, "populateList: list already populated");
            return true;
        }

        VerseList.mBookNumber = bookNumber;
        VerseList.mChapterNumber = chapterNumber;
        boolean returnValue;
        try {
            ITEMS.clear();
            ITEM_MAP.clear();
            SELECTED_ITEMS.clear();
            VerseItem item;
            for (int verseNumber = 1; verseNumber <= versesList.size(); verseNumber++) {
                item = new VerseItem(
                        bookNumber, chapterNumber, verseNumber, versesList.get((verseNumber - 1)));
                ITEMS.add(item);
                ITEM_MAP.put(item.getReference(), item);
            }
            returnValue = true;
        } catch (Exception ex) {
            returnValue = false;
            Log.d(TAG, "populateList: " + ex.getLocalizedMessage());
        }
        return returnValue;
    }

    public static boolean updateSelectedItems(VerseItem item) {
        String reference = item.getReference();
        if (SELECTED_ITEMS.containsKey(reference)) {
            SELECTED_ITEMS.remove(reference);
            Log.d(TAG, "updateSelectedItems: removed " + item.getReference());
        } else {
            Log.d(TAG, "updateSelectedItems: added " + item.getReference());
            SELECTED_ITEMS.put(reference, item);
        }
        Log.d(TAG, "updateSelectedItems() returned: " + SELECTED_ITEMS.containsKey(reference));
        return isSelected(item);
    }

    public static boolean isSelected(VerseItem item) {
        return SELECTED_ITEMS.containsKey(item.getReference());
    }

    public static boolean isSelectedItemsEmpty() {
        return SELECTED_ITEMS.isEmpty();
    }

    public static Collection<VerseItem> getSelectedItems() {
        return SELECTED_ITEMS.values();
    }

    public static boolean clearSelectedItems() {
        SELECTED_ITEMS.clear();
        return (SELECTED_ITEMS.size() == 0);
    }

    public static class VerseItem {

        private final int    mChapterNumber;
        private final int    mVerseNumber;
        private final int    mBookNumber;
        private final String mVerseText;

        VerseItem(int bookNumber, int chapterNumber, int verseNumber, String verseText) {
            mBookNumber = bookNumber;
            mChapterNumber = chapterNumber;
            mVerseNumber = verseNumber;
            mVerseText = verseText;
        }

        String getReference() {
            return getBookNumber() + Constants.DELIMITER_IN_REFERENCE +
                   getChapterNumber() + Constants.DELIMITER_IN_REFERENCE +
                   getVerseNumber() + Constants.DELIMITER_IN_REFERENCE;
        }

        @Override
        public String toString() {
            return getReference() + " - " + getVerseText();
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

        public String getBookName() {
            BooksList.BookItem bookItem = BooksList.getBookItem(getBookNumber());
            return (null == bookItem) ? "" : bookItem.getBookName();
        }
    }
}
