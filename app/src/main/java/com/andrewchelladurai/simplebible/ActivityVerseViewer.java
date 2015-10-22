/*
 * This file 'ActivityVerseViewer.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Copyright (c) Andrew Chelladurai - 2015.
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
 */

package com.andrewchelladurai.simplebible;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andrewchelladurai.simplebible.utilities.HelperDatabase;
import com.andrewchelladurai.simplebible.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityVerseViewer
        extends ActionBarActivity
        implements View.OnClickListener,
                   AdapterView.OnItemLongClickListener {

    private final String TAG = "ActivityVerseViewer";
    private int currentBookId;
    private int currentChapter;
    private int chapterCount;
    private String currentBookName;
    private ListView verseListView;
    private VerseListAdapter verseListAdapter;
    private ArrayList<String> arrayList;
    private TextView txtHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Entering onCreate");
        Utilities.updateTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_viewer);

        Log.d(TAG, "ID = " + getIntent().getIntExtra("ID", 1) + "");
        updateVariables(getIntent().getIntExtra("ID", 1));
        setTitle(currentBookName);

        // Always load 1st Chapter when loading a new BookUnit
        txtHeader.setText(currentBookName + " Chapter 1");
        updateVerseView(1);
        Log.i(TAG, "Exiting onCreate");
    }

    private void updateVariables(int id) {
        Log.i(TAG, "Entering updateVariables" + id);
        currentBookName = BookSList.getBookName(id);
        chapterCount = BookSList.getTotalChapters(id);
        currentBookId = BookSList.getBookNumber(id);

        verseListView = (ListView) findViewById(R.id.verseListView);
        verseListView.setOnItemLongClickListener(this);
        txtHeader = (TextView) findViewById(R.id.verseHeader);

        arrayList = new ArrayList<>(1);
        verseListAdapter = new VerseListAdapter(this, android.R.layout.simple_list_item_1,
                                                android.R.id.text1, arrayList);
        verseListView.setAdapter(verseListAdapter);
        Log.i(TAG, "Exiting updateVariables");
    }

    @Override
    protected void onResume() {
        super.onResume();
        verseListView.refreshDrawableState();
        verseListAdapter.notifyDataSetChanged();
    }

    private void updateVerseView(int chapterID) {
        Log.i(TAG, "Entering updateVerseView");
        HelperDatabase helperDatabase = ActivityWelcome.getDataBaseHelper();
        Cursor cursor = helperDatabase.getDBRecords(currentBookId + 1, chapterID);

        arrayList.clear();
        verseListAdapter.clear();

        if (cursor.moveToFirst()) {
            int verseIndex = cursor.getColumnIndex("Verse");
            int verseIdIndex = cursor.getColumnIndex("VerseId");
            //            int chapterIdIndex = cursor.getColumnIndex("ChapterId");
            //            int bookIdIndex = cursor.getColumnIndex("BookId");
            do {
                arrayList.add(cursor.getInt(verseIdIndex) + " : " + cursor.getString(verseIndex));
            } while (cursor.moveToNext());
            verseListView.refreshDrawableState();
            verseListAdapter.notifyDataSetChanged();
            txtHeader.setText(currentBookName + " Chapter " + chapterID);
            cursor.close();
            currentChapter = chapterID;
        }
        createChapterButtons();
        Log.i(TAG, "Exiting updateVerseView");
    }

    private void createChapterButtons() {
        Log.i(TAG, "Entering createChapterButtons");
        LinearLayout layout = (LinearLayout) findViewById(R.id.chapterButtonsLayout);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                              LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(-15, 0, -15, 0);
        layout.removeAllViews();

        for (int i = 0; i < chapterCount; i++) {
            Button b = new Button(this, null, R.attr.buttonStyleSmall);
            b.setText((i + 1) + "");
            b.setOnClickListener(this);
            b.setBackgroundColor(Color.TRANSPARENT);
            layout.addView(b, layoutParams);
        }
        layout.refreshDrawableState();
        Log.i(TAG, "Exiting createChapterButtons");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_verse_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, ActivitySettings.class));
                return true;
            case R.id.action_about:
                FragmentDialogAbout fda = new FragmentDialogAbout();
                fda.show(getSupportFragmentManager(), "about");
                return true;
            case R.id.action_reminder:
                reminderActionClicked();
                return true;
            default:
                Log.e(TAG, "ERROR : Option Item Selected hit Default : " + item.getTitle());
        }
        return super.onOptionsItemSelected(item);
    }

    private void reminderActionClicked() {
        if (Utilities.isReminderEnabled()) {
            Calendar c = Calendar.getInstance();
            TimePickerDialog tpd = new TimePickerDialog(
                    this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    Utilities.setReminderTimestamp(hour, minute);
                }
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            tpd.show();
        } else {
            Snackbar.make(findViewById(R.id.verse_viewer_fragment),
                          "Reminder is Disabled in Preferences.", Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(ActivityWelcome.this, "Reminder is Disabled in Preferences.",
//                                   Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "Entering onClick");
        if (v instanceof Button) {
            int chapterID = 1;
            try {
                chapterID = Integer.parseInt((((Button) v).getText() + ""));
            } catch (NumberFormatException e) {
                Log.e(TAG, "EXCEPTION : " + e.getLocalizedMessage());
                e.printStackTrace();
            }
            txtHeader.setText(currentBookName + " Chapter " + chapterID);
            updateVerseView(chapterID);
        } else {
            Log.w(TAG, "onClick called by unexpected widget " + v.getClass().getName());
        }
        Log.i(TAG, "Exiting onClick");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "Entering onItemLongClick");
        String verse = currentBookName + " Chapter " + currentChapter
                       + " Verse " + ((TextView) view).getText()
                       + "\n-- The Holy Bible (New International Version)";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, verse);
        startActivity(intent);
        Log.i(TAG, "Exiting onItemLongClick");
        return true;
    }

    protected class VerseListAdapter
            extends ArrayAdapter<String> {

        public VerseListAdapter(Context context, int resource, int textViewResourceId,
                                List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View textView = super.getView(position, convertView, parent);
            TextView item = (TextView) textView.findViewById(android.R.id.text1);

            switch (Integer.parseInt(Utilities.getStringPreference("verse_text_style", "0"))) {
                case 1:
                    item.setTypeface(Typeface.SERIF);
                    break;
                case 2:
                    item.setTypeface(Typeface.MONOSPACE);
                    break;
                default:
                    item.setTypeface(Typeface.DEFAULT);
                    break;
            }
            return textView;
        }
    }
}
