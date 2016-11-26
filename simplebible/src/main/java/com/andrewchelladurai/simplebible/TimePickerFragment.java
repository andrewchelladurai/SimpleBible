/*
 *
 * This file 'TimePickerFragment.java' is part of SimpleBible :
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

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.andrewchelladurai.simplebible.utilities.Utilities;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Nov-2016 @ 12:08 AM
 */

public class TimePickerFragment
        extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private SwitchPreference mPreference;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this,
                                    Utilities.getReminderHour(),
                                    Utilities.getReminderMinute(),
                                    DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        Utilities.setReminderTime(hour, minute);
        mPreference.setSummaryOn(String.format(
                getString(R.string.pref_key_reminder_summary_enabled),
                Utilities.getReminderHour(), Utilities.getReminderMinute()));
        Utilities.enableAndUpdateReminder(true);
    }

    public void setPreference(SwitchPreference preference) {
        mPreference = preference;
    }
}
