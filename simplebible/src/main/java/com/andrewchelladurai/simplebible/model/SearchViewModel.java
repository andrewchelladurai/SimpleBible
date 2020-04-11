package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.andrewchelladurai.simplebible.data.SbDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;

public class SearchViewModel
    extends AndroidViewModel {

  private static final String TAG = "SearchViewModel";

  private final SbDao dao;

  public SearchViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "SearchViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  public void findVersesContainingText(@NonNull final String text) {

  }

}
