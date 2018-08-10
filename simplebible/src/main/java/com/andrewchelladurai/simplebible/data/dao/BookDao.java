package com.andrewchelladurai.simplebible.data.dao;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:01 PM.
 */

@Dao
public interface BookDao {

    @Query("select distinct count(BOOKNUMBER) from BOOKSTATS")
    int getRecordCount();

    @Query("select * from BOOKSTATS order by BOOKNUMBER")
    List<Book> getAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewBook(@SuppressWarnings("NullableProblems") @NonNull Book book);

    @Query("delete from BOOKSTATS")
    void deleteAllRecords();
}
