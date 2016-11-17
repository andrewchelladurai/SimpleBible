/*
 *
 * This file 'ActivitySettings.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.Utilities;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 23-Oct-2016 @ 9:42 PM
 */

public class SettingsActivity
        extends PreferenceActivity {

    private static final String TAG = "SB_SettingsActivity";
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(
                android.R.id.content, new SettingsActivity.AboutPreferenceFragment()).commit();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
               || AboutPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    @NonNull @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AboutPreferenceFragment
            extends PreferenceFragment {

        private final PreferenceListener mListener = new PreferenceListener();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_list);

            final String keyList[] = getResources().getStringArray(R.array.pref_key_list);
            Preference preference;
            String value = "";
            for (String pref_key : keyList) {
                preference = findPreference(pref_key);
                if (preference != null) {
                    if (preference instanceof ListPreference) {
                        value = ((ListPreference) preference).getValue();
                    } else if (preference instanceof SwitchPreference) {
                        getSummaryForSwitchPreference((SwitchPreference) preference);
                    } else if (preference instanceof RingtonePreference) {
                        value = ((RingtonePreference) preference).getShowSilent()
                                ? "Enabled" : "Silent";
                    } else if (pref_key.equalsIgnoreCase(
                            getString(R.string.pref_key_export_bookmarks))) {
                        value = getString(R.string.pref_key_export_bookmarks_summary);
                    } else {
                        value = "";
                    }
                    preference.setSummary(value);
                    preference.setOnPreferenceChangeListener(mListener);
                    preference.setOnPreferenceClickListener(mListener);
                }
            }
        }

        /**
         * Sets the Summary ON/OFF texts for a SwitchPreference.
         *
         * @param preference the preference that needs to be updated.
         */
        private void getSummaryForSwitchPreference(SwitchPreference preference) {
            switch (preference.getKey()) {
                case "pref_key_theme_dark":
                    preference.setSummaryOn(R.string.pref_key_theme_dark_summary_enabled);
                    preference.setSummaryOff(R.string.pref_key_theme_dark_summary_disabled);
                    break;
                case "pref_key_reminder":
                    preference.setSummaryOn(String.format(
                            getString(R.string.pref_key_reminder_summary_enabled),
                            Utilities.getReminderHour(), Utilities.getReminderMinute()));
                    preference.setSummaryOff(R.string.pref_key_reminder_summary_disabled);
                    break;
                case "pref_key_reminder_vibrate":
                    preference.setSummaryOn(R.string.pref_key_reminder_vibrate_summary_enabled);
                    preference.setSummaryOff(R.string.pref_key_reminder_vibrate_summary_disabled);
                    break;
                default:
                    preference.setSummaryOn(R.string.enabled);
                    preference.setSummaryOff(R.string.disabled);
            }
        }

        private void exportBookmarks() {
            String returnMessage = Utilities.exportBookmarks();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if (returnMessage != null && returnMessage.contains(Constants.DELIMITER_IN_REFERENCE)) {
                String message[] = returnMessage.split(Constants.DELIMITER_IN_REFERENCE);
                builder.setTitle(String.format(
                        getString(R.string.dialog_title_bookmarks_export), message[0]));
                builder.setMessage(message[1]);
            } else {
                builder.setMessage("You will now have to grant permission manually");
            }
            builder.show();
        }

        private void showChangeLogDialog() {
            Log.d(TAG, "showChangeLogDialog");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            WebView webView = new WebView(getActivity());
            webView.loadUrl("file:///android_asset/about.html");

            builder.setView(webView)
                   .setNegativeButton(null, null)
                   .setPositiveButton(null, null)
                   .show();
        }

        private void updateReminderPreferences(SwitchPreference preference) {
            Log.d(TAG, "updateReminderPreferences() called");
            if (preference.isChecked()) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.setPreference(preference);
                dialog.show(getFragmentManager(), "TimePickerFragment");
            }
        }

        private class PreferenceListener
                implements Preference.OnPreferenceChangeListener,
                           Preference.OnPreferenceClickListener {

            @Override public boolean onPreferenceChange(Preference preference, Object obj) {
                final String pPreferenceKey = preference.getKey();
                Log.d(TAG, "onPreferenceChange: " + pPreferenceKey);
                String value = "";
                if (preference instanceof ListPreference) {
                    value = obj.toString();
                } else if (preference instanceof SwitchPreference) {
                    getSummaryForSwitchPreference((SwitchPreference) preference);
                } else if (preference instanceof RingtonePreference) {
                    value = ((RingtonePreference) preference).getShowSilent()
                            ? "Silent" : "Ring Audibly";
                } else {
                    value = obj.toString();
                }
                preference.setSummary(value);
                if (pPreferenceKey.equalsIgnoreCase(
                        getString(R.string.pref_key_theme_dark))) {
                    Utilities.restartApplication(getActivity());
                }
                return true;
            }

            @Override public boolean onPreferenceClick(Preference pPreference) {
                String key = pPreference.getKey();
                if (key == null) {
                    Log.d(TAG, "onPreferenceClick() returning : key = null");
                    return false;
                }
                Log.d(TAG, "onPreferenceClick() called key = [" + key + "]");
                if (key.equalsIgnoreCase(getString(R.string.pref_key_changelog))) {
                    showChangeLogDialog();
                } else if (key.equalsIgnoreCase(getString(R.string.pref_key_export_bookmarks))) {
                    exportBookmarks();
                } else if (key.equalsIgnoreCase(getString(R.string.pref_key_reminder))) {
                    updateReminderPreferences((SwitchPreference) pPreference);
                }
                return true;
            }
        }
    }
}
