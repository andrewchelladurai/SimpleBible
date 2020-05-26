package com.andrewchelladurai.simplebible.data.dao;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.data.entities.EntityBook;
import com.andrewchelladurai.simplebible.data.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.data.entities.EntityVerse;

import java.util.List;

@SuppressWarnings("NullableProblems")
@Dao
public interface SbDao {

  @Query("select sum(record_count) from ("
         + " select count(number) as record_count from sb_books "
         + " where lower(name) = lower(:bookNameFirst) and number = 1 "
         + " union all "
         + " select count(number) as record_count from sb_books "
         + " where lower(name) = lower(:bookNameLast) and number = 66 "
         + " union all "
         + " select count(distinct number) as record_count from sb_books "
         + " where number = :lastBookNumber "
         + " union all "
         + " select count(distinct book||'~'||chapter||'~'||verse) as record_count from sb_verses "
         + " where book||'~'||chapter||'~'||verse = :lastVerseReference"
         + " );")
  LiveData<Integer> validateTableData(@NonNull final String bookNameFirst,
                                      @NonNull final String bookNameLast,
                                      @NonNull final String lastBookNumber,
                                      @NonNull final String lastVerseReference);

  @Insert(entity = EntityBook.class, onConflict = OnConflictStrategy.REPLACE)
  void createBook(@NonNull EntityBook entityBook);

  @Insert(entity = EntityVerse.class, onConflict = OnConflictStrategy.REPLACE)
  void createVerse(@NonNull EntityVerse entityVerse);

  @Insert(entity = EntityBookmark.class, onConflict = OnConflictStrategy.REPLACE)
  void createBookmark(@NonNull EntityBookmark entityBookmark);

  @Delete(entity = EntityBookmark.class)
  void deleteBookmark(@NonNull EntityBookmark entityBookmark);

  @Query("select * from sb_books order by number")
  LiveData<List<EntityBook>> getAllBookRecords();

  @Query("select * from sb_verses "
         + "where book=:bookNum and chapter=:chapterNum and verse=:verseNumb")
  LiveData<EntityVerse> getVerse(@IntRange(from = 1, to = Book.MAX_BOOKS) final int bookNum,
                                 @IntRange(from = 1) final int chapterNum,
                                 @IntRange(from = 1) final int verseNumb);

  @Query("select * from sb_verses "
         + "where book=:bookNum and chapter=:chapterNum order by verse asc")
  LiveData<List<EntityVerse>> getChapter(
      @IntRange(from = 1, to = Book.MAX_BOOKS) final int bookNum,
      @IntRange(from = 1) final int chapterNum);

  @Query("select * from sb_verses where lower(text) like lower(:text)")
  LiveData<List<EntityVerse>> findVersesContainingText(@NonNull String text);

  @Query("select * from sb_verses"
         + " where book in (:bookNumbers)"
         + " and chapter in (:chapterNumbers)"
         + " and verse in (:verseNumbers)"
         + " order by book, chapter, verse")
  LiveData<List<EntityVerse>> getVerses(@NonNull List<Integer> bookNumbers,
                                        @NonNull List<Integer> chapterNumbers,
                                        @NonNull List<Integer> verseNumbers);

  @Query("select * from sb_bookmarks where reference=:reference")
  LiveData<EntityBookmark> getBookmarkForReference(@NonNull String reference);

  @Query("select * from sb_bookmarks")
  LiveData<List<EntityBookmark>> getAllBookmarks();

}
