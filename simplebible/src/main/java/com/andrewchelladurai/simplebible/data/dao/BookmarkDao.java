package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andrewchelladurai.simplebible.data.entity.Bookmark;

import java.util.List;

@Dao
public interface BookmarkDao {

  @Query("select * from sb_bookmarks where reference=:reference")
  LiveData<List<Bookmark>> findBookmarkUsingReference(@NonNull String reference);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void saveBookmark(@NonNull Bookmark bookmark);

}
