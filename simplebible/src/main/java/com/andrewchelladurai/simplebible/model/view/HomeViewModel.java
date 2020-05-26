package com.andrewchelladurai.simplebible.model.view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.db.SbDatabase;
import com.andrewchelladurai.simplebible.db.dao.SbDao;
import com.andrewchelladurai.simplebible.db.entities.EntityVerse;
import com.andrewchelladurai.simplebible.model.Book;
import com.andrewchelladurai.simplebible.model.Verse;

public class HomeViewModel
    extends AndroidViewModel {

  private static final String TAG = "HomeViewModel";

  @Nullable
  private static Verse CACHED_VERSE = null;

  @IntRange(from = 0)
  private static int CACHED_DOY = 0;

  @NonNull
  private final SbDao dao;

  public HomeViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "HomeViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @Nullable
  public Verse getCachedVerse() {
    return CACHED_VERSE;
  }

  public void setCachedVerse(@NonNull final Verse verse) {
    CACHED_VERSE = verse;
  }

  @NonNull
  public LiveData<EntityVerse> getVerse(@IntRange(from = 1, to = Book.MAX_BOOKS) final int book,
                                        @IntRange(from = 1) final int chapter,
                                        @IntRange(from = 1) final int verse) {
    Log.d(TAG, "getVerse: book[" + book + "], chapter[" + chapter + "], verse[" + verse + "]");

    return dao.getVerse(book, chapter, verse);

  }

  @IntRange(from = 0)
  public int getCachedVerseDay() {
    return CACHED_DOY;
  }

  public void setCachedVerseDay(@IntRange(from = 1) final int dayNo) {
    CACHED_DOY = dayNo;
  }

}
