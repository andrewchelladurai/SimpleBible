package com.andrewchelladurai.simplebible.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:01 PM.
 */

@Dao
public interface BookDao {

    @Query("select distinct count(BOOKNUMBER) from BOOKSTATS")
    int getRecordCount();

    @Query("select * from BOOKSTATS order by BOOKNUMBER")
    List<Book> getAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewBook(@SuppressWarnings("NullableProblems") @NonNull Book book);

}
