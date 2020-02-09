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
import androidx.preference.PreferenceFragmentCompat;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

public class ScreenSettings
    extends PreferenceFragmentCompat {

  private static final String TAG = "ScreenSettings";

  private ScreenSimpleBibleOps mainOps;
  private PreferenceChangeHandler preferenceChangeHandler;

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
  }

  @Override
  public void onPause() {
    super.onPause();
    if (preferenceChangeHandler != null) {
      getPreferenceManager().getSharedPreferences()
                            .unregisterOnSharedPreferenceChangeListener(preferenceChangeHandler);
    }
  }

  private class PreferenceChangeHandler
      implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences preferences,
                                          final String key) {
      if (getString(R.string.sb_pref_key_theme).equalsIgnoreCase(key)) {
        mainOps.handleThemeToggle();
      } else {
        Log.e(TAG, "onSharedPreferenceChanged: unhandled pref key [" + key + "]");
      }
    }

  }

}
