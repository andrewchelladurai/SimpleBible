package com.andrewchelladurai.simplebible.utils;

import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class VerseUtils {

  public static final int TOTAL_VERSES = 31098;
  public static final String SETUP_FILE = "init_data_translation_web.txt";
  public static final String SETUP_FILE_RECORD_SEPARATOR = "~";
  public static final int SETUP_FILE_RECORD_SEPARATOR_COUNT = 4;
  public static final String SEPARATOR = ":";

  private static final String TAG = "VerseUtils";

  @NonNull
  public static String createReference(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber,
      @IntRange(from = 1) final int chapterNumber,
      @IntRange(from = 1) final int verseNumber) {
    if (bookNumber > 0
        && bookNumber <= BookUtils.EXPECTED_COUNT
        && chapterNumber > 0
        && verseNumber > 0) {

      return bookNumber + SEPARATOR + chapterNumber + SEPARATOR + verseNumber;
    }
    throw new IllegalArgumentException("one of the reference arguments is invalid");
  }

  public static boolean validateReference(@NonNull final String reference) {
    if (reference.isEmpty()) {
      Log.e(TAG, "validateReference: empty reference");
      return false;
    }

    if (!reference.contains(SEPARATOR)) {
      Log.e(TAG, "validateReference: [" + reference + "] does not have [" + SEPARATOR + "] ");
      return false;
    }

    final String[] splits = reference.split(SEPARATOR);
    if (splits.length != 3) {
      Log.e(TAG, "validateReference: [" + reference + "] does not have 3 parts");
      return false;
    }

    int value;
    for (final String split : splits) {
      try {
        value = Integer.parseInt(split);
      } catch (NumberFormatException e) {
        Log.e(TAG, "validateReference: part of [" + reference + "] is NAN", e);
        return false;
      }
    }
    value = Integer.parseInt(splits[0]);
    if (value < 1 || value > 66) {
      Log.e(TAG, "validateReference: book part of [" + reference + "] is invalid");
      return false;
    }

    value = Integer.parseInt(splits[1]);
    if (value < 1) {
      Log.e(TAG, "validateReference: chapter part of [" + reference + "] is invalid");
      return false;
    }

    value = Integer.parseInt(splits[2]);
    if (value < 1) {
      Log.e(TAG, "validateReference: verse part of [" + reference + "] is invalid");
      return false;
    }

    return true;
  }

  @NonNull
  public static int[] splitReference(@NonNull final String reference) {
    if (!validateReference(reference)) {
      throw new UnsupportedOperationException(
          TAG + " splitReference: invalid reference [" + reference + "]");
    }

    final String[] splits = reference.split(SEPARATOR);
    int[] parts = new int[splits.length];
    for (int i = 0; i < parts.length; i++) {
      parts[i] = Integer.parseInt(splits[i]);
    }

    return parts;
  }

}
