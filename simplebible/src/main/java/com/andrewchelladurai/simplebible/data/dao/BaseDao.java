package com.andrewchelladurai.simplebible.data.dao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 18-Aug-2018 @ 7:38 PM.
 */
interface BaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createRecord(@NonNull Object object);

    @Query("Select * from TABLE_NAME where PRIMARY_KEY=:reference order by PRIMARY_KEY")
    LiveData<List<Object>> readRecord(String reference);

    @Update
    void updateRecord(@NonNull Object object);

    @Delete
    void deleteRecord(@NonNull Object object);

    @Query("select * from TABLE_NAME order by PRIMARY_KEY")
    LiveData<List<Object>> getAllRecords();

    @Query("Select * from TABLE_NAME where PRIMARY_KEY=:reference order by PRIMARY_KEY")
    LiveData<List<Object>> getRecordsContainingKey(String reference);

    @Query("select distinct count(PRIMARY_KEY) from TABLE_NAMES")
    int getNumberOfRecords();

    @Query("delete from TABLE_NAME")
    void deleteAllRecords();

}
