package com.andrewchelladurai.simplebible.data.dao;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:01 PM.
 */

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createRecord(@NonNull Book book);

    @Query("Select * from BOOKSTATS where BOOKNUMBER=:bookNumber order by BOOKNUMBER")
    LiveData<List<Book>> readRecord(@IntRange(from = 1, to = 66) int bookNumber);

    @Update
    void updateRecord(@NonNull Book book);

    @Delete
    void deleteRecord(@NonNull Book book);

    @Query("select * from BOOKSTATS order by BOOKNUMBER")
    LiveData<List<Book>> getAllRecords();

    @Query("Select * from BOOKSTATS where BOOKNUMBER=:bookNumber order by BOOKNUMBER")
    LiveData<List<Book>> getRecordsContainingKey(@IntRange(from = 1, to = 66) int bookNumber);

    @Query("select distinct count(BOOKNUMBER) from BOOKSTATS")
    int getNumberOfRecords();

    @Query("delete from BOOKSTATS")
    void deleteAllRecords();
}
