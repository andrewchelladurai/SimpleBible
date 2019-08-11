package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Verse;

import java.util.ArrayList;

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

}
