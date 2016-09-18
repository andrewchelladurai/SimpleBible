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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterList {

    public static final List<ChapterItem>        ITEMS    = new ArrayList<>();
    public static final Map<String, ChapterItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(ChapterItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ChapterItem createDummyItem(int position) {
        return new ChapterItem(String.valueOf(position), makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Verses for Chapter : ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nThis is a verse.");
        }
        return builder.toString();
    }

    public static class ChapterItem {

        public final String id;
        public final String details;

        public ChapterItem(String id, String details) {
            this.id = id;
            this.details = details;
        }

        @Override
        public String toString() {
            return "Chapter " + id;
        }
    }
}
