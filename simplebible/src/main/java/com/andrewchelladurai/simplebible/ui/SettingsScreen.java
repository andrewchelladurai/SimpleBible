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
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SettingsViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

import java.util.Calendar;

public class SettingsScreen
    extends PreferenceFragmentCompat {

  private static final String TAG = "SettingsScreen";

  private SettingsViewModel model;

  private ChangeHandler prefChangeHandler;

  private ClickListener prefClickListener;

  private SimpleBibleOps ops;

  @Override
  public void onAttach(@NonNull final Context context) {
    Log.d(TAG, "onAttach:");
    super.onAttach(context);

    if (context instanceof SimpleBibleOps) {
      ops = (SimpleBibleOps) context;
    } else {
      throw new ClassCastException(TAG + " onAttach: [Context] must implement [SimpleBibleOps]");
    }

    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(SettingsViewModel.class);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    ops = null;
  }

  @Override
  public void onResume() {
    super.onResume();

    if (prefChangeHandler == null) {
      prefChangeHandler = new ChangeHandler();
    }

    if (prefClickListener == null) {
      prefClickListener = new ClickListener();
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
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    Log.d(TAG, "onCreatePreferences:");
    setPreferencesFromResource(R.xml.root_preferences, rootKey);

    updateSectionTheme();
    updateSectionReminderTime();
  }

  private void updateSectionTheme() {
    final String key = getString(R.string.pref_theme_key);
    Log.d(TAG, "updateSectionTheme:key[" + key + "]");

    final SharedPreferences sPreferences = getPreferenceManager().getSharedPreferences();
    final Preference pref = getPreferenceScreen().findPreference(key);

    if (pref != null) {
      final String value =
          sPreferences.getString(key, getString(R.string.pref_theme_value_system));
      if (value.equalsIgnoreCase(getString(R.string.pref_theme_value_yes))) {
        pref.setSummary(getString(R.string.pref_theme_entry_yes));
      } else if (value.equalsIgnoreCase(getString(R.string.pref_theme_value_no))) {
        pref.setSummary(getString(R.string.pref_theme_entry_no));
      } else {
        pref.setSummary(getString(R.string.pref_theme_entry_system));
      }
    } else {
      Log.e(TAG, "updateSectionTheme: could not locate preference for key [" + key + "]");
    }
  }

  private void updateSectionReminderTime() {
    final String key = getString(R.string.pref_reminder_time_key);
    Log.d(TAG, "updateSectionReminderTime: populating section for [" + key + "]");

    final SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
    final Preference pref = getPreferenceScreen().findPreference(key);

    if (pref != null) {
      final String value = preferences.getString(key, "");

      if (model.validateReminderKeyValue(value)) {
        final int[] parts = model.splitReminderKeyValue(value);
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, parts[0]);
        cal.set(Calendar.MINUTE, parts[1]);
        pref.setSummary(DateFormat.getTimeFormat(requireActivity()).format(cal.getTime()));
      } else {
        pref.setSummary(getString(R.string.pref_reminder_time_summary_invalid));
      }
    } else {
      Log.d(TAG, "updateSectionReminderTime: could not locate preference for key [" + key + "]");
    }
  }

  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    ops.hideKeyboard();
    ops.showNavigationView();
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

    if (model.validateReminderKeyValue(value)) {
      final int[] array = model.splitReminderKeyValue(value);
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
          updateNotificationReminder();
        },
        hourMinute[0],
        hourMinute[1],
        DateFormat.is24HourFormat(fActivity)
    ).show();
  }

  private void updateKeyValueReminderTime(@IntRange(from = 0, to = 23) final int hour,
                                          @IntRange(from = 0, to = 59) final int min) {
    Log.d(TAG, "updateKeyValueReminderTime: hour = [" + hour + "], min = [" + min + "]");

    // save the HH:MM values passed in the preferences
    final SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
    final String key = getString(R.string.pref_reminder_time_key);
    editor.putString(key, model.createReminderKeyValue(hour, min));
    editor.apply();

    final Preference pref = getPreferenceScreen().findPreference(key);
    if (pref == null) {
      Log.e(TAG, "updateKeyValueReminderTime: no pref found  with key[" + key + "]");
      return;
    }

    // format the HH:MM values to show in the summary text of the preference entry
    final Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, min);
    pref.setSummary(DateFormat.getTimeFormat(requireActivity()).format(cal.getTime()));
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
                     R.string.scr_settings_err_not_found_about);
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
      ops.showErrorScreen(getString(errorMsgStrRef), true, false);
    }
  }

  private void handlePreferenceClickLicense() {
    Log.d(TAG, "handlePreferenceClickLicense:");
    showAlertWebView("licenses.html",
                     R.string.scr_settings_err_not_found_license);
  }

  private void updateNotificationReminder() {
    Log.d(TAG, "updateNotificationReminder:");
    final SharedPreferences sPref = getPreferenceManager().getSharedPreferences();

    final String timeValue = sPref.getString(getString(R.string.pref_reminder_time_key), "");

    if (model.validateReminderKeyValue(timeValue)) {
      final int[] timePart = model.splitReminderKeyValue(timeValue);
      final boolean reminderValue = sPref.getBoolean(getString(R.string.pref_reminder_key), true);
      ops.updateReminderTime(reminderValue, timePart[0], timePart[1]);
    } else {
      Log.e(TAG, "updateNotificationReminder: invalid timeValue[" + timeValue
                 + "]. forcefully get a correct value and use it.");
      handlePreferenceClickReminder();
    }
  }

  private class ChangeHandler
      implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences preferences,
                                          final String key) {
      if (getString(R.string.pref_theme_key).equalsIgnoreCase(key)) {
        ops.applyThemeSelectedInPreference();
        ops.restartApp();
      } else if (getString(R.string.pref_reminder_key).equalsIgnoreCase(key)
                 || getString(R.string.pref_reminder_time_key).equalsIgnoreCase(key)) {
        updateNotificationReminder();
      } else {
        Log.e(TAG, "onSharedPreferenceChanged: unhandled key [" + key + "]");
      }
    }

  }

  private class ClickListener
      implements PreferenceManager.OnPreferenceTreeClickListener {

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
      final String key = preference.getKey();

      if (getString(R.string.pref_theme_key).equalsIgnoreCase(key)) {
        handlePreferenceClickTheme();
      } else if (getString(R.string.pref_export_key).equalsIgnoreCase(key)) {
        handlePreferenceClickExport();
      } else if (getString(R.string.pref_reminder_time_key).equalsIgnoreCase(key)) {
        handlePreferenceClickReminder();
      } else if (getString(R.string.pref_rate_key).equalsIgnoreCase(key)) {
        handlePreferenceClickRate();
      } else if (getString(R.string.pref_email_key).equalsIgnoreCase(key)) {
        handlePreferenceClickEmail();
      } else if (getString(R.string.pref_about_key).equalsIgnoreCase(key)) {
        handlePreferenceClickAbout();
      } else if (getString(R.string.pref_license_key).equalsIgnoreCase(key)) {
        handlePreferenceClickLicense();
      } else if (getString(R.string.pref_reminder_key).equalsIgnoreCase(key)
                 || getString(R.string.pref_reminder_time_key).equalsIgnoreCase(key)) {
        Log.d(TAG, "onPreferenceTreeClick: ignore intentionally, handled in standalone method");
      } else {
        Log.e(TAG, "onPreferenceTreeClick: unknown key[" + key + "]");
        return false;
      }
      return true;
    }

  }

}
