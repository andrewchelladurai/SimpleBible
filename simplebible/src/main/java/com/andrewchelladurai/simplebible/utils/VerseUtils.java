package com.andrewchelladurai.simplebible.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.VerseEntity;

public class VerseUtils {

  public static final int EXPECTED_COUNT = 31098;
  public static final String SETUP_FILE = "init_data_translation_web.txt";
  public static final String SETUP_FILE_RECORD_SEPARATOR = "~";
  public static final int SETUP_FILE_RECORD_SEPARATOR_COUNT = 4;
  public static final String SEPARATOR = ":";

  private static final String TAG = "VerseUtils";

  private VerseUtils() {
  }

  static String createReference(@NonNull final VerseEntity verse) {
    final int bookNumber = verse.getBook();
    final int chapterNumber = verse.getChapter();
    final int verseNumber = verse.getVerse();

    if (bookNumber > 0
        && bookNumber <= BookUtils.EXPECTED_COUNT
        && chapterNumber > 0
        && verseNumber > 0) {

      return bookNumber + SEPARATOR + chapterNumber + SEPARATOR + verseNumber;
    }
    throw new IllegalArgumentException("one of the reference arguments is invalid");
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

    final Integer[] value = new Integer[1];

    for (final String split : splits) {
      try {
        value[0] = Integer.parseInt(split);
      } catch (NumberFormatException e) {
        Log.e(TAG, "validateReference: part of [" + reference + "] is NAN", e);
        return false;
      }
    }
    value[0] = Integer.parseInt(splits[0]);
    if (value[0] < 1 || value[0] > 66) {
      Log.e(TAG, "validateReference: book part of [" + reference + "] is invalid");
      return false;
    }

    value[0] = Integer.parseInt(splits[1]);
    if (value[0] < 1) {
      Log.e(TAG, "validateReference: chapter part of [" + reference + "] is invalid");
      return false;
    }

    value[0] = Integer.parseInt(splits[2]);
    if (value[0] < 1) {
      Log.e(TAG, "validateReference: verse part of [" + reference + "] is invalid");
      return false;
    }

    return true;
  }

}
