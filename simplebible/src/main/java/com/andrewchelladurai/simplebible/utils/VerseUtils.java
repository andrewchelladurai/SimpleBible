package com.andrewchelladurai.simplebible.utils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class VerseUtils {

  public static final int EXPECTED_COUNT = 31098;

  private static final char SEPARATOR = ':';

  @NonNull
  public static String createReference(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber,
      @IntRange(from = 1) final int chapterNumber,
      @IntRange(from = 1) final int verseNumber) {
    if (bookNumber > 0
        && bookNumber <= BookUtils.EXPECTED_COUNT
        && chapterNumber > 0
        && verseNumber > 0) {
      return String.valueOf(bookNumber + SEPARATOR + chapterNumber + SEPARATOR + verseNumber);
    }
    throw new IllegalArgumentException("one of the reference arguments is invalid");
  }

}
