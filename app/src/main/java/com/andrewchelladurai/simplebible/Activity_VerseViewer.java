/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 */

package com.andrewchelladurai.simplebible;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_VerseViewer
      extends ActionBarActivity
      implements View.OnClickListener {

    private int                  bookID;
    private String               bookName;
    private int                  chapterCount;
    private ListView             verseListView;
    private ArrayAdapter<String> verseListAdapter;
    private ArrayList<String>    arrayList;
    private TextView             txtHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_viewer);

        Log.d("ID = ", getIntent().getIntExtra("ID", 1) + "");
        updateVariables(getIntent().getIntExtra("ID", 1));
        setTitle(bookName);

        // Always load 1st Chapter when loading a new Book
        txtHeader.setText(bookName + " Chapter 1");
        updateVerseView(1);
    }

    private void updateVariables(int id) {
        bookName = BookList.books.get(id).getBookName();
        chapterCount = BookList.books.get(id).getTotalChapters();
        bookID = BookList.books.get(id).getBookNumber();

        verseListView = (ListView) findViewById(R.id.verseListView);
        txtHeader = (TextView) this.findViewById(R.id.verseHeader);

        arrayList = new ArrayList<>(1);

        verseListAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                                    android.R.layout
                                                          .simple_list_item_1,
                                                    android.R.id.text1, arrayList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View     view     = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                int      theme    = -1;
                try {
                    PackageInfo packageInfo = getPackageManager()
                          .getPackageInfo(getApplicationContext().getPackageName(),
                                          PackageManager.GET_META_DATA);
                    theme = (null != packageInfo) ? packageInfo.applicationInfo.theme
                                                  : R.style.LightTheme;
                } catch (NameNotFoundException e) {
                    Log.e("EXCEPTION =>",
                          "NameNotFoundException @ updateVariables()");
                    e.printStackTrace();
                } finally {
                    if (theme == R.style.DarkTheme) {
                        textView.setTextColor(Color.WHITE);
                    } else {
                        textView.setTextColor(Color.BLACK);
                    }
                }
                return view;
            }
        };
        verseListView.setAdapter(verseListAdapter);
    }

    private void updateVerseView(int chapterID) {
        DataBaseHelper dataBaseHelper = Activity_Welcome.getMyDbHelper();
        Cursor         cursor         = dataBaseHelper.getDBRecords(bookID + 1,
                                                                    chapterID);

        arrayList.clear();
        verseListAdapter.clear();

        if (cursor.moveToFirst()) {
            int verseIndex = cursor.getColumnIndex("Verse");
            int verseIdIndex = cursor.getColumnIndex("VerseId");
            //            int chapterIdIndex = cursor.getColumnIndex("ChapterId");
            //            int bookIdIndex = cursor.getColumnIndex("BookId");
            do {
                arrayList.add(cursor.getInt(verseIdIndex) + ": " + cursor.getString(
                      verseIndex));
            } while (cursor.moveToNext());
            verseListView.refreshDrawableState();
            verseListAdapter.notifyDataSetChanged();
            txtHeader.setText(bookName + " Chapter " + chapterID);
//            if (!cursor.isClosed()) {
            cursor.close();
//            }
        }
        createChapterButtons();
    }

    private void createChapterButtons() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                Log.e("Error",
                      "Option Item Selected hit Default : " + item.getTitle());
        }
        // Toast.makeText(this, "In the Next Release", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int chapterID = 1;
        try {
            chapterID = Integer.parseInt((((Button) v).getText() + ""));
        } catch (NumberFormatException e) {
            Log.e("EXCEPTION", e.getLocalizedMessage());
            e.printStackTrace();
        }
        txtHeader.setText(bookName + " Chapter " + chapterID);
        updateVerseView(chapterID);
    }
}
