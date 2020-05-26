package com.andrewchelladurai.simplebible.data;

import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.data.entities.EntityBook;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class Book
    implements Comparable {

  public static final int MAX_BOOKS = 66;

  private static final String TAG = "Book";

  @NonNull
  private static TreeMap<Integer, Book> BOOK_CACHE = new TreeMap<>();

  @NonNull
  private final String description;

  @IntRange(from = 1, to = MAX_BOOKS)
  private final int number;

  @NonNull
  private final String name;

  @IntRange(from = 1)
  private final int chapters;

  @IntRange(from = 1)
  private final int verses;

  @NonNull
  private final String testament;

  public Book(@NonNull EntityBook book) {
    description = book.getDescription();
    number = book.getNumber();
    name = book.getName();
    chapters = book.getChapters();
    verses = book.getVerses();
    testament = book.getTestament();
  }

  public static void updateCacheBooks(@NonNull final List<Book> bookList) {
    Log.d(TAG, "updateCacheBooks:");
    final int maxCount = MAX_BOOKS;

    if (isCacheUpdated()) {
      Log.d(TAG, "updateCacheBooks: already contains " + maxCount + " records\n"
                 + "1=[" + BOOK_CACHE.get(1) + "]\n"
                 + maxCount + "=[" + BOOK_CACHE.get(maxCount) + "]");
      return;
    }

    BOOK_CACHE.clear();
    for (final Book book : bookList) {
      BOOK_CACHE.put(book.getNumber(), book);
    }

    Log.d(TAG, "updateCacheBooks: updated [" + BOOK_CACHE.size() + "] records\n"
               + "1=[" + BOOK_CACHE.get(1) + "]\n"
               + maxCount + "=[" + BOOK_CACHE.get(maxCount) + "]");
  }

  public static boolean isCacheUpdated() {
    return BOOK_CACHE.size() == MAX_BOOKS;
  }

  @IntRange(from = 1, to = MAX_BOOKS)
  public int getNumber() {
    return number;
  }

  @Nullable
  public static Book getCachedBook(@IntRange(from = 1, to = MAX_BOOKS) final int bookNumber) {
    return BOOK_CACHE.get(bookNumber);
  }

  @NonNull
  public static Set<Integer> getCachedBookList() {
    return BOOK_CACHE.keySet();
  }

  @Override
  public int compareTo(@NonNull final Object that) {
    return this.getNumber() - ((Book) that).getNumber();
  }

  @Override
  public boolean equals(final Object newObj) {
    if (this == newObj) {
      return true;
    }

    if (newObj == null || this.getClass() != newObj.getClass()) {
      return false;
    }

    final Book that = (Book) newObj;
    return this.number == that.getNumber()
           && this.chapters == that.getChapters()
           && this.verses == that.getVerses()
           && this.name.equals(that.getName())
           && this.testament.equals(that.getTestament())
           && this.description.equals(that.getDescription());
  }

  @IntRange(from = 1)
  public int getChapters() {
    return chapters;
  }

  @IntRange(from = 1)
  public int getVerses() {
    return verses;
  }

  @NonNull
  public String getName() {
    return name;
  }

  @NonNull
  public String getTestament() {
    return testament;
  }

  @NonNull
  public String getDescription() {
    return description;
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, name, chapters, verses, testament, description);
  }

  @NonNull
  @Override
  public String toString() {
    return number
           + "-" + name
           + "-" + description
           + "-" + chapters
           + "-" + verses
           + "-" + testament;
  }

}
