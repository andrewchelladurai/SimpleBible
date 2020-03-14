package com.andrewchelladurai.simplebible.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.Calendar;

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

    updateSectionReminderTime();
  }

  private void updateSectionReminderTime() {
    final SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
    final String key = getString(R.string.pref_reminder_time_key);
    Log.d(TAG, "updateSectionReminderTime: populating section for [" + key + "]");

    final Preference pref = getPreferenceScreen().findPreference(key);
    if (pref != null) {
      final String value = preferences.getString(key, "");

      if (Utils.getInstance().validateReminderKeyValue(value)) {
        final int[] parts = Utils.getInstance().splitReminderKeyValue(value);
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, parts[0]);
        cal.set(Calendar.MINUTE, parts[1]);
        pref.setSummary(DateFormat.getTimeFormat(requireActivity()).format(cal.getTime()));
      } else {
        pref.setSummary(getString(R.string.pref_reminder_time_summary_invalid));
      }
    } else {
      Log.e(TAG, "onCreatePreferences: Preference with key [" + key + "] not found");
    }
  }

  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    mainOps.hideKeyboard();
    mainOps.showNavigationView();
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  private void handlePreferenceClickTheme() {
    Log.d(TAG, "handlePreferenceClickTheme:");
  }

  private void handlePreferenceClickExport() {
    Log.d(TAG, "handlePreferenceClickExport:");
  }

  private void handlePreferenceClickReminder() {
    Log.d(TAG, "handlePreferenceClickReminder:");
    final SharedPreferences preferences = getPreferenceManager().getSharedPreferences();

    final String key = getString(R.string.pref_reminder_time_key);
    final String value = preferences.getString(key, "");
    final int[] hourMinute = new int[2];

    final Utils utils = Utils.getInstance();
    if (utils.validateReminderKeyValue(value)) {
      final int[] array = utils.splitReminderKeyValue(value);
      hourMinute[0] = array[0];
      hourMinute[1] = array[1];
    } else {
      final Calendar now = Calendar.getInstance();
      hourMinute[0] = now.get(Calendar.HOUR_OF_DAY);
      hourMinute[1] = now.get(Calendar.MINUTE);
    }

    final FragmentActivity fActivity = requireActivity();
    new TimePickerDialog(
        fActivity,
        (view, hour, min) -> {
          updateKeyValueReminderTime(hour, min);
        },
        hourMinute[0],
        hourMinute[1],
        DateFormat.is24HourFormat(fActivity)
    ).show();
  }

  private void updateKeyValueReminderTime(@IntRange(from = 0, to = 23) final int hour,
                                          @IntRange(from = 0, to = 59) final int min) {
    final String key = getString(R.string.pref_reminder_time_key);
    Log.d(TAG, "handlePreferenceClickReminder: key[" + key + "]"
               + " hour[" + hour + "], " + "min[" + min + "]");
    final SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
    editor.putString(key, Utils.getInstance().createReminderKeyValue(hour, min));
    editor.apply();

    final Preference pref = getPreferenceScreen().findPreference(key);
    if (pref != null) {
      final Calendar cal = Calendar.getInstance();
      cal.set(Calendar.HOUR_OF_DAY, hour);
      cal.set(Calendar.MINUTE, min);
      pref.setSummary(DateFormat.getTimeFormat(requireActivity()).format(cal.getTime()));
    } else {
      Log.e(TAG, "updateKeyValueReminderTime: could not locate preference with key[" + key + "]");
    }
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

  private void handlePreferenceClickAbout() {
    Log.d(TAG, "handlePreferenceClickAbout:");
    showAlertWebView("about.html",
                     R.string.screen_settings_msg_not_found_about);
  }

  private void handlePreferenceClickLicense() {
    Log.d(TAG, "handlePreferenceClickLicense:");
    showAlertWebView("licenses.html",
                     R.string.screen_settings_msg_not_found_license);
  }

  private void showAlertWebView(@NonNull final String assetsFileName,
                                @StringRes final int errorMsgStrRef) {
    try {
      final FragmentActivity fragAct = requireActivity();
      final WebView wv = new WebView(fragAct.getApplicationContext());
      wv.loadUrl("file:///android_asset/" + assetsFileName);
      wv.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
          } else {
            return false;
          }
        }
      });

      new AlertDialog.Builder(fragAct).setView(wv)
                                      .show();
    } catch (Exception e) {
      Log.e(TAG, "showAlertWebView: Could not open assets file [" + assetsFileName + "]", e);
      mainOps.showErrorScreen(getString(errorMsgStrRef), true, false);
    }
  }

  private class PreferenceChangeHandler
      implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences preferences,
                                          final String key) {
      if (getString(R.string.pref_theme_key).equalsIgnoreCase(key)) {
        mainOps.handleThemeToggle();
        mainOps.restartApp();
      } else if (getString(R.string.pref_reminder_key).equalsIgnoreCase(key)) {
        Log.d(TAG, "onSharedPreferenceChanged: [" + key + "] is handled automatically");
      } else if (getString(R.string.pref_reminder_time_key).equalsIgnoreCase(key)) {
        Log.d(TAG, "onSharedPreferenceChanged: [" + key + "] is handled automatically");
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
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_reminder_time_key))) {
        handlePreferenceClickReminder();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_rate_key))) {
        handlePreferenceClickRate();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_email_key))) {
        handlePreferenceClickEmail();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_about_key))) {
        handlePreferenceClickAbout();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_license_key))) {
        handlePreferenceClickLicense();
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_reminder_key))) {
        Log.d(TAG, "onPreferenceTreeClick: [pref_reminder_key] is handled automatically");
      } else if (gotKey.equalsIgnoreCase(getString(R.string.pref_reminder_time_key))) {
        Log.d(TAG, "onPreferenceTreeClick: [pref_reminder_time_key] is handled automatically");
      } else {
        Log.e(TAG, "onPreferenceTreeClick: unknown key[" + gotKey + "]");
        return false;
      }
      return true;
    }

  }

}
