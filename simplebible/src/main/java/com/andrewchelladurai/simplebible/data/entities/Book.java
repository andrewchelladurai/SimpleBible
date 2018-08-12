package com.andrewchelladurai.simplebible.data.entities;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 8:39 PM.
 */

@Entity(tableName = "BOOKSTATS")
public class Book {

    @ColumnInfo(name = "DESC")
    @NonNull
    private final String description;

    @ColumnInfo(name = "BOOKNUMBER")
    @IntRange(from = 1, to = 66)
    @PrimaryKey
    private final int number;

    @ColumnInfo(name = "BOOKNAME")
    @NonNull
    private final String name;

    @ColumnInfo(name = "CHAPTERCOUNT")
    @IntRange(from = 1)
    private final int chapters;

    @ColumnInfo(name = "VERSECOUNT")
    @IntRange(from = 1)
    private final int verses;

    public Book(@NonNull final String description,
                @IntRange(from = 1, to = 66) final int number,
                @NonNull final String name,
                @IntRange(from = 1) final int chapters,
                @IntRange(from = 1) final int verses) {
        this.description = description;
        this.number = number;
        this.name = name;
        this.chapters = chapters;
        this.verses = verses;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public int getNumber() {
        return number;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getChapters() {
        return chapters;
    }

    public int getVerses() {
        return verses;
    }

}
