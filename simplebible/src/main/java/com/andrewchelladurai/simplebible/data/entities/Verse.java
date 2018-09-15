package com.andrewchelladurai.simplebible.data.entities;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 8:17 PM.
 */

@Entity(tableName = "BIBLEVERSES",
        primaryKeys = {"BOOKNUMBER", "CHAPTERNUMBER", "VERSENUMBER"})
public class Verse {

    private static final String TAG = "Verse";

    public static String SEPARATOR = ":";

    @NonNull
    @ColumnInfo(name = "TRANSLATION")
    private final String translation;

    @ColumnInfo(name = "BOOKNUMBER")
    @IntRange(from = 1,
              to = 66)
    private final int book;

    @ColumnInfo(name = "CHAPTERNUMBER")
    @IntRange(from = 1)
    private final int chapter;

    @ColumnInfo(name = "VERSENUMBER")
    @IntRange(from = 1)
    private final int verse;

    @NonNull
    @ColumnInfo(name = "VERSETEXT")
    private final String text;

    private boolean selected = false;

    public Verse(@NonNull final String translation, @IntRange(from = 1,
                                                              to = 66) final int book,
                 @IntRange(from = 1) final int chapter, @IntRange(from = 1) final int verse,
                 @NonNull final String text) {
        this.translation = translation;
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.text = text;
        this.selected = false;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public String getReference() {
        return createReference(book, chapter, verse);
    }

    @Override
    public String toString() {
        return getReference();
    }

    @NonNull
    public static String createReference(@IntRange(from = 1,
                                                   to = 66) int book,
                                         @IntRange(from = 1) int chapter,
                                         @IntRange(from = 1) int verse) {
        if (book < 1 || book > 66 || chapter < 1 || verse < 1) {
            throw new UnsupportedOperationException(TAG + " : createReference: invalid params");
        }
        return book + SEPARATOR + chapter + SEPARATOR + verse;
    }
}
