package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.data.SbDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchViewModel
    extends AndroidViewModel {

  private static final String TAG = "SearchViewModel";

  @NonNull
  private static final ArrayList<EntityVerse> BASE_LIST = new ArrayList<>();

  @NonNull
  private static final ArrayMap<String, EntityVerse> SELECTED_LIST = new ArrayMap<>();

  @NonNull
  private static String SEARCH_TEXT = "";

  private final SbDao dao;

  public SearchViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "SearchViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @NonNull
  public LiveData<List<EntityVerse>> findVersesContainingText(@NonNull final String text) {
    return dao.findVersesContainingText("%" + text + "%");
  }

  public void updateContent(@NonNull final List<EntityVerse> verseList, @NonNull String text) {
    final int count = getResultCount();
    if (SEARCH_TEXT.equalsIgnoreCase(text) && count == verseList.size()) {
      Log.d(TAG, "updateContent: already cached [" + count + "] results for [" + text + "]");
    }

    resetContent();
    BASE_LIST.addAll(verseList);

    Log.d(TAG, "updateContent: updated [" + getResultCount() + " records]");
  }

  @IntRange(from = 0)
  public int getResultCount() {
    return BASE_LIST.size();
  }

  public void resetContent() {
    BASE_LIST.clear();
    SELECTED_LIST.clear();
    SEARCH_TEXT = "";
  }

  public void clearSelectedList() {
    SELECTED_LIST.clear();
  }

  @NonNull
  public Collection<EntityVerse> getSelectedList() {
    return SELECTED_LIST.values();
  }

  public boolean isSelected(@NonNull final String verseReference) {
    return SELECTED_LIST.containsKey(verseReference);
  }

  public void removeSelection(@NonNull final String verseReference) {
    SELECTED_LIST.remove(verseReference);
  }

  public void addSelection(@NonNull final String verseReference, @NonNull final EntityVerse verse) {
    SELECTED_LIST.put(verseReference, verse);
  }

  @NonNull
  public EntityVerse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return BASE_LIST.get(position);
  }

  @NonNull
  public String getCachedText() {
    return SEARCH_TEXT;
  }

}
