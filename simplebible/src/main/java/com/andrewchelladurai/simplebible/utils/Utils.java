package com.andrewchelladurai.simplebible.utils;

import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class Utils {

  public static final int TOTAL_BOOKS = 66;
  public static final int TOTAL_VERSES = 31098;
  public static final String VERSE_ASSET_FILE = "init_data_translation_web.txt";
  public static final String VERSE_ASSET_FILE_RECORD_SEPARATOR = "~";
  public static final int VERSE_ASSET_FILE_RECORD_SEPARATOR_COUNT = 4;
  public static final String VERSE_REFERENCE_SEPARATOR = ":";

  private static final String TAG = "Utils";

  @NonNull
  public static String createVerseReference(
      @IntRange(from = 1, to = TOTAL_BOOKS) final int bookNumber,
      @IntRange(from = 1) final int chapterNumber,
      @IntRange(from = 1) final int verseNumber) {
    if (bookNumber > 0
        && bookNumber <= TOTAL_BOOKS
        && chapterNumber > 0
        && verseNumber > 0) {

      return bookNumber + VERSE_REFERENCE_SEPARATOR
             + chapterNumber + VERSE_REFERENCE_SEPARATOR
             + verseNumber;
    }
    throw new IllegalArgumentException("one of the reference arguments is invalid");
  }

  public static boolean validateVerseReference(@NonNull final String reference) {
    if (reference.isEmpty()) {
      Log.e(TAG, "validateVerseReference: empty reference");
      return false;
    }

    if (!reference.contains(VERSE_REFERENCE_SEPARATOR)) {
      Log.e(TAG, "validateVerseReference: [" + reference
                 + "] does not have [" + VERSE_REFERENCE_SEPARATOR + "] ");
      return false;
    }

    final String[] splits = reference.split(VERSE_REFERENCE_SEPARATOR);
    if (splits.length != 3) {
      Log.e(TAG, "validateVerseReference: [" + reference + "] does not have 3 parts");
      return false;
    }

    int value;
    for (final String split : splits) {
      try {
        value = Integer.parseInt(split);
      } catch (NumberFormatException e) {
        Log.e(TAG, "validateVerseReference: part of [" + reference + "] is NAN", e);
        return false;
      }
    }
    value = Integer.parseInt(splits[0]);
    if (value < 1 || value > 66) {
      Log.e(TAG, "validateVerseReference: book part of [" + reference + "] is invalid");
      return false;
    }

    value = Integer.parseInt(splits[1]);
    if (value < 1) {
      Log.e(TAG, "validateVerseReference: chapter part of [" + reference + "] is invalid");
      return false;
    }

    value = Integer.parseInt(splits[2]);
    if (value < 1) {
      Log.e(TAG, "validateVerseReference: verse part of [" + reference + "] is invalid");
      return false;
    }

    return true;
  }

  @NonNull
  public static int[] splitVerseReference(@NonNull final String reference) {
    if (!validateVerseReference(reference)) {
      throw new UnsupportedOperationException(
          TAG + " splitVerseReference: invalid reference [" + reference + "]");
    }

    final String[] splits = reference.split(VERSE_REFERENCE_SEPARATOR);
    int[] parts = new int[splits.length];
    for (int i = 0; i < parts.length; i++) {
      parts[i] = Integer.parseInt(splits[i]);
    }

    return parts;
  }

}
