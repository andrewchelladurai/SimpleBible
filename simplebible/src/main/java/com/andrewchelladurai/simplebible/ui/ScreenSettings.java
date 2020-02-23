package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
  private PreferenceChangeHandler prefChangeHandler;
  private PreferenceClickListener prefClickListener;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  @Override
  public void onResume() {
    super.onResume();

    if (prefChangeHandler == null) {
      prefChangeHandler = new PreferenceChangeHandler();
    }

    if (prefClickListener == null) {
      prefClickListener = new PreferenceClickListener();
    }

    getPreferenceManager().getSharedPreferences()
                          .registerOnSharedPreferenceChangeListener(prefChangeHandler);
    getPreferenceManager().setOnPreferenceTreeClickListener(prefClickListener);
  }

  @Override
  public void onPause() {
    super.onPause();

    getPreferenceManager().getSharedPreferences()
                          .unregisterOnSharedPreferenceChangeListener(prefChangeHandler);
    getPreferenceManager().setOnPreferenceTreeClickListener(null);
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

  private void handlePreferenceClickExport() {
    Log.d(TAG, "handlePreferenceClickExport:");
  }

  private void handlePreferenceClickAbout() {
    Log.d(TAG, "handlePreferenceClickAbout:");
  }

  private void handlePreferenceClickLicense() {
    Log.d(TAG, "handlePreferenceClickLicense:");
  }

  private void handlePreferenceClickEmail() {
    Log.d(TAG, "handlePreferenceClickEmail:");
    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
    emailIntent.setData(Uri.parse("mailto:"));
    emailIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.pref_email_address));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.pref_email_subject));
    if (null != emailIntent.resolveActivity(requireActivity().getPackageManager())) {
      startActivity(emailIntent);
    } else {
      Log.e(TAG, "handlePreferenceClickEmail: No email application found");
    }
  }

  private void handlePreferenceClickTheme() {
    Log.d(TAG, "handlePreferenceClickTheme:");
  }

  private void handlePreferenceClickRate() {
    Log.d(TAG, "handlePreferenceClickRate:");
    final String packName = requireActivity().getPackageName();

    final Intent appIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + packName));

    if (appIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
      startActivity(appIntent);
    } else {
      Log.e(TAG, "handlePreferenceClickRate: Google PlayStore NOT found");
      final Uri urlPath = Uri.parse("https://play.google.com/store/apps/details?id=" + packName);
      final Intent browserIntent = new Intent(Intent.ACTION_VIEW, urlPath);
      startActivity(browserIntent);
    }

  }

  private void handlePreferenceClickReminder() {
    Log.d(TAG, "handlePreferenceClickReminder:");
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
        Log.e(TAG, "onSharedPreferenceChanged: unhandled key [" + key + "]");
      }
    }

  }

  private class PreferenceClickListener
      implements PreferenceManager.OnPreferenceTreeClickListener {

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
      final String gotKey = preference.getKey();

      if (gotKey.equalsIgnoreCase(getString(R.string.pref_theme_key))) {
        handlePreferenceClickTheme();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_export_key))) {
        handlePreferenceClickExport();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_reminder_key))) {
        handlePreferenceClickReminder();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_rate_key))) {
        handlePreferenceClickRate();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_email_key))) {
        handlePreferenceClickEmail();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_about_key))) {
        handlePreferenceClickAbout();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_license_key))) {
        handlePreferenceClickLicense();
      } else {
        Log.e(TAG, "onPreferenceTreeClick: unknown key[" + gotKey + "]");
        return false;
      }
      return true;
    }

  }

}
