package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChapterScreenModel
    extends AndroidViewModel {

  private static final String TAG = "ChapterScreenModel";
  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  private static int bookNumber = 1;
  @IntRange(from = 1)
  private static int chapterNumber = 1;
  private final VerseDao verseDao;
  private ArrayList<Verse> cacheList = new ArrayList<>();
  private HashSet<Verse> selectedVerseSet = new HashSet<>();
  private HashSet<String> selectedTextSet = new HashSet<>();

  public ChapterScreenModel(@NonNull final Application application) {
    super(application);
    verseDao = SbDatabase.getDatabase(application).getVerseDao();
  }

  @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT)
  public int getBookNumber() {
    return bookNumber;
  }

  public void setBookNumber(@IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int book) {
    ChapterScreenModel.bookNumber = book;
  }

  @IntRange(from = 1)
  public int getChapterNumber() {
    return chapterNumber;
  }

  public void setChapterNumber(@IntRange(from = 1) final int chapter) {
    ChapterScreenModel.chapterNumber = chapter;
  }

  public LiveData<List<Verse>> getChapterVerses() {
    return verseDao.getRecordsOfChapter(bookNumber, chapterNumber);
  }

  public void updateCache(@NonNull final List<?> newList) {
    cacheList.clear();
    if (newList.isEmpty()) {
      Log.e(TAG, "updateCache: newList is empty");
      return;
    }
    for (final Object object : newList) {
      cacheList.add((Verse) object);
    }
    Log.d(TAG, "updateCache: updated [" + getCacheSize() + "] items");
  }

  public int getCacheSize() {
    return cacheList.size();
  }

  public List<?> getCache() {
    return cacheList;
  }

  public Object getCachedItemAt(final int position) {
    return cacheList.get(position);
  }

  public boolean isResultSelected(@NonNull final Verse verse) {
    return selectedVerseSet.contains(verse);
  }

  public void removeSelection(@NonNull final Verse verse) {
    selectedVerseSet.remove(verse);
  }

  public void removeSelection(@NonNull final String text) {
    selectedTextSet.remove(text);
  }

  public void addSelection(@NonNull final Verse verse) {
    selectedVerseSet.add(verse);
  }

  public void addSelection(@NonNull final String text) {
    selectedTextSet.add(text);
  }

  @NonNull
  public HashSet<Verse> getSelection() {
    return selectedVerseSet;
  }

  public boolean isSelectionEmpty() {
    return selectedVerseSet.isEmpty();
  }

  @NonNull
  public HashSet<String> getSelectedText() {
    return selectedTextSet;
  }

}
