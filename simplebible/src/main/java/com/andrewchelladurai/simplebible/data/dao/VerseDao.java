package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.andrewchelladurai.simplebible.data.entity.Verse;

@Dao
public interface VerseDao {

  @Query("select count(book||chapter||verse) from sb_verses;")
  int getVerseCount();

  @Query("select count(book||chapter||verse) from sb_verses;")
  LiveData<Integer> getLiveVerseCount();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createVerse(@NonNull Verse verse);

}
