package com.andrewchelladurai.simplebible.data.dao;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:01 PM.
 */

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createBook(@NonNull Book book);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBook(@NonNull Book book);

    @Delete
    void deleteBook(@NonNull Book book);

    @Query("select * from BOOKSTATS order by BOOKNUMBER")
    LiveData<List<Book>> getAllRecords();

    @Query("Select * from BOOKSTATS where BOOKNUMBER=:bookNumber order by BOOKNUMBER")
    LiveData<List<Book>> getBookUsingNumber(@IntRange(from = 1, to = 66) int bookNumber);

    @Query("Select * from BOOKSTATS where BOOKNAME=:bookName order by BOOKNUMBER")
    LiveData<List<Book>> getBookUsingName(String bookName);

    @Query("select distinct count(BOOKNUMBER) from BOOKSTATS")
    int getNumberOfBooks();

    @Query("delete from BOOKSTATS")
    void deleteAllRecords();
}
