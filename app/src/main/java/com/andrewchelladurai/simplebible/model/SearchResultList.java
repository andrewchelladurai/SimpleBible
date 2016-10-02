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

import android.support.annotation.NonNull;
import android.util.Log;

import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.internal.zzs.TAG;

public class SearchResultList {

    private static final List<SearchResultItem>        ITEMS          = new ArrayList<>();
    private static final Map<String, SearchResultItem> ITEM_MAP       = new HashMap<>();
    private static final Map<String, SearchResultItem> SELECTED_ITEMS = new HashMap<>();
    private static final StringBuilder                 mInput         = new StringBuilder();

    public static boolean populateList(String input, ArrayList<String[]> list) {
        if (mInput.toString().equalsIgnoreCase(input)) {
            Log.d(TAG, "populateList: Already populated list using results for " + input);
            return true;
        } else {
            mInput.delete(0, mInput.length());
            mInput.append(input);
            Log.d(TAG, "populateList: populated list with results for " + input);
        }
        boolean returnValue;
        try {
            ITEM_MAP.clear();
            ITEMS.clear();
            SELECTED_ITEMS.clear();
            int count = list.size();
            SearchResultItem item;
            String[] parts;
            for (int i = 0; i < count; i++) {
                parts = list.get(i);
                item = new SearchResultItem(parts[0], parts[1]);
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

    public static List<SearchResultItem> getItems() {
        return ITEMS;
    }

    public static void clearList() {
        ITEM_MAP.clear();
        ITEMS.clear();
        SELECTED_ITEMS.clear();
        mInput.delete(0, mInput.length());
    }

    public static boolean isItemSelected(@NonNull SearchResultItem item) {
        return SELECTED_ITEMS.containsKey(item.getReference());
    }

    public static boolean updateSelectedItems(@NonNull SearchResultItem item) {
        String reference = item.getReference();
        if (SELECTED_ITEMS.containsKey(reference)) {
            SELECTED_ITEMS.remove(reference);
        } else {
            SELECTED_ITEMS.put(reference, item);
        }
        return isItemSelected(item);
    }

    public static class SearchResultItem {

        private int    mBookNumber    = 0;
        private int    mChapterNumber = 0;
        private int    mVerseNumber   = 0;
        private String mVerseText     = "";

        private SearchResultItem(String reference, String content) {
            String parts[] = Utilities.getReferenceParts(reference);
            if (parts != null) {
                mBookNumber = Integer.parseInt(parts[0]);
                mChapterNumber = Integer.parseInt(parts[1]);
                mVerseNumber = Integer.parseInt(parts[2]);
                mVerseText = content;
            }
        }

        @Override
        public String toString() {
            return getReference() + " - " + getVerseText();
        }

        public String getReference() {
            return Utilities.prepareReferenceString(getBookNumber(), getChapterNumber(),
                                                    getVerseNumber());
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
    }
}
