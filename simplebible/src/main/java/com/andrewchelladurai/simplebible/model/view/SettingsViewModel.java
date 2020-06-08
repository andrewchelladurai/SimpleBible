package com.andrewchelladurai.simplebible.model.view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewchelladurai.simplebible.db.SbDatabase;
import com.andrewchelladurai.simplebible.db.dao.SbDao;
import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.db.entities.EntityVerse;
import com.andrewchelladurai.simplebible.model.Verse;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SettingsViewModel
    extends AndroidViewModel {

  private static final String TAG = "SettingsScreenModel";

  private final SbDao dao;

  public SettingsViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "SettingsScreenModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  public boolean validateReminderTimeValue(@IntRange(from = 0, to = 23) final int hour,
                                           @IntRange(from = 0, to = 59) final int minute) {
    if (hour < 0 || hour > 23) {
      Log.e(TAG, "validateReminderTimeValue:", new IllegalArgumentException(
          "invalid hour value [" + hour + "] must be between [0...23]"));
      return false;
    }

    if (minute < 0 || minute > 59) {
      Log.e(TAG, "validateReminderTimeValue:", new IllegalArgumentException(
          "invalid minute value [" + minute + "] must be between [0...59]"));
      return false;
    }

    return true;
  }

  @NonNull
  public LiveData<List<EntityBookmark>> getAllBookmarks() {
    return dao.getAllBookmarks();
  }

  @NonNull
  public LiveData<List<EntityVerse>> getBookmarkVerses(@NonNull final String[] references) {
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

}
