package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.EntityBook;
import com.andrewchelladurai.simplebible.data.entity.EntityVerse;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.List;

public class ScreenChapterModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenChapterModel";
  private static EntityBook CACHED_BOOK;
  @IntRange(from = 1)
  private static int CACHED_CHAPTER_NUMBER;
  @IntRange(from = 1)
  private static int CACHED_VERSE_NUMBER;

  private final VerseDao verseDao;
  private final BookDao bookDao;

  public ScreenChapterModel(@NonNull final Application application) {
    super(application);
    verseDao = SbDatabase.getDatabase(getApplication())
                         .getVerseDao();
    bookDao = SbDatabase.getDatabase(getApplication())
                        .getBookDao();
    Log.d(TAG, "ScreenChapterModel:");
  }

  public LiveData<EntityBook> getBook(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return bookDao.getBookUsingPositionLive(bookNumber);
  }

  public void setCachedVerseNumber(@IntRange(from = 1) final int verseNumber) {
    CACHED_VERSE_NUMBER = verseNumber;
  }

  @IntRange(from = 1)
  public int getCachedVerse() {
    return CACHED_VERSE_NUMBER;
  }

  @NonNull
  public EntityBook getCachedBook() {
    return CACHED_BOOK;
  }

  public void setCachedBook(@NonNull final EntityBook book) {
    CACHED_BOOK = book;
  }

  @NonNull
  public LiveData<List<EntityVerse>> getChapterVerseList() {
    return verseDao.getLiveChapterVerses(CACHED_BOOK.getNumber(), getCachedChapterNumber());
  }

  public int getCachedChapterNumber() {
    return CACHED_CHAPTER_NUMBER;
  }

  public void setCachedChapterNumber(@IntRange(from = 1) final int chapter) {
    CACHED_CHAPTER_NUMBER = chapter;
  }

}
