package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.EntityBook;
import com.andrewchelladurai.simplebible.data.entity.EntityVerse;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;

import java.util.ArrayList;
import java.util.List;

public class BookmarkDetailModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkDetailModel";

  private final VerseDao verseDao;
  private final BookmarkDao bookmarkDao;
  private final BookDao bookDao;
  private final ArrayList<EntityVerse> cachedList = new ArrayList<>();

  public BookmarkDetailModel(@NonNull final Application application) {
    super(application);
    bookmarkDao = SbDatabase.getDatabase(getApplication())
                            .getBookmarkDao();
    bookDao = SbDatabase.getDatabase(getApplication())
                        .getBookDao();
    verseDao = SbDatabase.getDatabase(getApplication())
                         .getVerseDao();
  }

  public void cacheList(@NonNull final ArrayList<EntityVerse> list) {
    cachedList.clear();
    cachedList.addAll(list);
    Log.d(TAG, "cacheList: [" + cachedList.size() + "] records cached");
  }

  @NonNull
  public ArrayList<EntityVerse> getCachedList() {
    return cachedList;
  }

  public LiveData<List<Bookmark>> getBookmark(@NonNull final String reference) {
    Log.d(TAG, "getBookmark: reference = [" + reference + "]");
    if (reference.isEmpty()) {
      throw new IllegalArgumentException("empty bookmark reference passed");
    }

    return bookmarkDao.findBookmarkUsingReference(reference);
  }

  public LiveData<EntityBook> getBook(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return bookDao.getBookUsingPositionLive(bookNumber);
  }

  @NonNull
  public MutableLiveData<Boolean> saveBookmark(@NonNull final String reference,
                                               @NonNull final String note) {
    Log.d(TAG, "saveBookmark: reference [" + reference + "], note [" + note + "]");
    final MutableLiveData<Boolean> taskResult = new MutableLiveData<>();

    if (reference.isEmpty()) {
      taskResult.postValue(false);
      throw new IllegalArgumentException("empty bookmark reference passed");
    }

    if (!BookmarkUtils.validateReference(reference)) {
      taskResult.postValue(false);
      throw new IllegalArgumentException("Invalid bookmark references");
    }

    final Bookmark bookmark = new Bookmark(reference, note);
    BookmarkUtils.saveBookmark(taskResult, bookmarkDao, bookmark);

    return taskResult;
  }

  @NonNull
  public MutableLiveData<Boolean> deleteBookmark(@NonNull final String reference,
                                                 @NonNull final String note) {
    Log.d(TAG, "deleteBookmark: reference [" + reference + "]");
    final MutableLiveData<Boolean> taskResult = new MutableLiveData<>();

    if (reference.isEmpty()) {
      taskResult.postValue(false);

      final String message = "deleteBookmark: empty bookmark reference passed";
      Log.e(TAG, message);
      throw new IllegalArgumentException(TAG + " - " + message);
    }

    if (!BookmarkUtils.validateReference(reference)) {
      taskResult.postValue(false);

      final String message = "deleteBookmark: Invalid bookmark references";
      Log.e(TAG, message);
      throw new IllegalArgumentException(TAG + " - " + message);
    }

    final Bookmark bookmark = new Bookmark(reference, note);
    BookmarkUtils.deleteBookmark(taskResult, bookmarkDao, bookmark);

    return taskResult;
  }

}
