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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerseList {

    static final List<VerseItem>        ITEMS    = new ArrayList<>();
    static final Map<String, VerseItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    public static List<VerseItem> getItems() {
        if (ITEMS.isEmpty() || ITEMS.size() == 0) {
            populateList();
        }
        return ITEMS;
    }

    private static void populateList() {
        for (int i = 1; i <= COUNT; i++) {
            VerseItem item = new VerseItem(String.valueOf(i), "Item " + i);
            ITEMS.add(item);
            ITEM_MAP.put(item.content, item);
        }
    }

    public static class VerseItem {

        public final String content;
        public final String details;

        public VerseItem(String content, String details) {
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
