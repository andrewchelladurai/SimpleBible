package com.andrewchelladurai.simplebible.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.EntityBook;

import java.util.List;
import java.util.TreeMap;

public class Utils {

  private static final String TAG = "Utils";

  public static final int MAX_BOOKS = 66;

  public static final int MAX_VERSES = 31098;

  private static final Utils THIS_INSTANCE = new Utils();

  @NonNull
  private static TreeMap<Integer, EntityBook> CACHE_BOOKS_MAP = new TreeMap<>();

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
                 + "1=[" + CACHE_BOOKS_MAP.get(1) + "]\n"
                 + maxCount + "=[" + CACHE_BOOKS_MAP.get(maxCount) + "]");
      return;
    }

    CACHE_BOOKS_MAP.clear();
    for (final EntityBook book : bookList) {
      CACHE_BOOKS_MAP.put(book.getNumber(), book);
    }

    Log.d(TAG, "updateCacheBooks: updated [" + CACHE_BOOKS_MAP.size() + "] records\n"
               + "1=[" + CACHE_BOOKS_MAP.get(1) + "]\n"
               + maxCount + "=[" + CACHE_BOOKS_MAP.get(maxCount) + "]");
  }

  public boolean isCacheUpdated() {
    return CACHE_BOOKS_MAP.size() == MAX_BOOKS;
  }

}
