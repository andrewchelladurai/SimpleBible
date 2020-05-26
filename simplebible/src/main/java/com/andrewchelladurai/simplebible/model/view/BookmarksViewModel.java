package com.andrewchelladurai.simplebible.model.view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.db.SbDatabase;
import com.andrewchelladurai.simplebible.db.dao.SbDao;
import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.db.entities.EntityVerse;
import com.andrewchelladurai.simplebible.model.Bookmark;
import com.andrewchelladurai.simplebible.model.Verse;

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
    final String[] references = Bookmark.splitBookmarkReference(bookmark.getReference());
    final int[] referenceParts = Verse.splitReference(references[0]);
    //noinspection ConstantConditions
    return dao.getVerse(referenceParts[0], referenceParts[1], referenceParts[2]);
  }

  public boolean deleteBookmark(@NonNull final EntityBookmark bookmark) {
    dao.deleteBookmark(bookmark);
    return true;
  }

}
