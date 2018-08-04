package com.andrewchelladurai.simplebible.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 8:39 PM.
 */

@Entity(tableName = "BOOKSTATS")
public class Book {

    @ColumnInfo(name = "DESC")
    @NonNull
    private String description = "";

    @ColumnInfo(name = "BOOKNUMBER")
    @IntRange(from = 1, to = 66)
    @PrimaryKey
    private int number;

    @ColumnInfo(name = "BOOKNAME")
    @NonNull
    private String name = "";

    @ColumnInfo(name = "CHAPTERCOUNT")
    @IntRange(from = 1)
    private int chapters;

    @ColumnInfo(name = "VERSECOUNT")
    @IntRange(from = 1)
    private int verses;

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public int getVerses() {
        return verses;
    }

    public void setVerses(int verses) {
        this.verses = verses;
    }
}
