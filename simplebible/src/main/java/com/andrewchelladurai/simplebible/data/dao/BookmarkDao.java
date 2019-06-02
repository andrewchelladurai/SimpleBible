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

  @SuppressWarnings("NullableProblems")
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createRecord(@NonNull Bookmark bookmark);

  @SuppressWarnings("NullableProblems")
  @Query("select count(*) from sb_bookmarks where `references` = :reference")
  LiveData<Integer> doesRecordExistLive(@NonNull String reference);

  @SuppressWarnings("NullableProblems")
  @Query("select * from sb_bookmarks where `references` = :reference")
  LiveData<Bookmark> getRecordLive(@NonNull String reference);

}
