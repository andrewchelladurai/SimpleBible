package com.andrewchelladurai.simplebible.utilities;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 2:05 AM
 */
public abstract class Constants {

    public static final String SUCCESS                     = "SUCCESS";
    public static final String FAILURE                     = "FAILURE";
    public static final String DELIMITER_IN_REFERENCE      = ":";
    public static final String DELIMITER_BETWEEN_REFERENCE = "~";
//    public static final String REMOVED                     = "REMOVED";
//    public static final String ADDED                       = "ADDED";
//    public static final String PRESENT_IN_DATABASE         = "PRESENT_IN_DATABASE";
//    public static final String ABSENT_IN_DATABASE          = "ABSENT_IN_DATABASE";
//    public static final String BUNDLE_ARG_BOOKMARK_ITEM    = "BOOKMARK_ITEM";
    public static final String DEFAULT_REFERENCE           = "43:3:16"; // John 3:16
    static final        String DATABASE_NAME               = "Bible.db";
    static final        int    DATABASE_VERSION            = 20160921; // 20160922

    static final int VERSE_SIZE_SMALL = -1;
    static final int VERSE_SIZE_MEDIUM = 0;
    static final int VERSE_SIZE_BIG = 1;
    static final int VERSE_SIZE_LARGE = 2;
    static final int VERSE_STYLE_NORMAL = 0;
    static final int VERSE_STYLE_OLD_ENGLISH = 1;
    static final int VERSE_STYLE_TYPEWRITER = 2;

    private Constants() {
    }

    static abstract class SimpleBibleTable {

//        public static final String COLUMN_VERSION        = "VERSION";
        static final        String NAME                  = "BIBLE_VERSES";
        static final        String COLUMN_VERSE_NUMBER   = "VERSE_NUMBER";
        static final        String COLUMN_CHAPTER_NUMBER = "CHAPTER_NUMBER";
        static final        String COLUMN_BOOK_NUMBER    = "BOOK_NUMBER";
        static final        String COLUMN_VERSE_TEXT     = "VERSE_TEXT";
    }

    static abstract class BookmarksTable {

        static final String NAME             = "BOOK_MARKS";
        static final String COLUMN_REFERENCE = "REFERENCE";
        static final String COLUMN_NOTE      = "NOTE";
    }
}
