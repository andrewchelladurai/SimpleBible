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

    private static final String               TAG      = "SB_ListNotes";
    private static final List<Entry>          ITEMS    = new ArrayList<>();
    private static final Map<String[], Entry> ITEM_MAP = new HashMap<>();

    public static void populate() {
        ITEM_MAP.clear();
        ITEMS.clear();

        DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        ArrayList<String[]> list = dbu.getAllNotes();
        if (list == null) {
            Utilities.throwError(TAG + " getAllNotes == null");
        }
        if (list.size() == 0) {
            return;
        }
        for (String[] items : list) {
            Entry entry = new Entry(items[0], items[1]);
            ITEMS.add(entry);
            ITEM_MAP.put(entry.getReference(), entry);
        }
    }

    public static int getCount() {
        return ITEMS.size();
    }

    public static List<Entry> getEntries() {
        return ITEMS;
    }

    public static class Entry {

        private static final String TAG = "SB_ListNotes.Entry";
        private final String[] reference;
        private final String   notes;

        public Entry(String id, String details) {
            reference = id.split("~");
            notes = details;
        }

        @Override
        public String toString() {
            return getNotes();
        }

        public String getNotes() {
            return notes;
        }

        public String[] getReference() {
            return reference;
        }

    }
}
