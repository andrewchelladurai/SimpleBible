package com.andrewchelladurai.simplebible.utils;

import androidx.annotation.NonNull;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

public class BookmarkUtils {

  private static final String TAG = "BookmarkUtils";
  private static final String SEPARATOR = "~";

  @NonNull
  public static String createBookmarkReference(@NonNull final List<?> list) {
    StringBuilder builder = new StringBuilder();
    Verse verse;
    for (final Object o : list) {
      verse = (Verse) o;
      builder.append(verse.toString())
             .append(SEPARATOR);
    }

    // remove the last SEPARATOR value from the built string and return it.
    builder.trimToSize();
    return builder.substring(0, builder.length() - SEPARATOR.length());
  }

}
