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
    private String translation = "";

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
    private String text = "";

    @NonNull
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(@NonNull String translation) {
        this.translation = translation;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }
}
