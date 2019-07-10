package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.andrewchelladurai.simplebible.data.entity.Book;

public class ScreenChapterModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenChapterModel";

  @NonNull
  private Book book;
  @IntRange(from = 1)
  private int chapter;

  public ScreenChapterModel(@NonNull final Application application) {
    super(application);
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

}
