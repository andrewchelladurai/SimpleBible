/*
 *
 * This file 'Bookmark.java' is part of SimpleBible :
 *
 * Copyright (c) 2018.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible.data;

import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.andrewchelladurai.simplebible.data.Verse.createReference;

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
