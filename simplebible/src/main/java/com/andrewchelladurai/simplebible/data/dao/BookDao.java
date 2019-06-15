package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.utils.BookUtils;

@Dao
public interface BookDao {

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  @Query("select count(number) from sb_books;")
  int getBookCount();

  @Nullable
  @Query("select * from sb_books where number=:bookNumber;")
  Book getBookUsingPosition(@IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) int bookNumber);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createBook(@NonNull Book book);

}
