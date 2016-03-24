/*
 * This file 'ChapterViewActivity.java' is part of SimpleBible :
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
 */

package com.andrewchelladurai.simplebible.v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.DatabaseUtility;
import com.andrewchelladurai.simplebible.R;

import java.util.ArrayList;

public class ChapterViewActivity
        extends AppCompatActivity {

    public static final String ARG_CHAPTER_NUMBER = "ARG_CHAPTER_NUMBER";
    public static final String ARG_BOOK_NUMBER = "ARG_BOOK_NUMBER";
    private static final String TAG = "ChapterViewActivity";
    private static ArrayAdapter<String> verseListAdapter;
    private int bookNumber, chapterNumber;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        if (savedState != null) {
            bookNumber = Integer.parseInt(savedState.get(ARG_BOOK_NUMBER) + "");
            chapterNumber = Integer.parseInt(savedState.get(ARG_CHAPTER_NUMBER) + "");
            Log.d(TAG, "onCreate: Inside savedState != null = true");
        } else {
            bookNumber = Integer.parseInt(getIntent().getStringExtra(ARG_BOOK_NUMBER) + "");
            chapterNumber = Integer.parseInt(getIntent().getStringExtra(ARG_CHAPTER_NUMBER) + "");
            Log.d(TAG, "onCreate: Inside savedState != null = false");
        }
        Log.d(TAG, "onCreate: Book : Chapter" + bookNumber + " : " + chapterNumber);

        BookNameContent.BookNameItem item = BookNameContent.getBookItem(bookNumber);
        if (item != null) {
            setTitle(item.getName() + " : Chapter " + chapterNumber);
        }

        setContentView(R.layout.activity_chapter_viewv2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        verseListAdapter = new AdapterVerseList(getBaseContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>(1));

        ListViewCompat listViewCompat = (ListViewCompat) findViewById(R.id.chapter_content_list);
        if (listViewCompat != null) {
            listViewCompat.setAdapter(verseListAdapter);
            listViewCompat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    return handleVerseLongClick(view);
                }
            });
        }
        refreshVersesList(bookNumber, chapterNumber);
    }

    public void refreshVersesList(int bookNumber, int chapterNumber) {
        Log.d(TAG, "refreshVersesList() called with bookNumber = ["
                + bookNumber + "], chapterNumber = [" + chapterNumber + "]");
        ArrayList<String> versesList = DatabaseUtility.getInstance(getApplicationContext())
                .getAllVerseOfChapter(bookNumber + 1, chapterNumber);
        verseListAdapter.clear();
        verseListAdapter.addAll(versesList);
        verseListAdapter.notifyDataSetChanged();

        ListViewCompat listViewCompat = (ListViewCompat) findViewById(R.id.chapter_content_list);
        if (listViewCompat != null) {
            listViewCompat.setSelectionAfterHeaderView();
        }
    }

    private boolean handleVerseLongClick(View view) {
        String title = getTitle().toString();
        title = title.replace("Chapter ", "");
        String verse = ((TextView) view).getText()
                + " -- The Holy Bible (New International Version)";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, title + ":" + verse);
        startActivity(intent);
        return true;
    }
}
