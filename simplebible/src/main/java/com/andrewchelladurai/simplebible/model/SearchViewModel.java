package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.data.SbDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;

import java.util.List;

public class SearchViewModel
    extends AndroidViewModel {

  private static final String TAG = "SearchViewModel";

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

}
