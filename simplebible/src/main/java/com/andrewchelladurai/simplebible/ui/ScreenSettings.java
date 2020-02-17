package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

public class ScreenSettings
    extends PreferenceFragmentCompat {

  private static final String TAG = "ScreenSettings";

  private ScreenSimpleBibleOps mainOps;
  private PreferenceChangeHandler preferenceChangeHandler;
  private PreferenceClickListener preferenceClickListener;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
  }

  @Override
  public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
    setPreferencesFromResource(R.xml.sb_main_preferences, rootKey);
  }

  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    mainOps.hideKeyboard();
    mainOps.showNavigationView();
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  @Override
  public void onResume() {
    super.onResume();

    if (preferenceChangeHandler == null) {
      preferenceChangeHandler = new PreferenceChangeHandler();
      getPreferenceManager().getSharedPreferences()
                            .registerOnSharedPreferenceChangeListener(preferenceChangeHandler);
    }

    if (preferenceClickListener == null) {
      preferenceClickListener = new PreferenceClickListener();
      getPreferenceManager().setOnPreferenceTreeClickListener(preferenceClickListener);
    }

  }

  @Override
  public void onPause() {
    super.onPause();

    if (preferenceChangeHandler != null) {
      getPreferenceManager().getSharedPreferences()
                            .unregisterOnSharedPreferenceChangeListener(preferenceChangeHandler);
    }

    if (preferenceClickListener != null) {
      getPreferenceManager().setOnPreferenceTreeClickListener(null);
    }

  }

  private void handlePreferenceClickExport() {
    Log.d(TAG, "handlePreferenceClickExport:");
  }

  private void handlePreferenceClickAbout() {
    Log.d(TAG, "handlePreferenceClickAbout:");
  }

  private class PreferenceChangeHandler
      implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences preferences,
                                          final String key) {
      if (getString(R.string.pref_theme_key).equalsIgnoreCase(key)) {
        mainOps.handleThemeToggle();
        mainOps.restartApp();
      } else {
        Log.e(TAG, "onSharedPreferenceChanged: unhandled pref key [" + key + "]");
      }
    }

  }

  private class PreferenceClickListener
      implements PreferenceManager.OnPreferenceTreeClickListener {

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
      final String preferenceKey = preference.getKey();
      final String keyAbout = getString(R.string.pref_about_key);
      final String keyExport = getString(R.string.pref_export_key);

      if (preferenceKey.equalsIgnoreCase(keyAbout)) {
        handlePreferenceClickAbout();
        return true;
      } else if (preferenceKey.equalsIgnoreCase(keyExport)) {
        handlePreferenceClickExport();
        return true;
      } else {
        Log.e(TAG, "onPreferenceTreeClick: unknown key[" + preferenceKey + "]");
        return false;
      }
    }

  }

}
