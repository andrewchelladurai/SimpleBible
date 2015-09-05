/*
 *
 * This file is part of SimpleBible : A Holy Bible Application on the
 * Android Mobile platform with easy navigation and offline access.
 *
 * Copyright (c) 2015.
 * Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
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

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_VerseViewer
        extends ActionBarActivity
        implements View.OnClickListener,
                   AdapterView.OnItemLongClickListener {

    private final String CLASS_NAME = "Activity_VerseViewer";
    private int               currentBookId;
    private int               currentChapter;
    private int               chapterCount;
    private String            currentBookName;
    private ListView          verseListView;
    private ArrayAdapter<String> verseListAdapter;
    private ArrayList<String> arrayList;
    private TextView          txtHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(CLASS_NAME, "Entering onCreate");
        Activity_Settings.changeTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_viewer);

        Log.d(CLASS_NAME, "ID = " + getIntent().getIntExtra("ID", 1) + "");
        updateVariables(getIntent().getIntExtra("ID", 1));
        setTitle(currentBookName);

        // Always load 1st Chapter when loading a new Book
        txtHeader.setText(currentBookName + " Chapter 1");
        updateVerseView(1);
        Log.i(CLASS_NAME, "Exiting onCreate");
    }

    private void updateVariables(int id) {
        Log.i(CLASS_NAME, "Entering updateVariables" + id);
        currentBookName = BookList.getBookName(id);
        chapterCount = BookList.getTotalChapters(id);
        currentBookId = BookList.getBookNumber(id);

        verseListView = (ListView) findViewById(R.id.verseListView);
        verseListView.setOnItemLongClickListener(this);
        txtHeader = (TextView) findViewById(R.id.verseHeader);

        arrayList = new ArrayList<>(1);
        verseListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                                                    android.R.id.text1, arrayList);
        verseListView.setAdapter(verseListAdapter);
        Log.i(CLASS_NAME, "Exiting updateVariables");
    }

    private void updateVerseView(int chapterID) {
        Log.i(CLASS_NAME, "Entering updateVerseView");
        DataBaseHelper dataBaseHelper = Activity_Welcome.getDataBaseHelper();
        Cursor cursor = dataBaseHelper.getDBRecords(currentBookId + 1, chapterID);

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
        Log.i(CLASS_NAME, "Exiting updateVerseView");
    }

    private void createChapterButtons() {
        Log.i(CLASS_NAME, "Entering createChapterButtons");
        LinearLayout layout = (LinearLayout) findViewById(R.id.chapterButtonsLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
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
        Log.i(CLASS_NAME, "Exiting createChapterButtons");
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
            startActivity(new Intent(this, Activity_Settings.class));
            return true;
        default:
            Log.e(CLASS_NAME, "ERROR : Option Item Selected hit Default : " + item.getTitle());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.i(CLASS_NAME, "Entering onClick");
        if (v instanceof Button) {
            int chapterID = 1;
            try {
                chapterID = Integer.parseInt((((Button) v).getText() + ""));
            } catch (NumberFormatException e) {
                Log.e(CLASS_NAME, "EXCEPTION : " + e.getLocalizedMessage());
                e.printStackTrace();
            }
            txtHeader.setText(currentBookName + " Chapter " + chapterID);
            updateVerseView(chapterID);
        } else {
            Log.w(CLASS_NAME, "onClick called by unexpected widget " + v.getClass().getName());
        }
        Log.i(CLASS_NAME, "Exiting onClick");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(CLASS_NAME, "Entering onItemLongClick");
        String verse = currentBookName + " Chapter " + currentChapter
                       + " Verse " + ((TextView) view).getText()
                       + " -- The Holy Bible (New International Version)";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, verse);
        startActivity(intent);
        Log.i(CLASS_NAME, "Exiting onItemLongClick");
        return true;
    }

}
