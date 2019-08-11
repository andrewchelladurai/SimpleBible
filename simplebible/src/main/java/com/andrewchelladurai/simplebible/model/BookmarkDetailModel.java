package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.ArrayList;
import java.util.List;

public class BookmarkDetailModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkDetailModel";

  private final VerseDao verseDao;
  private final BookmarkDao bookmarkDao;
  private final BookDao bookDao;
  private ArrayList<Verse> cachedList = new ArrayList<>();

  public BookmarkDetailModel(@NonNull final Application application) {
    super(application);
    bookmarkDao = SbDatabase.getDatabase(getApplication()).getBookmarkDao();
    bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
    verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
  }

  public void cacheList(@NonNull final ArrayList<Verse> list) {
    cachedList.clear();
    cachedList.addAll(list);
    Log.d(TAG, "cacheList: [] records cached");
  }

  @NonNull
  public ArrayList<Verse> getCachedList() {
    return cachedList;
  }

  public LiveData<List<Bookmark>> getBookmark(@NonNull final String reference) {
    Log.d(TAG, "getBookmark: reference = [" + reference + "]");
    if (reference.isEmpty()) {
      throw new IllegalArgumentException("empty bookmark reference passed");
    }

    return bookmarkDao.findBookmarkUsingReference(reference);
  }

  public LiveData<Book> getBook(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return bookDao.getBookUsingPositionLive(bookNumber);
  }

}