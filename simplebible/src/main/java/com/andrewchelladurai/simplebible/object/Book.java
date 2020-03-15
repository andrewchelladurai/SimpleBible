package com.andrewchelladurai.simplebible.object;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.EntityBook;
import com.andrewchelladurai.simplebible.utils.BookUtils;

public class Book {

  @NonNull
  private EntityBook entityBook;

  public Book(@NonNull final EntityBook entityBook) {
    this.entityBook = entityBook;
  }

  @NonNull
  public String getName() {
    return entityBook.getName();
  }

  @IntRange(from = 1)
  public int getChapters() {
    return entityBook.getChapters();
  }

  @NonNull
  public String getDescription() {
    return entityBook.getDescription();
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  public int getNumber() {
    return entityBook.getNumber();
  }

}
