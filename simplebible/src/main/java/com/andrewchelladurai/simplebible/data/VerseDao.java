/*
 *
 * This file 'VerseDao.java' is part of SimpleBible :
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

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 15-Sep-2018 @ 3:55 PM
 */
@Dao
public interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createVerse(@NonNull Verse verse);

    @Query("select * from BIBLEVERSES"
           + " where BOOKNUMBER = :book"
           + " and CHAPTERNUMBER = :chapter"
           + " and VERSENUMBER = :verse"
           + " order by VERSENUMBER ASC")
    LiveData<List<Verse>> getVerseForReference(@IntRange(from = 1, to = 66) int book,
                                               @IntRange(from = 1) int chapter,
                                               @IntRange(from = 1) int verse);

    @Query("select * from BIBLEVERSES"
           + " where BOOKNUMBER = :book"
           + " and CHAPTERNUMBER = :chapter"
           + " order by VERSENUMBER ASC")
    LiveData<List<Verse>> getChapter(@IntRange(from = 1, to = 66) int book,
                                     @IntRange(from = 1) int chapter);

    @Query("select * from BIBLEVERSES"
           + " where VERSETEXT LIKE :text"
           + " order by BOOKNUMBER, CHAPTERNUMBER , VERSENUMBER ASC")
    LiveData<List<Verse>> getVersesContainingText(@NonNull String text);

    @Query("select * from BIBLEVERSES"
           + " where BOOKNUMBER in (:bookList)"
           + " and CHAPTERNUMBER in (:chapterList)"
           + " and VERSENUMBER in (:verseList)"
           + " order by BOOKNUMBER, CHAPTERNUMBER , VERSENUMBER ASC")
    LiveData<List<Verse>> getVersesForBookmarkReference(@NonNull List<Integer> bookList,
                                                        @NonNull List<Integer> chapterList,
                                                        @NonNull List<Integer> verseList);

    @Query("select distinct count(BOOKNUMBER||CHAPTERNUMBER||VERSENUMBER)"
           + " from BIBLEVERSES")
    int getNumberOfRecords();

    @Query("delete from BIBLEVERSES")
    void deleteAllRecords();
}
