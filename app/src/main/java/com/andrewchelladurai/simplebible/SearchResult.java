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
        ITEM_MAP.put(item.getVerseID(), item);
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
            return getVerseID() + " = " + getVerseText();
        }

        public String getVerseID() {
            return getBookNumber() + ":" + getChapterNumber() + ":" + getVerseNumber();
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
            return getBookNumber() + ":" + getChapterNumber() + ":" + getVerseNumber();
        }
    }
}
