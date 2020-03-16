package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SettingsScreenModel;

public class SettingsScreen
    extends PreferenceFragmentCompat {

  private static final String TAG = "SettingsScreen";

  private SettingsScreenModel model;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    Log.d(TAG, "onCreatePreferences:");
    setPreferencesFromResource(R.xml.root_preferences, rootKey);

    updateSectionTheme();
  }

  private void updateSectionTheme() {
    Log.d(TAG, "updateSectionTheme:");
    final String key = getString(R.string.pref_theme_key);

    final Preference preference = findPreference(key);
    if (preference == null) {
      Log.e(TAG, "updateSectionTheme: ",
            new NullPointerException("No preference found for key[" + key + "]"));
      return;
    }

    // get the value set for the preference
    final String value = getPreferenceManager()
                             .getSharedPreferences()
                             .getString(key, getString(R.string.pref_theme_value_system));

    // depending on the current value, update the summary so the user can know at a glance
    if (value.equalsIgnoreCase(getString(R.string.pref_theme_value_yes))) {
      preference.setSummary(R.string.pref_theme_entry_yes);
    } else if (value.equalsIgnoreCase(getString(R.string.pref_theme_value_no))) {
      preference.setSummary(R.string.pref_theme_entry_no);
    } else {
      preference.setSummary(R.string.pref_theme_entry_system);
    }

  }

  @Override public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(SettingsScreenModel.class);
  }

}
