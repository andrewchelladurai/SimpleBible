package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkScreenModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkScreenModel";
  private static ArrayList<Verse> cacheList = new ArrayList<>();
  private static String cacheReference = "";

  private final BookmarkDao bookmarkDao;

  public BookmarkScreenModel(@NonNull final Application application) {
    super(application);
    bookmarkDao = SbDatabase.getDatabase(getApplication()).getBookmarkDao();
  }

  public LiveData<Book> getBook(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return SbDatabase.getDatabase(getApplication())
                     .getBookDao()
                     .getRecordLive(bookNumber);
  }

  public void updateCache(@NonNull final List<?> newList) {
    if (newList.isEmpty()) {
      Log.e(TAG, "updateCache: empty list passed, returning");
      return;
    }

    final ArrayList<Verse> tempList = new ArrayList<>();
    for (final Object o : newList) {
      tempList.add((Verse) o);
    }

    //noinspection unchecked
    Collections.sort(tempList);

    final String reference = BookmarkUtils.createBookmarkReference(tempList);
    if (reference.equalsIgnoreCase(cacheReference)
        && newList.size() == cacheList.size()) {
      Log.d(TAG, "updateCache: already cached reference [" + cacheReference
                 + "] with [" + cacheList.size() + "] records");
      return;
    }

    cacheList.clear();
    cacheList.addAll(tempList);
    cacheReference = reference;
    Log.d(TAG, "updateCache: cached reference [" + cacheReference + "] "
               + "with [" + cacheList.size() + "] records");
  }

  @NonNull
  public List<?> getCachedList() {
    return cacheList;
  }

  public int getCachedListSize() {
    return cacheList.size();
  }

  public Verse getCachedItemAt(@IntRange(from = 0) final int position) {
    return cacheList.get(position);
  }

  public LiveData<Integer> bookmarkExists(@NonNull final String reference) {
    if (reference.isEmpty()) {
      throw new IllegalArgumentException(TAG + " bookmarkExists: Empty reference passed");
    }

    return bookmarkDao.doesRecordExistLive(reference);
  }

  public LiveData<Integer> saveBookmark(@NonNull final String reference,
                                        @NonNull final String note) {
    new BookmarkUtils.CreateBookmarkTask(bookmarkDao).execute(new Bookmark(reference, note));
    return bookmarkExists(reference);
  }

  public LiveData<Bookmark> getBookmark(@NonNull final String reference) {
    return bookmarkDao.getRecordLive(reference);
  }

  @NonNull
  public String getCachedReference() {
    return cacheReference;
  }

}
