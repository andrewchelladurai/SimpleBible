package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.SbDao;
import com.andrewchelladurai.simplebible.data.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.data.entities.EntityVerse;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BookmarksViewModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarksViewModel";

  private static final ArrayList<EntityBookmark> CACHE_BOOKMARKS_LIST = new ArrayList<>();

  @NonNull
  private final SbDao dao;

  public BookmarksViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "BookmarksViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @NonNull
  public LiveData<List<EntityBookmark>> getBookmarks() {
    return dao.getAllBookmarks();
  }

  public void cacheBookmarks(@NonNull final List<EntityBookmark> bookmarks) {
    CACHE_BOOKMARKS_LIST.clear();
    CACHE_BOOKMARKS_LIST.addAll(bookmarks);
    Log.d(TAG, "cacheBookmarks: cached [" + getBookmarkListSize() + "] bookmarks");
  }

  @IntRange(from = 0)
  public int getBookmarkListSize() {
    return CACHE_BOOKMARKS_LIST.size();
  }

  @NonNull
  public EntityBookmark getBookmarkAtPosition(@IntRange(from = 0) final int position) {
    return CACHE_BOOKMARKS_LIST.get(position);
  }

  @NonNull
  public LiveData<EntityVerse> getFirstVerseOfBookmark(@NonNull final EntityBookmark bookmark) {
    final Utils utils = Utils.getInstance();
    final String[] references = utils.splitBookmarkReference(bookmark.getReference());
    final int[] referenceParts = utils.splitVerseReference(references[0]);
    //noinspection ConstantConditions
    return dao.getVerse(referenceParts[0], referenceParts[1], referenceParts[2]);
  }

  public boolean deleteBookmark(@NonNull final EntityBookmark bookmark) {
    dao.deleteBookmark(bookmark);
    return true;
  }

}
