package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;

@Dao
public interface BookmarkDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createRecord(@NonNull Bookmark bookmark);

  @Query("select count(`references`) from sb_bookmarks where `references` like :reference")
  int doesRecordExist(@NonNull String reference);

  @Query("select count(*) from sb_bookmarks where `references` like :reference")
  LiveData<Integer> doesRecordExistLive(@NonNull String reference);

}
