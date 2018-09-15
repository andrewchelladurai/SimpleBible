package com.andrewchelladurai.simplebible.data.entities;

import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.andrewchelladurai.simplebible.data.entities.Verse.createReference;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 8:39 PM.
 */

@Entity(tableName = "BOOKMARKS")
public class Bookmark {

    private static final String TAG = "Bookmark";

    public static String SEPARATOR = "~";

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "REFERENCE")
    private String reference;

    @NonNull
    @ColumnInfo(name = "NOTE")
    private String note;

    public Bookmark(final String reference, final String note) {
        this.reference = reference;
        this.note = note;
    }

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

    @NonNull
    public static String createBookmarkReference(@NonNull final ArrayList<Verse> list) {
        if (list.isEmpty()) {
            Log.e(TAG, "createBookmarkReference: Empty list passed");
            return "";
        }
        final StringBuilder value = new StringBuilder();
        for (Verse verse : list) {
            value.append(createReference(verse.getBook(), verse.getChapter(), verse.getVerse()))
                 .append(SEPARATOR);
        }
        value.deleteCharAt(value.lastIndexOf(SEPARATOR));
        return value.toString();
    }
}
