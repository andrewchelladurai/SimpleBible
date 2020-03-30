package com.andrewchelladurai.simplebible.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.andrewchelladurai.simplebible.data.EntityBook;

import java.util.List;

public class Utils {

  private static final String TAG = "Utils";

  public static final int MAX_BOOKS = 66;

  public static final int MAX_VERSES = 31098;

  private static final Utils THIS_INSTANCE = new Utils();

  @NonNull
  private static ArraySet<EntityBook> CACHED_BOOKS_SET = new ArraySet<>(Utils.MAX_BOOKS);

  private Utils() {
  }

  public static Utils getInstance() {
    return THIS_INSTANCE;
  }

  public void updateCacheBooks(@NonNull final List<EntityBook> bookList) {
    Log.d(TAG, "updateCacheBooks:");
    final int maxCount = MAX_BOOKS;

    if (isCacheUpdated()) {
      Log.d(TAG, "updateCacheBooks: already contains " + maxCount + "records\n"
                 + "1=[" + CACHED_BOOKS_SET.valueAt(0) + "]\n"
                 + maxCount + "=[" + CACHED_BOOKS_SET.valueAt(maxCount - 1) + "]");
      return;
    }

    CACHED_BOOKS_SET.clear();
    CACHED_BOOKS_SET.addAll(bookList);

    Log.d(TAG, "updateCacheBooks: updated [" + CACHED_BOOKS_SET.size() + "] records\n"
               + "1=[" + CACHED_BOOKS_SET.valueAt(0) + "]\n"
               + maxCount + "=[" + CACHED_BOOKS_SET.valueAt(maxCount - 1) + "]");
  }

  public boolean isCacheUpdated() {
    return CACHED_BOOKS_SET.size() == MAX_BOOKS;
  }

}
