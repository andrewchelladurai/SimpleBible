package com.andrewchelladurai.simplebible.data.dao;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;

import java.util.List;

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
 * on : 04-Aug-2018 @ 9:06 PM.
 */

@Dao
public interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createRecord(@NonNull Bookmark bookmark);

    @Query("select * from BOOKMARKS where REFERENCE=:reference")
    LiveData<List<Bookmark>> readRecord(@NonNull String reference);

    @Update
    void updateRecord(@NonNull Bookmark bookmark);

    @Delete
    void deleteRecord(@NonNull Bookmark bookmark);

    @Query("select * from BOOKMARKS order by REFERENCE")
    LiveData<List<Bookmark>> getAllRecords();

    @Query("select * from BOOKMARKS where REFERENCE LIKE :likeReference")
    LiveData<List<Bookmark>> getRecordsContainingKey(@NonNull String likeReference);

    @Query("select count(distinct REFERENCE) from BOOKMARKS")
    int getNumberOfRecords();

    @Query("delete from BOOKMARKS")
    void deleteAllRecords();

}
