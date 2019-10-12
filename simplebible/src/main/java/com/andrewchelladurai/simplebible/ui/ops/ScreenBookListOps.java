package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.Book;

public interface ScreenBookListOps {

  @NonNull
  String getFormattedBookDetails(@IntRange(from = 1) int chapterCount);

  void handleBookClick(@NonNull Book book);

}
