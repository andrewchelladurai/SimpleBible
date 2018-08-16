package com.andrewchelladurai.simplebible.data.dao;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:13 PM.
 */

@Dao
public interface VerseDao {

    @Query("select distinct count(BOOKNUMBER||CHAPTERNUMBER||VERSENUMBER)"
           + " from BIBLEVERSES")
    int getRecordCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewVerse(@SuppressWarnings("NullableProblems") @NonNull Verse verse);

    @Query("delete from BIBLEVERSES")
    void deleteAllRecords();

    @Query("select * from BIBLEVERSES"
           + " where BOOKNUMBER = :book"
           + " and CHAPTERNUMBER = :chapter"
           + " order by VERSENUMBER ASC")
    LiveData<List<Verse>> getChapter(@IntRange(from = 1, to = 66) int book,
                                     @IntRange(from = 1) int chapter);

    @Query("select * from BIBLEVERSES"
           + " where BOOKNUMBER = :book"
           + " and CHAPTERNUMBER = :chapter"
           + " and VERSENUMBER = :verse"
           + " order by VERSENUMBER ASC")
    LiveData<Verse> getVerse(@IntRange(from = 1, to = 66) int book,
                             @IntRange(from = 1) int chapter,
                             @IntRange(from = 1) int verse);

    @Query("select * from BIBLEVERSES"
           + " where VERSETEXT LIKE :text"
           + " order by BOOKNUMBER, CHAPTERNUMBER , VERSENUMBER ASC")
    LiveData<List<Verse>> getVerseWithText(@NonNull String text);

    @Query("select * from BIBLEVERSES"
           + " where BOOKNUMBER in (:bookList)"
           + " and CHAPTERNUMBER in (:chapterList)"
           + " and VERSENUMBER in (:verseList)"
           + " order by BOOKNUMBER, CHAPTERNUMBER , VERSENUMBER ASC")
    LiveData<List<Verse>> getVerses(@NonNull List<Integer> bookList,
                                    @NonNull List<Integer> chapterList,
                                    @NonNull List<Integer> verseList);
}
