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
 * on : 04-Aug-2018 @ 9:06 PM.
 */

@Dao
public interface BookmarkDao {

    @Query("select distinct count(REFERENCE) from BOOKMARKS")
    int getRecordCount();

    @Query("select * from BOOKMARKS order by REFERENCE")
    List<Bookmark> getAllBookmarks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewBookmark(@SuppressWarnings("NullableProblems") @NonNull Bookmark bookmark);

}
