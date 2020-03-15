package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ScreenSettingsModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenSettingsModel";

  public ScreenSettingsModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "ScreenSettingsModel:");
  }

}
