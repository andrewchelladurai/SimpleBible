package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SearchScreenModel
    extends AndroidViewModel {

  private static final String TAG = "SearchScreenModel";

  private final BookDao bookDao;
  private final VerseDao verseDao;

  private HashSet<Verse> selectedVerses = new HashSet<>();
  private HashSet<String> selectedTexts = new HashSet<>();
  private ArrayList<Verse> list = new ArrayList<>();

  public SearchScreenModel(@NonNull final Application application) {
    super(application);
    bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
    verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
  }

  public void refreshCachedList(@NonNull final List<?> newList) {
    clearCachedList();
    for (final Object object : newList) {
      list.add((Verse) object);
    }
    Log.d(TAG, "refreshCachedList: cached [" + getCachedListSize() + "] records");
  }

  public Verse getCachedItemAt(final int position) {
    return list.get(position);
  }

  public int getCachedListSize() {
    return list.size();
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

  public void addSelection(@NonNull final String text) {
    selectedTexts.add(text);
  }

  public void addSelection(@NonNull final Verse verse) {
    selectedVerses.add(verse);
  }

  public LiveData<List<Verse>> searchText(@NonNull final String text) {
    return verseDao.getRecordsWithText("%" + text + "%");
  }

  public boolean isSelectionEmpty() {
    return selectedVerses.isEmpty();
  }

  public LiveData<Book> getBook(@IntRange(from = 1, to = 66) final int bookNumber) {
    return bookDao.getRecordLive(bookNumber);
  }

  public List<?> getCachedList() {
    return list;
  }

  @NonNull
  public HashSet<Verse> getSelectedVerses() {
    return selectedVerses;
  }

  @NonNull
  public HashSet<String> getSelectedTexts() {
    return selectedTexts;
  }

  public void clearCachedList() {
    list.clear();
    clearSelection();
  }

  public void clearSelection() {
    selectedTexts.clear();
    selectedVerses.clear();
  }

}
