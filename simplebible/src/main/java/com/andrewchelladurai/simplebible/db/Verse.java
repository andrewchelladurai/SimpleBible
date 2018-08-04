package com.andrewchelladurai.simplebible.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 8:17 PM.
 */

@Entity(tableName = "BIBLEVERSES",
        primaryKeys = {"BOOKNUMBER", "CHAPTERNUMBER", "VERSENUMBER"})
public class Verse {

    @NonNull
    @ColumnInfo(name = "TRANSLATION")
    private String translation;

    @ColumnInfo(name = "BOOKNUMBER")
    @IntRange(from = 1, to = 66)
    private int book;

    @ColumnInfo(name = "CHAPTERNUMBER")
    @IntRange(from = 1)
    private int chapter;

    @ColumnInfo(name = "VERSENUMBER")
    @IntRange(from = 1)
    private int verse;

    @NonNull
    @ColumnInfo(name = "VERSETEXT")
    private String text;

    public Verse(@NonNull final String translation,
                 @IntRange(from = 1, to = 66) final int book,
                 @IntRange(from = 1) final int chapter,
                 @IntRange(from = 1) final int verse,
                 @NonNull final String text) {
        this.translation = translation;
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.text = text;
    }

    @NonNull
    public String getTranslation() {
        return translation;
    }

    public int getBook() {
        return book;
    }

    public int getChapter() {
        return chapter;
    }

    public int getVerse() {
        return verse;
    }

    @NonNull
    public String getText() {
        return text;
    }

}
