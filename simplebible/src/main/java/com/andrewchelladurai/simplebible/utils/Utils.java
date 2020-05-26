package com.andrewchelladurai.simplebible.utils;

import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.data.Verse;
import com.andrewchelladurai.simplebible.data.entities.EntityBook;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Utils {

  private static final String TAG = "Utils";

  public static final int MAX_BOOKS = 66;

  private static final Utils THIS_INSTANCE = new Utils();

  public static final String SEPARATOR_BOOKMARK_REFERENCE = "~";

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
      Log.d(TAG, "updateCacheBooks: already contains " + maxCount + " records\n"
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

  @NonNull
  public String[] splitBookmarkReference(@NonNull final String reference) {
    if (validateBookmarkReference(reference)) {
      return reference.split(SEPARATOR_BOOKMARK_REFERENCE);
    }
    return new String[0];
  }

  @Nullable
  public EntityBook getCachedBook(@IntRange(from = 1, to = MAX_BOOKS) final int bookNumber) {
    return CACHE_BOOKS_MAP.get(bookNumber);
  }

  @Nullable
  public String createBookmarkReference(@NonNull final Collection<Verse> list) {
    if (list.isEmpty()) {
      Log.d(TAG, "handleActionBookmark: list is null or empty");
      return null;
    }

    final StringBuilder reference = new StringBuilder();
    for (final Verse verse : list) {
      reference.append(verse.getReference())
               .append(SEPARATOR_BOOKMARK_REFERENCE);
    }

    // remove the last appended SEPARATOR_BOOKMARK_REFERENCE
    final int refLength = reference.length();
    reference.delete(refLength - SEPARATOR_BOOKMARK_REFERENCE.length(), refLength);

    return reference.toString();
  }

  @NonNull
  public Set<Integer> getCachedBookList() {
    return CACHE_BOOKS_MAP.keySet();
  }

  public boolean validateBookmarkReference(@NonNull final String reference) {
    final String[] verseList = reference.split(SEPARATOR_BOOKMARK_REFERENCE);

    if (verseList.length < 1) {
      Log.e(TAG, "validateBookmarkReference:",
            new IllegalArgumentException("Bookmark reference[" + reference + "] has no verses "
                                         + "after splitting it using separator ["
                                         + SEPARATOR_BOOKMARK_REFERENCE + "]"));
      return false;
    }

    for (final String verse : verseList) {
      if (!Verse.validateReference(verse)) {
        Log.e(TAG, "validateBookmarkReference: ", new IllegalArgumentException(
            "Verse reference [" + verse + "] present in bookmark reference ["
            + reference + "] failed validation"
        ));
        return false;
      }
    }

    return true;
  }

}
