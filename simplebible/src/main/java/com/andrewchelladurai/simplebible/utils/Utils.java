package com.andrewchelladurai.simplebible.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.model.Verse;

import java.util.Collection;

public class Utils {

  private static final String TAG = "Utils";

  private static final Utils THIS_INSTANCE = new Utils();

  public static final String SEPARATOR_BOOKMARK_REFERENCE = "~";

  private Utils() {
  }

  public static Utils getInstance() {
    return THIS_INSTANCE;
  }

  @NonNull
  public String[] splitBookmarkReference(@NonNull final String reference) {
    if (validateBookmarkReference(reference)) {
      return reference.split(SEPARATOR_BOOKMARK_REFERENCE);
    }
    return new String[0];
  }

  @Nullable
  public String createBookmarkReference(@NonNull final Collection<Verse> list) {
    if (list.isEmpty()) {
      Log.d(TAG, "handleActionBookmark: list is null or empty");
      return null;
    }

    final StringBuilder reference = new StringBuilder();
    for (final Verse verse : list) {
      reference.append(verse.getReference())
               .append(SEPARATOR_BOOKMARK_REFERENCE);
    }

    // remove the last appended SEPARATOR_BOOKMARK_REFERENCE
    final int refLength = reference.length();
    reference.delete(refLength - SEPARATOR_BOOKMARK_REFERENCE.length(), refLength);

    return reference.toString();
  }

  public boolean validateBookmarkReference(@NonNull final String reference) {
    final String[] verseList = reference.split(SEPARATOR_BOOKMARK_REFERENCE);

    if (verseList.length < 1) {
      Log.e(TAG, "validateBookmarkReference:",
            new IllegalArgumentException("Bookmark reference[" + reference + "] has no verses "
                                         + "after splitting it using separator ["
                                         + SEPARATOR_BOOKMARK_REFERENCE + "]"));
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

}
