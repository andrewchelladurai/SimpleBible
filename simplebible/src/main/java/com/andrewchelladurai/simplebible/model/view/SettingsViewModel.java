package com.andrewchelladurai.simplebible.model.view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SettingsViewModel
    extends AndroidViewModel {

  private static final String TAG = "SettingsScreenModel";

  public SettingsViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "SettingsScreenModel:");
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

}
