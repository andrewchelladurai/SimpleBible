package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.db.entities.EntityVerse;
import com.andrewchelladurai.simplebible.model.Book;
import com.andrewchelladurai.simplebible.model.Bookmark;
import com.andrewchelladurai.simplebible.model.Verse;
import com.andrewchelladurai.simplebible.model.view.SettingsViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    model = AndroidViewModelFactory.getInstance(requireActivity().getApplication())
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
    ops.hideNavigationView();
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    Log.d(TAG, "onCreatePreferences:");
    setPreferencesFromResource(R.xml.root_preferences, rootKey);

    updateSummaryTheme();
    updateSummaryReminder();
    updateSummaryReminderTime();
  }

  private void updateSummaryTheme() {
    final String key = getString(R.string.pref_theme_key);
    final Preference prefSection = getPreferenceScreen().findPreference(key);
    if (prefSection == null) {
      Log.e(TAG, "updateSummaryTheme:",
            new NullPointerException("No Preference found for key[" + key + "]"));
      return;
    }

    final String value = getPreferenceManager().getSharedPreferences()
                                               .getString(key, getString(
                                                 R.string.pref_theme_value_system));
    if (getString(R.string.pref_theme_value_yes).equalsIgnoreCase(value)) {
      prefSection.setSummary(R.string.pref_theme_entry_yes);
    } else if (getString(R.string.pref_theme_value_no).equalsIgnoreCase(value)) {
      prefSection.setSummary(R.string.pref_theme_entry_no);
    } else if (getString(R.string.pref_theme_value_system).equalsIgnoreCase(value)) {
      prefSection.setSummary(R.string.pref_theme_entry_system);
    } else {
      Log.e(TAG, "updateSummaryTheme:", new IllegalArgumentException(
        "unknown value[" + value + "] returned for key[" + key + "]"));
    }
  }

  private void updateSummaryReminder() {
    Log.d(TAG, "updateSummaryReminder: commented because of git commit"
               + "[fee26f975e23c9b46954edd72cb03b402707c830]");
    /*
    final String key = getString(R.string.pref_reminder_key);
    final Preference prefSection = getPreferenceScreen().findPreference(key);
    if (prefSection == null) {
      Log.e(TAG, "updateSummaryReminder:", new NullPointerException(
          "No Preference found for key[" + key + "]"));
      return;
    }

    final boolean value = getPreferenceManager().getSharedPreferences()
                                                .getBoolean(key, false);
    prefSection.setSummary((value) ? R.string.pref_reminder_summary_on
                                   : R.string.pref_reminder_summary_off);
    */
  }

  private void updateSummaryReminderTime() {
    Log.d(TAG, "updateSummaryReminderTime: commented because of git commit"
               + "[fee26f975e23c9b46954edd72cb03b402707c830]");
    /*
    final String key = getString(R.string.pref_reminder_time_key);
    final Preference prefSection = getPreferenceScreen().findPreference(key);
    if (prefSection == null) {
      Log.e(TAG, "updateSummaryReminder:", new NullPointerException(
          "No Preference found for key[" + key + "]"));
      return;
    }

    final SharedPreferences sPref = getPreferenceManager().getSharedPreferences();
    final Resources resources = getResources();
    final int hour = sPref.getInt(getString(R.string.pref_reminder_time_hour_key),
                                  resources.getInteger(R.integer.default_reminder_time_hour));
    final int minute = sPref.getInt(getString(R.string.pref_reminder_time_minute_key),
                                    resources.getInteger(R.integer.default_reminder_time_minute));

    if (model.validateReminderTimeValue(hour, minute)) {
      final Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, hour);
      calendar.set(Calendar.MINUTE, minute);
      final String timeStr = DateFormat.getTimeFormat(requireContext())
                                       .format(calendar.getTime());
      prefSection.setSummary(getString(R.string.pref_reminder_time_summary_valid, timeStr));
    } else {
      prefSection.setSummary(R.string.pref_reminder_time_summary_invalid);
    }
    */
  }

  private void handleValueChangeTheme() {
    updateSummaryTheme();

    ops.updateApplicationTheme();
    ops.restartApp();
  }

  private void handleValueChangeReminderState() {
    updateSummaryReminder();
    ops.updateDailyVerseReminderState();
  }

  private void handleValueChangeReminderTime() {
    updateSummaryReminderTime();
    ops.updateDailyVerseReminderTime();
  }

  private void handlePreferenceClickLicense() {
    showAlertWebView("licenses.html", R.string.scr_settings_err_not_found_license);
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

  private void handlePreferenceClickAbout() {
    showAlertWebView("about.html", R.string.scr_settings_err_not_found_about);
  }

  private void handlePreferenceClickEmail() {
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

  private void handlePreferenceClickRate() {
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

  private void handlePreferenceClickReminderTime() {
    final SharedPreferences sPref = getPreferenceManager().getSharedPreferences();
    final Resources resources = getResources();
    final int hour = sPref.getInt(getString(R.string.pref_reminder_time_hour_key),
                                  resources.getInteger(R.integer.default_reminder_time_hour));
    final int minute = sPref.getInt(getString(R.string.pref_reminder_time_minute_key),
                                    resources.getInteger(R.integer.default_reminder_time_minute));
    final Context context = requireContext();
    new TimePickerDialog(context, (view, newHour, newMinute) -> {
      if (newHour != hour || newMinute != minute) {
        updateDailyVerseReminderTime(newHour, newMinute);
      }
    }, hour, minute, DateFormat.is24HourFormat(context)).show();
  }

  private void updateDailyVerseReminderTime(@IntRange(from = 0, to = 23) final int hour,
                                            @IntRange(from = 0, to = 59) final int minute) {
    Log.d(TAG, "updateDailyVerseReminderTime:");
    final SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences()
                                                                  .edit();
    editor.putInt(getString(R.string.pref_reminder_time_hour_key), hour);
    editor.putInt(getString(R.string.pref_reminder_time_minute_key), minute);
    editor.apply();

    updateSummaryReminderTime();
    ops.updateDailyVerseReminderTime();
  }

  private void handlePreferenceClickExport() {
    Log.d(TAG, "handlePreferenceClickExport:");
    model.getAllBookmarks()
         .observe(this, bList -> {
           if (bList == null || bList.isEmpty()) {
             ops.showMessage(getString(R.string.scr_settings_msg_empty_bookmarks),
                             R.id.main_nav_bar);
             return;
           }

           final StringBuilder bookmarkText = new StringBuilder();
           final int bListSize = bList.size();
           final int[] currentEntry = {0};

           for (final EntityBookmark eBookmark : bList) {
             final ArrayList<Verse> verseList = new ArrayList<>(0);
             final String bookmarkReference = eBookmark.getReference();

             model.getBookmarkVerses(Bookmark.splitBookmarkReference(bookmarkReference))
                  .observe(this, vList -> {

                    if (vList == null || vList.isEmpty()) {
                      Log.e(TAG, "handlePreferenceClickExport: ", new NullPointerException(
                        "no verses found for bookmark reference[" + bookmarkReference + "]"));
                      return;
                    }

                    for (final EntityVerse eVerse : vList) {
                      final Book book = Book.getCachedBook(eVerse.getBook());
                      if (book == null) {
                        Log.e(TAG, "handlePreferenceClickExport: ",
                              new NullPointerException("No book found for verse [" + eVerse + "]"));
                        continue;
                      }
                      verseList.add(new Verse(eVerse, book));
                    }

                    bookmarkText.append(formatBookmark(new Bookmark(eBookmark, verseList)))
                                .append("\n");
                    currentEntry[0] = currentEntry[0] + 1;

                    if (currentEntry[0] == bListSize) {
                      model.setFormattedBookmarksData(bookmarkText.toString());

                      final String tsPattern = "yyyyMMdd_HHmmssSSS";
                      final SimpleDateFormat tsFormat = new SimpleDateFormat(tsPattern,
                                                                             Locale.getDefault());
                      final String formattedTime = tsFormat.format(new Date());
                      final String fileName = getString(
                        R.string.scr_settings_template_bookmark_export_filename, formattedTime);

                      Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                      intent.addCategory(Intent.CATEGORY_OPENABLE);
                      intent.setType("application/text");
                      intent.putExtra(Intent.EXTRA_TITLE, fileName);

                      startActivityForResult(intent, 0);
                    }
                  });
           }
         });
  }

  @NonNull
  private String formatBookmark(@NonNull final Bookmark bookmark) {
    final String note = bookmark.getNote();
    final List<Verse> verseList = bookmark.getVerseList();
    final StringBuilder verseText = new StringBuilder();

    for (final Verse verse : verseList) {
      verseText.append(
        verse.getFormattedContentForBookmark(getString(R.string.scr_bookmark_template_verse_item))
             .toString())
               .append("\n");
    }

    return getString(R.string.scr_settings_template_bookmark_export, // template
                     verseText, // transformed verses
                     (note.isEmpty()) // note text, use placeholder if empty
                     ? getString(R.string.scr_bookmark_note_empty) : bookmark.getNote());

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
    if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
      final Uri fileUri = resultData.getData();
      if (fileUri != null) {
        try {
          final ParcelFileDescriptor pfd = requireActivity().getContentResolver()
                                                            .openFileDescriptor(fileUri, "w");
          if (pfd != null) {
            final FileOutputStream oStream = new FileOutputStream(pfd.getFileDescriptor());
            oStream.write((model.getFormattedBookmarksData()).getBytes());
            oStream.close();
            pfd.close();
            ops.showMessage("Bookmarks Exported", R.id.main_nav_bar);
          } else {
            ops.showErrorScreen("Error creating export file: NULL ParcelFileDescriptor", true,
                                false);
          }
        } catch (IOException e) {
          ops.showErrorScreen("Error creating export file.\n" + e.getLocalizedMessage(), true,
                              false);
        }
      } else {
        ops.showMessage("File creation cancelled.", R.id.main_nav_bar);
      }
    } else {
      ops.showMessage("File creation cancelled.", R.id.main_nav_bar);
    }
  }

  private class ChangeHandler
    implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences preferences, final String key) {
      if (key.equalsIgnoreCase(getString(R.string.pref_theme_key))) {
        handleValueChangeTheme();
      } else if (key.equalsIgnoreCase(getString(R.string.pref_reminder_key))) {
        handleValueChangeReminderState();
      } else if (key.equalsIgnoreCase(getString(R.string.pref_reminder_time_key))) {
        handleValueChangeReminderTime();
      } else {
        Log.e(TAG, "onSharedPreferenceChanged: ignoring key [" + key + "]");
      }
    }

  }

  private class ClickListener
    implements PreferenceManager.OnPreferenceTreeClickListener {

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
      final String key = preference.getKey();

      if (key.equalsIgnoreCase(getString(R.string.pref_export_key))) {
        handlePreferenceClickExport();
      } else if (key.equalsIgnoreCase(getString(R.string.pref_reminder_time_key))) {
        handlePreferenceClickReminderTime();
      } else if (key.equalsIgnoreCase(getString(R.string.pref_rate_key))) {
        handlePreferenceClickRate();
      } else if (key.equalsIgnoreCase(getString(R.string.pref_email_key))) {
        handlePreferenceClickEmail();
      } else if (key.equalsIgnoreCase(getString(R.string.pref_about_key))) {
        handlePreferenceClickAbout();
      } else if (key.equalsIgnoreCase(getString(R.string.pref_license_key))) {
        handlePreferenceClickLicense();
      } else {
        Log.d(TAG, "onPreferenceTreeClick: ignoring preferenceKey = [" + key + "]");
      }

      return true;
    }

  }

}
