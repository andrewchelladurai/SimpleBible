package com.andrewchelladurai.simplebible.model.view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewchelladurai.simplebible.db.SbDatabase;
import com.andrewchelladurai.simplebible.db.dao.SbDao;
import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.db.entities.EntityVerse;
import com.andrewchelladurai.simplebible.model.Bookmark;
import com.andrewchelladurai.simplebible.model.Verse;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class BookmarkViewModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkViewModel";

  @Nullable
  private static Bookmark CACHED_BOOKMARK = null;

  @NonNull
  private final SbDao dao;

  public BookmarkViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "BookmarkViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  public void clearCache() {
    CACHED_BOOKMARK = null;
  }

  @Nullable
  public Bookmark getCachedBookmark() {
    return CACHED_BOOKMARK;
  }

  public void setCachedBookmark(@Nullable final Bookmark bookmark) {
    CACHED_BOOKMARK = bookmark;
    Log.d(TAG, "setCachedBookmark: cached [" + bookmark + "]");
  }

  public boolean validateBookmarkReference(@NonNull final String reference) {
    return Utils.getInstance().validateBookmarkReference(reference);
  }

  @NonNull
  public String[] getVersesForBookmarkReference(@NonNull final String reference) {
    return Utils.getInstance().splitBookmarkReference(reference);
  }

  @NonNull
  public LiveData<List<EntityVerse>> getVerses(@NonNull final String[] references) {
    if (references.length < 1) {
      Log.e(TAG, "getVerses:", new IllegalArgumentException("received empty array of references"));
      return new MutableLiveData<>();
    }

    final TreeSet<String> finalReferences = new TreeSet<>();
    for (final String reference : references) {
      if (Verse.validateReference(reference)) {
        finalReferences.add(reference);
      }
    }

    if (finalReferences.isEmpty()) {
      Log.e(TAG, "getVerses:", new IllegalArgumentException("No valid verse references remaining"));
      return new MutableLiveData<>();
    }

    final ArrayList<Integer> bookNumbers = new ArrayList<>();
    final ArrayList<Integer> chapterNumbers = new ArrayList<>();
    final ArrayList<Integer> verseNumbers = new ArrayList<>();
    int[] parts;

    for (final String verseReference : finalReferences) {
      parts = Verse.splitReference(verseReference);
      if (parts == null) {
        Log.e(TAG, "getVerses: ",
              new IllegalArgumentException("no parts found for verse [" + verseReference
                                           + "] even though it passed validation"));
        continue;
      }
      bookNumbers.add(parts[0]);
      chapterNumbers.add(parts[1]);
      verseNumbers.add(parts[2]);
    }

    return dao.getVerses(bookNumbers, chapterNumbers, verseNumbers);
  }

  @NonNull
  public LiveData<EntityBookmark> getBookmarkForReference(@NonNull final String reference) {
    return dao.getBookmarkForReference(reference);
  }

  @IntRange(from = 0)
  public int getCachedVerseListSize() {
    return (CACHED_BOOKMARK != null) ? CACHED_BOOKMARK.getVerseList().size() : 0;
  }

  @Nullable
  public Verse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return (CACHED_BOOKMARK != null) ? CACHED_BOOKMARK.getVerseList().get(position) : null;
  }

  public boolean saveBookmark(@NonNull final EntityBookmark bookmark) {
    dao.createBookmark(bookmark);
    return true;
  }

  public boolean deleteBookmark(@NonNull final EntityBookmark bookmark) {
    dao.deleteBookmark(bookmark);
    return true;
  }

}
