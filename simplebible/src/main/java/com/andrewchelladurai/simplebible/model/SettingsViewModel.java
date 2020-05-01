package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SettingsViewModel
    extends AndroidViewModel {

  private static final String TAG = "SettingsScreenModel";

  private static final String KEY_VALUE_SEPARATOR = "~";

  public SettingsViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "SettingsScreenModel:");
  }

  public int[] splitReminderKeyValue(@NonNull final String value) {
    if (validateReminderKeyValue(value)) {
      final String[] parts = value.split(KEY_VALUE_SEPARATOR);
      return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }
    Log.e(TAG, "splitReminderKeyValue: ",
          new IllegalArgumentException("value[" + value + "] failed validation, returning "
                                       + "default HH:MM value 08:00"));
    return new int[]{8, 0};
  }

  public boolean validateReminderKeyValue(@NonNull final String value) {
    final String[] parts = value.split(KEY_VALUE_SEPARATOR);
    if (parts.length != 2) {
      Log.e(TAG, "validateReminderKeyValue:", new IllegalArgumentException(
          "value expected to have 2 parts [HH~MM] but it has [" + parts.length + "]"));
      return false;
    }

    int intValue;
    try {
      intValue = Integer.parseInt(parts[0]);
    } catch (NumberFormatException nfe) {
      Log.e(TAG, "validateReminderKeyValue: hour value is not an integer", nfe);
      return false;
    }

    if (intValue < 0 || intValue > 23) {
      Log.e(TAG, "validateReminderKeyValue:", new IllegalArgumentException(
          "invalid hour value [" + intValue + "] must be between [0...23]"));
      return false;
    }

    try {
      intValue = Integer.parseInt(parts[1]);
    } catch (NumberFormatException nfe) {
      Log.e(TAG, "validateReminderKeyValue: minute value is not an integer", nfe);
      return false;
    }

    if (intValue < 0 || intValue > 59) {
      Log.e(TAG, "validateReminderKeyValue:", new IllegalArgumentException(
          "invalid minute value [" + intValue + "] must be between [0...59]"));
      return false;
    }

    return true;
  }

  @NonNull
  public String createReminderKeyValue(@IntRange(from = 0, to = 23) final int hour,
                                       @IntRange(from = 0, to = 59) final int minute) {
    return hour + KEY_VALUE_SEPARATOR + minute;
  }

}
