package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;

@Dao
public interface BookmarkDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createRecord(@NonNull Bookmark bookmark);

}
