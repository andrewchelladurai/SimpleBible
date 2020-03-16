package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BookListViewModel
    extends AndroidViewModel {

  private static final String TAG = "BookListViewModel";

  public BookListViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "BookListViewModel:");
  }

}
