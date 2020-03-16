package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SettingsScreenModel
    extends AndroidViewModel {

  private static final String TAG = "SettingsScreenModel";

  private static final String KEY_VALUE_SEPARATOR = "~";

  public SettingsScreenModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "SettingsScreenModel:");
  }

  public int[] splitReminderKeyValue(@NonNull final String value) {
    Log.d(TAG, "getReminderKeyValue() called with: value = [" + value + "]");
    if (validateReminderKeyValue(value)) {
      final String[] parts = value.split(KEY_VALUE_SEPARATOR);
      return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }
    throw new IllegalArgumentException("argument value[" + value + "] failed validation");
  }

  public boolean validateReminderKeyValue(@NonNull final String value) {
    Log.d(TAG, "validateReminderKeyValue: value[" + value + "]");

    final String[] parts = value.split(KEY_VALUE_SEPARATOR);
    if (parts.length != 2) {
      Log.e(TAG, "validateReminderKeyValue: we expected 2 parts [HH~MM] in the value "
                 + "and found [" + parts.length + "] parts in it.");
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
      Log.e(TAG, "validateReminderKeyValue: hour[" + intValue
                 + "] is not within expected limit [0...23]");
      return false;
    }

    try {
      intValue = Integer.parseInt(parts[1]);
    } catch (NumberFormatException nfe) {
      Log.e(TAG, "validateReminderKeyValue: minute value is not an integer", nfe);
      return false;
    }

    if (intValue < 0 || intValue > 59) {
      Log.e(TAG, "validateReminderKeyValue: minute[" + intValue
                 + "] is not within expected limit [0...59]");
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
