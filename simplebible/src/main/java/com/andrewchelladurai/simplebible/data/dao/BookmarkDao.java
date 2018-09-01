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
    void createBookmark(@NonNull Bookmark bookmark);

    @Query("select * from BOOKMARKS where REFERENCE=:reference order by REFERENCE")
    LiveData<List<Bookmark>> queryBookmarkUsingReference(@NonNull String reference);

    @Query("select * from BOOKMARKS where NOTE LIKE note order by REFERENCE")
    LiveData<List<Bookmark>> queryBookmarkUsingNote(@NonNull String note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBookmark(@NonNull Bookmark bookmark);

    @Delete
    void deleteBookmark(@NonNull Bookmark bookmark);

    @Query("select * from BOOKMARKS order by REFERENCE")
    LiveData<List<Bookmark>> getAllBookmarks();

    @Query("select count(distinct REFERENCE) from BOOKMARKS")
    int getNumberOfBookmarks();

    @Query("delete from BOOKMARKS")
    void deleteAllRecords();

}
