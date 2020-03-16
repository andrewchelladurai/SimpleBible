package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SettingsScreenModel;

public class SettingsScreen
    extends PreferenceFragmentCompat {

  private static final String TAG = "SettingsScreen";

  private SettingsScreenModel model;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.root_preferences, rootKey);
    Log.d(TAG, "onCreatePreferences:");
  }

  @Override public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(SettingsScreenModel.class);
  }

}
