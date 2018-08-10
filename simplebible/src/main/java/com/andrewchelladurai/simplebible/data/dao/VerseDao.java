package com.andrewchelladurai.simplebible.data.dao;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import androidx.annotation.NonNull;
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

    @Query("select distinct count(BOOKNUMBER||CHAPTERNUMBER||VERSENUMBER) from BIBLEVERSES")
    int getRecordCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewVerse(@SuppressWarnings("NullableProblems") @NonNull Verse verse);

    @Query("delete from BIBLEVERSES")
    void deleteAllRecords();
}
