package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SearchViewModel
    extends AndroidViewModel {

  private static final String TAG = "SearchViewModel";

  public SearchViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "SearchViewModel:");
  }

}
