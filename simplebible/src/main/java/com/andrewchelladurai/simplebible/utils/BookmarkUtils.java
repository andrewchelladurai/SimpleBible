package com.andrewchelladurai.simplebible.utils;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkUtils {

  private static final String SEPARATOR = "~";
  private static final String TAG = "BookmarkUtils";
  private static final BookmarkUtils THIS_INSTANCE = new BookmarkUtils();

  private BookmarkUtils() {
  }

  public static BookmarkUtils getInstance() {
    return THIS_INSTANCE;
  }

  public boolean validateReference(@NonNull final String reference) {
    Log.d(TAG, "validateReference [" + reference + "]");

    final VerseUtils verseUtils = VerseUtils.getInstance();

    // if there are more than one verse references in the bookmark reference
    if (reference.contains(SEPARATOR)) {
      final String[] verseReferences = reference.split(SEPARATOR);
      // check if each of the verse reference is valid
      for (final String vReference : verseReferences) {
        // if any one of the verse reference is invalid, fail the validation
        if (!verseUtils.validateReference(vReference)) {
          Log.d(TAG, "validateReference: verseReference [" + vReference + " is invalid]");
          return false;
        }
      }
      // pass the validation if all verse references check out
      return true;
    } else {
      // this will be called if we did not find any bookmark reference separator
      // meaning - only one verse is being bookmarked
      // if so, validate the single verse's reference
      if (!verseUtils.validateReference(reference)) {
        Log.d(TAG, "validateReference: verseReference [" + reference + " is invalid]");
        return false;
      } else {
        return true;
      }
    }
  }

  @NonNull
  public String createReference(@NonNull final List<?> list) {
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

    final VerseUtils verseUtils = VerseUtils.getInstance();
    for (final Verse verse : tempList) {
      builder.append(verseUtils.createReference(verse))
             .append(SEPARATOR);
    }

    // remove the last SEPARATOR value from the built string and return it.
    builder.trimToSize();
    return builder.substring(0, builder.length() - SEPARATOR.length());
  }

  public void saveBookmark(@NonNull final MutableLiveData<Boolean> taskResult,
                           @NonNull final BookmarkDao bookmarkDao,
                           @NonNull final Bookmark bookmark) {
    Log.d(TAG, "saveBookmark: " + bookmark);
    new SaveBookmarkTask(bookmarkDao, taskResult).execute(bookmark);
  }

  private static class SaveBookmarkTask
      extends AsyncTask<Bookmark, Void, Boolean> {

    private static final String TAG = "SaveBookmarkTask";
    private final BookmarkDao dao;
    private final MutableLiveData<Boolean> taskResult;

    SaveBookmarkTask(final BookmarkDao bookmarkDao,
                     final MutableLiveData<Boolean> taskResult) {
      dao = bookmarkDao;
      this.taskResult = taskResult;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
      taskResult.postValue(result);
    }

    @Override
    protected Boolean doInBackground(final Bookmark... bookmarks) {
      final Bookmark bookmark = bookmarks[0];
      Log.d(TAG, "doInBackground: " + bookmark);
      dao.saveBookmark(bookmark);
      return true;
    }

  }

}
