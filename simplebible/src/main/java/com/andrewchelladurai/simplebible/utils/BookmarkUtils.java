package com.andrewchelladurai.simplebible.utils;

import androidx.annotation.NonNull;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkUtils {

  public static final String SEPARATOR = "~";
  private static final String TAG = "BookmarkUtils";

  @NonNull
  public static String createReference(@NonNull final List<?> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException(TAG + " createReference: Empty list passed");
    }

    final ArrayList<Verse> tempList = new ArrayList<>();
    for (final Object o : list) {
      tempList.add((Verse) o);
    }

    //noinspection unchecked
    Collections.sort(tempList);

    StringBuilder builder = new StringBuilder();

    for (final Verse verse : tempList) {
      builder.append(verse.toString())
             .append(SEPARATOR);
    }

    // remove the last SEPARATOR value from the built string and return it.
    builder.trimToSize();
    return builder.substring(0, builder.length() - SEPARATOR.length());
  }

}
