package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.andrewchelladurai.simplebible.utils.Utils;

public class ChapterViewModel
    extends AndroidViewModel {

  private static final String TAG = "ChapterViewModel";

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  private static int CACHED_BOOK = 1;

  @IntRange(from = 1)
  private static int CACHED_CHAPTER = 1;

  public ChapterViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "ChapterViewModel:");
  }

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  public int getCurrentBook() {
    return CACHED_BOOK;
  }

  public void setCurrentBook(@IntRange(from = 1, to = Utils.MAX_BOOKS) final int book) {
    CACHED_BOOK = book;
  }

  @IntRange(from = 1)
  public int getCurrentChapter() {
    return CACHED_CHAPTER;
  }

  public void setCurrentChapter(@IntRange(from = 1) final int chapter) {
    CACHED_CHAPTER = chapter;
  }

}
