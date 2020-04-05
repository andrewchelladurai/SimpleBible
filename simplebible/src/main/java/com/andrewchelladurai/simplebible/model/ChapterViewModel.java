package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.data.SbDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.List;
import java.util.TreeMap;

public class ChapterViewModel
    extends AndroidViewModel {

  private static final String TAG = "ChapterViewModel";

  @NonNull
  private final SbDao dao;

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  private static int CACHED_BOOK = 1;

  @IntRange(from = 1)
  private static int CACHED_CHAPTER = 1;

  @NonNull
  private static final TreeMap<Integer, EntityVerse> CACHED_LIST = new TreeMap<>();

  @NonNull
  private static final TreeMap<Integer, EntityVerse> SELECTED_LIST = new TreeMap<>();

  public ChapterViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "ChapterViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  public int getCachedBookNumber() {
    return CACHED_BOOK;
  }

  public void setCachedBookNumber(@IntRange(from = 1, to = Utils.MAX_BOOKS) final int book) {
    CACHED_BOOK = book;
  }

  @IntRange(from = 1)
  public int getCachedChapterNumber() {
    return CACHED_CHAPTER;
  }

  public void setCachedChapterNumber(@IntRange(from = 1) final int chapter) {
    CACHED_CHAPTER = chapter;
  }

  @NonNull
  public LiveData<List<EntityVerse>> getChapterVerses(final int book, final int chapter) {
    return dao.getChapter(book, chapter);
  }

  public void clearSelection() {
    SELECTED_LIST.clear();
  }

  public void clearCache() {
    CACHED_BOOK = CACHED_CHAPTER = 0;
    CACHED_LIST.clear();
  }

  public void setCacheList(@NonNull final List<EntityVerse> verseList) {
    if (verseList.isEmpty()) {
      Log.e(TAG, "setCacheList: empty list");
      return;
    }

    for (final EntityVerse verse : verseList) {
      CACHED_LIST.put(verse.getVerse(), verse);
    }

    Log.d(TAG, "setCacheList: cached [" + getCachedListSize() + "] verses");

  }

  public int getCachedListSize() {
    return CACHED_LIST.size();
  }

  public int getSelectedListSize() {
    return SELECTED_LIST.size();
  }

  @Nullable
  public EntityVerse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return CACHED_LIST.get(position);
  }

  public boolean isVerseSelected(@NonNull final EntityVerse verse) {
    final EntityVerse foundVerse = SELECTED_LIST.get(verse.getVerse());
    return (foundVerse != null && foundVerse.equals(verse));
  }

  public void removeSelectedVerse(@NonNull final EntityVerse verse) {
    SELECTED_LIST.remove(verse.getVerse());
  }

  public void addSelectedVerse(@NonNull final EntityVerse verse) {
    SELECTED_LIST.put(verse.getVerse(), verse);
  }

}
