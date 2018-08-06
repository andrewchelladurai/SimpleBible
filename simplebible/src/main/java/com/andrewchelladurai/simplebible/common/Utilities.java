package com.andrewchelladurai.simplebible.common;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 07-Aug-2018 @ 1:22 AM.
 */
public class Utilities {

    public static final  String    FILE_BOOK_DETAILS    = "mainStepsBooks.csv";
    public static final  String    FILE_VERSE_DETAILS   = "mainStepsVerses.csv";
    public static final  String    DATABASE_NAME        = "SimpleBible.db";
    public static final  int       EXPECTED_BOOK_COUNT  = 66; // lines in the file
    public static final  int       EXPECTED_VERSE_COUNT = 31098; // lines in the file
    private static final Utilities ourInstance          = new Utilities();

    private Utilities() {
    }

    public static Utilities getInstance() {
        return ourInstance;
    }
}
