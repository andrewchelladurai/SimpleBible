/*
 *
 * This file 'VerseList.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
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

package com.andrewchelladurai.simplebible;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerseList {

    public static final  List<Entry>        ITEMS    = new ArrayList<>();
    public static final  Map<String, Entry> ITEM_MAP = new HashMap<>();
    private static final int                COUNT    = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            Entry entry = new Entry(String.valueOf(i), "Item " + i);
            ITEMS.add(entry);
            ITEM_MAP.put(entry.mReference, entry);
        }
    }

    public static class Entry
            implements Parcelable {

        public final String mReference;
        public final String mContent;

        public Entry(String id, String content) {
            mReference = id;
            mContent = content;
        }

        protected Entry(Parcel in) {
            mReference = in.readString();
            mContent = in.readString();
        }

        @Override
        public String toString() {
            return mContent;
        }

        public static final Creator<Entry> CREATOR = new Creator<Entry>() {

            @Override
            public Entry createFromParcel(Parcel in) {
                return new Entry(in);
            }

            @Override
            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mReference);
            dest.writeString(mContent);
        }
    }
}
