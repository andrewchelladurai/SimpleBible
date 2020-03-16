package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BookmarkListViewModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkListViewModel";

  public BookmarkListViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "BookmarkListViewModel:");
  }

}
