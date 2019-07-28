package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Verse;

import java.util.List;

public class ScreenChapterModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenChapterModel";
  private final VerseDao verseDao;

  @NonNull
  private Book book;
  @IntRange(from = 1)
  private int chapter;

  public ScreenChapterModel(@NonNull final Application application) {
    super(application);
    verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
  }

  @NonNull
  public Book getBook() {
    return book;
  }

  public void setBook(@NonNull final Book book) {
    this.book = book;
    Log.d(TAG, "setBook: [" + this.book + "]");
  }

  public void setBookChapter(@IntRange(from = 1) final int chapter) {
    this.chapter = chapter;
    Log.d(TAG, "setBookChapter: [" + chapter + "]");
  }

  @IntRange(from = 1)
  public int getChapter() {
    return chapter;
  }

  @NonNull
  public LiveData<List<Verse>> getChapterVerseList() {
    return verseDao.getLiveChapterVerses(book.getNumber(), getChapter());
  }

}
