package com.andrewchelladurai.simplebible.data;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SbDao {

  @Query("select sum(record_count) from ("
         + " select count(1) as record_count from sb_books "
         + " where lower(name) = lower(:bookNameFirst) and number = 1 "
         + " union all "
         + " select count(1) as record_count from sb_books "
         + " where lower(name) = lower(:bookNameLast) and number = 66 "
         + " union all "
         + " select count(1) as record_count from sb_books "
         + " where number = :lastBookNumber "
         + " union all "
         + " select count(1) as record_count from sb_verses "
         + " where book||''||chapter||''||verse = :lastVerseNumber"
         + " );")
  LiveData<Integer> validateTableData(@NonNull final String bookNameFirst,
                                      @NonNull final String bookNameLast,
                                      @IntRange(from = 1) final int lastBookNumber,
                                      @IntRange(from = 1) final int lastVerseNumber);

  @Insert(entity = EntityBook.class, onConflict = OnConflictStrategy.REPLACE)
  void createBook(EntityBook entityBook);

}
