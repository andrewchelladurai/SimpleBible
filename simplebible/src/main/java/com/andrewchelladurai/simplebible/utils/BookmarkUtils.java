package com.andrewchelladurai.simplebible.utils;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import com.andrewchelladurai.simplebible.data.SbDatabase;
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

  public static class CreateBookmarkLoader
      extends AsyncTaskLoader<Boolean> {

    public static final int ID = 13;
    private static final String TAG = "CreateBookmarkLoader";
    private final Bookmark bookmark;
    private final BookmarkDao bookmarkDao;

    public CreateBookmarkLoader(@NonNull final Context context,
                                final Bookmark bookmark) {
      super(context);
      bookmarkDao = SbDatabase.getDatabase(getContext()).getBookmarkDao();
      this.bookmark = bookmark;
    }

    @Nullable
    @Override
    public Boolean loadInBackground() {
      Log.d(TAG, "loadInBackground: " + bookmark);
      bookmarkDao.createRecord(bookmark);
      return true;
    }

  }

  public static class UpdateBookmarkLoader
      extends AsyncTaskLoader<Boolean> {

    public static final int ID = 14;
    private final Bookmark bookmark;
    private final BookmarkDao bookmarkDao;

    public UpdateBookmarkLoader(final Context context, final Bookmark bookmark) {
      super(context);
      bookmarkDao = SbDatabase.getDatabase(getContext()).getBookmarkDao();
      this.bookmark = bookmark;
    }

    @Nullable
    @Override
    public Boolean loadInBackground() {
      Log.d(TAG, "loadInBackground: " + bookmark);
      bookmarkDao.updateRecord(bookmark);
      return true;
    }

  }

  public static class DeleteBookmarkLoader
      extends AsyncTaskLoader<Boolean> {

    public static final int ID = 16;
    @NonNull
    private final Bookmark bookmark;
    private final BookmarkDao bookmarkDao;

    public DeleteBookmarkLoader(@NonNull final Context context, @NonNull Bookmark bookmark) {
      super(context);
      bookmarkDao = SbDatabase.getDatabase(getContext()).getBookmarkDao();
      this.bookmark = bookmark;
    }

    @Nullable
    @Override
    public Boolean loadInBackground() {
      Log.d(TAG, "loadInBackground: " + bookmark);
      bookmarkDao.deleteRecord(bookmark);
      return true;
    }

  }

}
