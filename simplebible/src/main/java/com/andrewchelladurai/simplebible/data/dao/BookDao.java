package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andrewchelladurai.simplebible.data.entity.EntityBook;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.List;

@Dao
public interface BookDao {

  int maxBookNumber = BookUtils.EXPECTED_COUNT;

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  @Query("select count(number) from sb_books;")
  int getBookCount();

  @Query("select * from sb_books where number=:bookNumber;")
  EntityBook getBookUsingPosition(@IntRange(from = 1, to = maxBookNumber) int bookNumber);

  @Query("select * from sb_books where number=:bookNumber;")
  LiveData<EntityBook> getBookUsingPositionLive(
      @IntRange(from = 1, to = maxBookNumber) int bookNumber);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createBook(@NonNull EntityBook book);

  @Query("select * from sb_books order by number asc")
  LiveData<List<EntityBook>> getAllBooksLive();

}
