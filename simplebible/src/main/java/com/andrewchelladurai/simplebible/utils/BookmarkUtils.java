package com.andrewchelladurai.simplebible.utils;

import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkUtils {

  private static final String TAG = "BookmarkUtils";
  private static final String SEPARATOR = "~";

  @NonNull
  public static String createBookmarkReference(@NonNull final List<?> list) {
    if (list.isEmpty()) {
      throw new IllegalArgumentException(TAG + " createBookmarkReference: Empty list passed");
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

  public static class CreateBookmarkTask
      extends AsyncTask<Bookmark, Void, Integer> {

    private static final String TAG = "CreateBookmarkTask";
    private BookmarkDao bookmarkDao;

    public CreateBookmarkTask(@NonNull final BookmarkDao bookmarkDao) {
      this.bookmarkDao = bookmarkDao;
    }

    @Override
    protected Integer doInBackground(final Bookmark... bookmarks) {
      final Bookmark bookmark = bookmarks[0];
      Log.d(TAG, "doInBackground() called with: bookmarks = [" + bookmark + "]");
      bookmarkDao.createRecord(bookmark);
      return bookmarkDao.doesRecordExist("%" + bookmark.getReferences() + "%");
    }

  }

}
