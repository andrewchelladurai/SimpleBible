package com.andrewchelladurai.simplebible.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 8:39 PM.
 */

@Entity(tableName = "BOOKMARKS")
public
class Bookmark {

    public static String SEPARATOR = "~";

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "REFERENCE")
    private String reference;

    @NonNull
    @ColumnInfo(name = "NOTE")
    private String note;

    public
    Bookmark(final String reference, final String note) {
        this.reference = reference;
        this.note = note;
    }

    @NonNull
    public
    String getReference() {
        return reference;
    }

    public
    void setReference(
        @NonNull
            String reference) {
        this.reference = reference;
    }

    @NonNull
    public
    String getNote() {
        return note;
    }

    public
    void setNote(
        @NonNull
            String note) {
        this.note = note;
    }
}
