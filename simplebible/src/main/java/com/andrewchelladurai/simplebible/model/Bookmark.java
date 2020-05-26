package com.andrewchelladurai.simplebible.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bookmark {

  private static final String TAG = "Bookmark";

  public static final String REFERENCE_SEPARATOR = "~";

  @NonNull
  private final String reference;

  @NonNull
  private final String note;

  @NonNull
  private final ArrayList<Verse> verseList = new ArrayList<>(0);

  public Bookmark(@NonNull EntityBookmark entityBookmark,
                  @NonNull final ArrayList<Verse> verseList) {
    reference = entityBookmark.getReference();
    note = entityBookmark.getNote();

    this.verseList.clear();
    this.verseList.addAll(verseList);
  }

  @NonNull
  public String getReference() {
    return reference;
  }

  @NonNull
  public String getNote() {
    return note;
  }

  @NonNull
  public List<Verse> getVerseList() {
    return verseList.subList(0, verseList.size());
  }

  @NonNull
  public static String[] splitBookmarkReference(@NonNull final String reference) {
    if (validateBookmarkReference(reference)) {
      return reference.split(REFERENCE_SEPARATOR);
    }
    return new String[0];
  }

  public static boolean validateBookmarkReference(@NonNull final String reference) {
    final String[] verseList = reference.split(REFERENCE_SEPARATOR);

    if (verseList.length < 1) {
      Log.e(TAG, "validateBookmarkReference:",
            new IllegalArgumentException("Bookmark reference[" + reference + "] has no verses "
                                         + "after splitting it using separator ["
                                         + REFERENCE_SEPARATOR + "]"));
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

  @Nullable
  public static String createBookmarkReference(@NonNull final Collection<Verse> list) {
    if (list.isEmpty()) {
      Log.d(TAG, "handleActionBookmark: list is null or empty");
      return null;
    }

    final StringBuilder reference = new StringBuilder();
    for (final Verse verse : list) {
      reference.append(verse.getReference())
               .append(REFERENCE_SEPARATOR);
    }

    // remove the last appended SEPARATOR_BOOKMARK_REFERENCE
    final int refLength = reference.length();
    reference.delete(refLength - REFERENCE_SEPARATOR.length(), refLength);

    return reference.toString();
  }

}
