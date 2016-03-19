/*
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
 */

package com.andrewchelladurai.simplebible.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkedVerseList {

    public static final List<BookmarkedVerse> ITEMS = new ArrayList<>();
    public static final Map<String, BookmarkedVerse> ITEM_MAP = new HashMap<>();

    static {
        for (int i = 1; i <= 25; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(BookmarkedVerse item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.verseID, item);
    }

    private static BookmarkedVerse createDummyItem(int position) {
        String id = "Verse " + position;
        String verse = "Verse @ Position : " + position;
        String notes = "Notes Yes / No";
        return new BookmarkedVerse(id, verse, notes);
    }

    public static class BookmarkedVerse {
        public final String verseID;
        public final String verseText;
        public final String verseNotes;

        public BookmarkedVerse(String id, String verse, String notes) {
            verseID = id;
            verseText = verse;
            verseNotes = notes;
        }

        public String toString() {
            return "ID : " + verseID + " Verse : " + verseText + " Notes" + verseNotes;
        }
    }
}
