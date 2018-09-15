/*
 *
 * This file 'Book.java' is part of SimpleBible :
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
    private final String mBookDescription;

    @ColumnInfo(name = "BOOKNUMBER")
    @IntRange(from = 1,
              to = 66)
    @PrimaryKey
    private final int mBookNumber;

    @ColumnInfo(name = "BOOKNAME")
    @NonNull
    private final String mBookName;

    @ColumnInfo(name = "CHAPTERCOUNT")
    @IntRange(from = 1)
    private final int mBookChapterCount;

    @ColumnInfo(name = "VERSECOUNT")
    @IntRange(from = 1)
    private final int mBookVerseCount;

    public Book(@NonNull final String bookDescription, @IntRange(from = 1,
                                                                 to = 66) final int bookNumber,
                @NonNull final String bookName, @IntRange(from = 1) final int bookChapterCount,
                @IntRange(from = 1) final int bookVerseCount) {
        mBookDescription = bookDescription;
        mBookNumber = bookNumber;
        mBookName = bookName;
        mBookChapterCount = bookChapterCount;
        mBookVerseCount = bookVerseCount;
    }

    public String getBookDescription() {
        return mBookDescription;
    }

    public int getBookNumber() {
        return mBookNumber;
    }

    public String getBookName() {
        return mBookName;
    }

    public int getBookChapterCount() {
        return mBookChapterCount;
    }

    public int getBookVerseCount() {
        return mBookVerseCount;
    }
}
