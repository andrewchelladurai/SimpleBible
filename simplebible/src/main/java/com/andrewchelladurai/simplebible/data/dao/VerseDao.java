package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

@Dao
public interface VerseDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createRecord(@NonNull Verse verse);

  @Query("select count(book||chapter||verse) from sb_verses")
  int getRecordCount();

  @Query("select * from sb_verses "
         + "where book=:book and chapter=:chapter and verse=:verse")
  Verse getRecord(@IntRange(from = 1, to = 66) int book,
                  @IntRange(from = 1) int chapter,
                  @IntRange(from = 1) int verse);

  @Query("select * from sb_verses "
         + "where book=:book and chapter=:chapter and verse=:verse")
  LiveData<Verse> getRecordLive(@IntRange(from = 1, to = 66) int book,
                                @IntRange(from = 1) int chapter,
                                @IntRange(from = 1) int verse);

  @Query("select * from sb_verses "
         + "where lower(text) like lower(:text) order by book, chapter, verse")
  LiveData<List<Verse>> getRecordsWithText(@NonNull String text);

  @Query("select * from sb_verses "
         + "where book=:book and chapter=:chapter order by verse asc")
  LiveData<List<Verse>> getRecordsOfChapter(@IntRange(from = 1, to = 66) int book,
                                            @IntRange(from = 1) int chapter);

}
