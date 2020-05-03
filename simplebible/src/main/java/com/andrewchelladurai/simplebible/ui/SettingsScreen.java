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
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SettingsViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

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

    final PreferenceManager prefManager = getPreferenceManager();
    prefManager.getSharedPreferences()
               .registerOnSharedPreferenceChangeListener(prefChangeHandler);
    prefManager.setOnPreferenceTreeClickListener(prefClickListener);
  }

  @Override
  public void onPause() {
    super.onPause();

    final PreferenceManager prefManager = getPreferenceManager();
    prefManager.getSharedPreferences()
               .unregisterOnSharedPreferenceChangeListener(prefChangeHandler);
    prefManager.setOnPreferenceTreeClickListener(null);
  }

  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    ops.hideKeyboard();
    ops.showNavigationView();
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    Log.d(TAG, "onCreatePreferences:");
    setPreferencesFromResource(R.xml.root_preferences, rootKey);

    updateSummaryTheme(getString(R.string.pref_theme_key));
    updateSummaryReminder(getString(R.string.pref_reminder_key));
  }

  private void updateSummaryTheme(final String key) {
    Log.d(TAG, "updateSummaryTheme: key[" + key + "]");
  }

  private void updateSummaryReminder(@NonNull final String key) {
    Log.d(TAG, "updateSummaryReminder: key[" + key + "]");
  }

  private void handleValueChangeTheme(final String key) {
    Log.d(TAG, "handleValueChangeTheme: key[" + key + "]");
    updateSummaryTheme(key);

    ops.applyThemeSelectedInPreference();
    ops.restartApp();
  }

  private void handleValueChangeReminder(final String key) {
    Log.d(TAG, "handleValueChangeReminder: key[" + key + "]");
    updateSummaryReminder(key);
  }

/*
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
*/

  private class ChangeHandler
      implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences preferences,
                                          final String key) {
      if (key.equalsIgnoreCase(getString(R.string.pref_theme_key))) {
        handleValueChangeTheme(key);
      } else if (key.equalsIgnoreCase(getString(R.string.pref_reminder_key))) {
        handleValueChangeReminder(key);
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
      Log.d(TAG, "onPreferenceTreeClick: preferenceKey = [" + key + "]");
      return true;
    }

  }

}
