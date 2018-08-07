package com.andrewchelladurai.simplebible.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 8:39 PM.
 */

@Entity(tableName = "BOOKMARKS")
public class Bookmark {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "REFERENCE")
    private String reference = "";

    @NonNull
    @ColumnInfo(name = "NOTE")
    private String note = "";

    @NonNull
    public String getReference() {
        return reference;
    }

    public void setReference(@NonNull String reference) {
        this.reference = reference;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public void setNote(@NonNull String note) {
        this.note = note;
    }
}
