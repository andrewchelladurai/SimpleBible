package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SettingsScreenModel;

public class SettingsScreen
    extends PreferenceFragmentCompat {

  private SettingsScreenModel model;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.root_preferences, rootKey);
  }

  @Override public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(SettingsScreenModel.class);
  }

}
