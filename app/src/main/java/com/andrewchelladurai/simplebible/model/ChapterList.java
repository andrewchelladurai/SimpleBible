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

package com.andrewchelladurai.simplebible.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterList {

    private static final String                    TAG      = "SB_ChapterList";
    private static final List<ChapterItem>         ITEMS    = new ArrayList<>();
    private static final Map<Integer, ChapterItem> ITEM_MAP = new HashMap<>();

    public static boolean populateListItems(int count, String prependText) {
        boolean returnValue;
        try {
            ITEMS.clear();
            ITEM_MAP.clear();

            for (int i = 1; i <= count; i++) {
                ChapterItem item = new ChapterItem(i, prependText);
                ITEMS.add(item);
                ITEM_MAP.put(item.number, item);
            }
            returnValue = true;
        } catch (Exception ex) {
            returnValue = false;
            Log.d(TAG, "populateListItems: " + ex.getLocalizedMessage());
        }
        return returnValue;
    }

    public static ChapterItem getChapterItem(int chapterNumber) {
        return (ITEM_MAP.containsKey(chapterNumber)) ? ITEM_MAP.get(chapterNumber) : null;
    }

    public static List<ChapterItem> getAllItems() {
        return ITEMS;
    }

    public static class ChapterItem {

        final int    number;
        final String displayText;

        ChapterItem(int id, String prependText) {
            number = id;
            displayText = prependText + " " + number;
        }

        @Override
        public String toString() {
            return getLabel();
        }

        public String getLabel() {
            return displayText;
        }

        public int getChapterNumber() {
            return number;
        }
    }
}
