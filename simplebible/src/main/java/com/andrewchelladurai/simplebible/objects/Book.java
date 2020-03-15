package com.andrewchelladurai.simplebible.objects;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.EntityBook;
import com.andrewchelladurai.simplebible.utils.BookUtils;

public class Book {

  @NonNull
  private final EntityBook book;

  private Book(@NonNull EntityBook book) {
    this.book = book;
  }

  @NonNull
  public String getTestament() {
    return book.getTestament();
  }

  @NonNull
  public String getDescription() {
    return book.getDescription();
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  public int getNumber() {
    return book.getNumber();
  }

  @NonNull
  public String getName() {
    return book.getName();
  }

  @IntRange(from = 1)
  public int getChapters() {
    return book.getChapters();
  }

  @IntRange(from = 1)
  public int getVerses() {
    return book.getVerses();
  }

}
