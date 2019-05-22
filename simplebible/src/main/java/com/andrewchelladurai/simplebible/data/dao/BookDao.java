package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.List;

@Dao
public interface BookDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createRecord(@NonNull Book book);

  @Query("select count(distinct number) from sb_books")
  int getRecordCount();

  @Query("select count(distinct number) from sb_books")
  LiveData<Integer> getLiveBookCount();

  @Query("select * from sb_books "
         + "where number=:number")
  Book getRecord(@IntRange(from = 1, to = 66) final int number);

  @Query("select * from sb_books order by number")
  List<Book> getAllRecordsLive();

}
