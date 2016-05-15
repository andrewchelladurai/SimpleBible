package com.andrewchelladurai.simplebible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult {

    public static final List<Verse> ITEMS = new ArrayList<>();
    public static final Map<String, Verse> ITEM_MAP = new HashMap<>();
    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(i, i, i, "Verse @ " + i + ":" + i + ":" + i);
        }
    }

    private static void addItem(int pBook, int pChapter, int pVerse, String pText) {
        Verse item = new Verse(pBook, pChapter, pVerse, pText);
        ITEMS.add(item);
        ITEM_MAP.put(item.getVerseID(), item);
    }

    public static class Verse {

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

        public String getVerseReference() {
            return getBookNumber() + ":" + getChapterNumber() + ":" + getVerseNumber();
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

        public String getVerseText() {
            return mVerseText;
        }
    }
}
