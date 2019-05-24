package com.andrewchelladurai.simplebible.model;

import android.app.Application;
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

  private final BookDao bookDao;
  private final VerseDao verseDao;
  private HashSet<Verse> selectedVerseSet = new HashSet<>();
  private HashSet<String> selectedTextSet = new HashSet<>();
  private ArrayList<Verse> list = new ArrayList<>();

  public SearchScreenModel(@NonNull final Application application) {
    super(application);
    bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
    verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
  }

  public boolean isEmptyText(final @NonNull String text) {
    return text.trim().isEmpty();
  }

  public boolean isMinimumLength(final @NonNull String text) {
    return text.trim().length() < 3;
  }

  public boolean isMaximumLength(final @NonNull String text) {
    return text.trim().length() > 50;
  }

  public LiveData<List<Verse>> searchText(@NonNull final String text) {
    return verseDao.getRecordsWithText("%" + text + "%");
  }

  public LiveData<Book> getBook(@IntRange(from = 1, to = 66) final int bookNumber) {
    return bookDao.getRecordLive(bookNumber);
  }

  public List<?> getAdapterList() {
    return list;
  }

  public Verse getAdapterItemAt(final int position) {
    return list.get(position);
  }

  public int getAdapterListSize() {
    return list.size();
  }

  public void clearAdapterList() {
    list.clear();
    selectedVerseSet.clear();
    selectedTextSet.clear();
  }

  public void addToAdapterList(final Verse verse) {
    list.add(verse);
  }

  public boolean isResultSelected(@NonNull final Verse verse) {
    return selectedVerseSet.contains(verse);
  }

  public void removeSelectedResult(@NonNull final Verse verse) {
    selectedVerseSet.remove(verse);
  }

  public void removeSelectedText(@NonNull final String text) {
    selectedTextSet.remove(text);
  }

  public void addSelectedResult(@NonNull final Verse verse) {
    selectedVerseSet.add(verse);
  }

  public void addSelectedText(@NonNull final String text) {
    selectedTextSet.add(text);
  }

  @NonNull
  public HashSet<Verse> getSelectedResults() {
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
