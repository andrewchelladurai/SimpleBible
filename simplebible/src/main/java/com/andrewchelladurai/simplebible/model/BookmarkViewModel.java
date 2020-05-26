package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.Verse;
import com.andrewchelladurai.simplebible.data.dao.SbDao;
import com.andrewchelladurai.simplebible.data.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.data.entities.EntityVerse;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class BookmarkViewModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkViewModel";

  @NonNull
  private static final ArrayList<Verse> CACHED_VERSES = new ArrayList<>();

  @Nullable
  private static EntityBookmark CACHED_BOOKMARK = null;

  @NonNull
  private static String CACHED_BOOKMARK_REFERENCE = "";

  @NonNull
  private final SbDao dao;

  public BookmarkViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "BookmarkViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  public void clearCache() {
    CACHED_BOOKMARK = null;
    CACHED_BOOKMARK_REFERENCE = "";
    CACHED_VERSES.clear();
  }

  @Nullable
  public EntityBookmark getCachedBookmark() {
    return CACHED_BOOKMARK;
  }

  public void setCachedBookmark(@NonNull final EntityBookmark bookmark) {
    CACHED_BOOKMARK = bookmark;
    Log.d(TAG, "setCachedBookmark: cached [" + bookmark + "]");
  }

  @NonNull
  public String getCachedBookmarkReference() {
    return CACHED_BOOKMARK_REFERENCE;
  }

  public void setCachedBookmarkReference(@NonNull final String reference) {
    CACHED_BOOKMARK_REFERENCE = reference;
    Log.d(TAG, "setCachedBookmarkReference: reference[" + CACHED_BOOKMARK_REFERENCE + "]");
  }

  public void setCachedVerses(@NonNull final List<Verse> verseList) {
    CACHED_VERSES.clear();
    CACHED_VERSES.addAll(verseList);
    Log.d(TAG, "setCachedVerses: [" + CACHED_VERSES.size() + "] verses cached");
  }

  public boolean validateBookmarkReference(@NonNull final String reference) {
    return Utils.getInstance().validateBookmarkReference(reference);
  }

  @NonNull
  public String[] getVersesForBookmarkReference(@NonNull final String reference) {
    return Utils.getInstance().splitBookmarkReference(reference);
  }

  @Nullable
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
      return null;
    }

    final ArrayList<Integer> bookNumbers = new ArrayList<>();
    final ArrayList<Integer> chapterNumbers = new ArrayList<>();
    final ArrayList<Integer> verseNumbers = new ArrayList<>();
    //noinspection CheckStyle
    int[] vParts;

    for (final String verseReference : finalReferences) {
      vParts = Verse.splitReference(verseReference);
      if (vParts == null) {
        Log.e(TAG, "getVerses: ",
              new IllegalArgumentException("no parts found for verse [" + verseReference
                                           + "] even though it passed validation"));
        continue;
      }
      bookNumbers.add(vParts[0]);
      chapterNumbers.add(vParts[1]);
      verseNumbers.add(vParts[2]);
    }

    return dao.getVerses(bookNumbers, chapterNumbers, verseNumbers);
  }

  @NonNull
  public LiveData<EntityBookmark> getBookmarkForReference(@NonNull final String reference) {
    return dao.getBookmarkForReference(reference);
  }

  @IntRange(from = 0)
  public int getCachedVerseListSize() {
    return CACHED_VERSES.size();
  }

  @Nullable
  public Verse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return CACHED_VERSES.get(position);
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
