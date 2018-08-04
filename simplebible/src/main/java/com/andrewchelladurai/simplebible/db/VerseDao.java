package com.andrewchelladurai.simplebible.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:13 PM.
 */

@Dao
public interface VerseDao {

    @Query("select distinct count(BOOKNUMBER||CHAPTERNUMBER||VERSENUMBER) from BIBLEVERSES")
    int getTotalRecordCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    boolean createNewVerse(@NonNull Verse verse);

}
