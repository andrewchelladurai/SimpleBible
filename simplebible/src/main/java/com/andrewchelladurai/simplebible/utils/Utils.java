package com.andrewchelladurai.simplebible.utils;

import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.EntityVerse;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class Utils {

  private static final String TAG = "Utils";

  public static final int MAX_BOOKS = 66;

  public static final int MAX_VERSES = 31098;

  private static final Utils THIS_INSTANCE = new Utils();

  public static final String SEPARATOR_VERSE_REFERENCE = ":";

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

  @Nullable
  public int[] splitVerseReference(@NonNull final String reference) {

    if (!validateVerseReference(reference)) {
      return null;
    }

    final String[] parts = reference.split(SEPARATOR_VERSE_REFERENCE);
    return new int[]{
        Integer.parseInt(parts[0]),
        Integer.parseInt(parts[1]),
        Integer.parseInt(parts[2]),
        };
  }

  public boolean validateVerseReference(@NonNull final String reference) {
    Log.d(TAG, "validateVerseReference: reference[" + reference + "]");

    if (reference.isEmpty()) {
      Log.e(TAG, "validateVerseReference: empty reference");
      return false;
    }

    if (!reference.contains(SEPARATOR_VERSE_REFERENCE)) {
      Log.e(TAG, "validateVerseReference: no separator char found");
      return false;
    }

    final String[] part = reference.split(SEPARATOR_VERSE_REFERENCE);
    if (part.length != 3) {
      Log.e(TAG, "validateVerseReference: reference does not contain 3 parts");
    }

    @IntRange(from = 1, to = MAX_BOOKS) final int book;
    @IntRange(from = 1) final int chapter;
    @IntRange(from = 1) final int verse;

    try {
      book = Integer.parseInt(part[0]);
      chapter = Integer.parseInt(part[1]);
      verse = Integer.parseInt(part[2]);
    } catch (NumberFormatException nfe) {
      Log.e(TAG, "validateVerseReference: book, chapter or book is NAN", nfe);
      return false;
    }

    if (book < 1 || book > MAX_BOOKS) {
      Log.e(TAG, "validateVerseReference: invalid book number");
      return false;
    }

    if (chapter < 1) {
      Log.e(TAG, "validateVerseReference: invalid chapter number");
      return false;
    }

    if (verse < 1) {
      Log.e(TAG, "validateVerseReference: invalid verse number");
      return false;
    }

    return true;
  }

  @Nullable
  public EntityBook getCachedBook(@IntRange(from = 1, to = MAX_BOOKS) final int bookNumber) {
    return CACHE_BOOKS_MAP.get(bookNumber);
  }

  @Nullable
  public String createBookmarkReference(@NonNull final Collection<EntityVerse> list) {
    if (list.isEmpty()) {
      Log.d(TAG, "handleActionBookmark: list is null or empty");
      return null;
    }

    final StringBuilder reference = new StringBuilder();
    for (final EntityVerse verse : list) {
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

}
