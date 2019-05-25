package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Book;
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
  private ArrayList<Verse> cacheList = new ArrayList<>();
  private HashSet<Verse> selectedVerses = new HashSet<>();
  private HashSet<String> selectedTexts = new HashSet<>();

  public ChapterScreenModel(@NonNull final Application application) {
    super(application);
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
    return SbDatabase.getDatabase(getApplication())
                     .getVerseDao()
                     .getRecordsOfChapter(bookNumber, chapterNumber);
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

  public boolean isSelected(@NonNull final Verse verse) {
    return selectedVerses.contains(verse);
  }

  public void removeSelection(@NonNull final Verse verse) {
    selectedVerses.remove(verse);
  }

  public void removeSelection(@NonNull final String text) {
    selectedTexts.remove(text);
  }

  public void addSelection(@NonNull final Verse verse) {
    selectedVerses.add(verse);
  }

  public void addSelection(@NonNull final String text) {
    selectedTexts.add(text);
  }

  @NonNull
  public HashSet<Verse> getSelectedVerses() {
    return selectedVerses;
  }

  public boolean isSelectionEmpty() {
    return selectedVerses.isEmpty();
  }

  @NonNull
  public HashSet<String> getSelectedTexts() {
    return selectedTexts;
  }

  public void cleatSelections() {
    selectedTexts.clear();
    selectedVerses.clear();
  }

  public LiveData<Book> getBook() {
    return SbDatabase.getDatabase(getApplication())
                     .getBookDao()
                     .getRecordLive(getBookNumber());
  }

}
