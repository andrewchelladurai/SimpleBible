package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.List;

@Dao
public interface VerseDao {

  @Query("select count(book||chapter||verse) from sb_verses;")
  int getVerseCount();

  @Query("select count(book||chapter||verse) from sb_verses;")
  LiveData<Integer> getLiveVerseCount();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createVerse(@NonNull Verse verse);

  @Query("select * from sb_verses"
         + " where book=:book"
         + " and chapter=:chapter"
         + " and verse=:verse")
  LiveData<Verse> getLiveVerse(@IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) int book,
                               @IntRange(from = 1) int chapter,
                               @IntRange(from = 1) int verse);

  @Query("select * from sb_verses"
         + " where lower(text) like :textToSearch"
         + " order by book, chapter, verse")
  LiveData<List<Verse>> getLiveVersesWithText(@NonNull String textToSearch);

  @Query("select * from sb_verses "
         + "where book=:bookNumber and chapter=:chapterNumber "
         + "order by verse")
  LiveData<List<Verse>> getLiveChapterVerses(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) int bookNumber,
      @IntRange(from = 1) int chapterNumber);

}
