/*
 * This file 'ChapterVerseActivity.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityChapterView
        extends AppCompatActivity {

    public static final String ARG_CHAPTER_NUMBER = "ARG_CHAPTER_NUMBER";
    public static final String ARG_BOOK_NUMBER = "ARG_BOOK_NUMBER";
    private static final String TAG = "ChapterVerseActivity";
    private int bookNumber = 0, chapterNumber = 0;
    private ArrayAdapter<String> verseListAdapter;
    private ListViewCompat listViewCompat;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        String value = getIntent().getStringExtra(ARG_BOOK_NUMBER);
        bookNumber = (value == null) ? 0 : Integer.parseInt(value);
        value = getIntent().getStringExtra(ARG_CHAPTER_NUMBER);
        chapterNumber = (value == null) ? 0 : Integer.parseInt(value);

        if (chapterNumber < 0) {
            Log.e(TAG, "onCreate", new RuntimeException("[BookNumber] + [ChapterNumber = Zero] : ["
                    + bookNumber + "][" + chapterNumber + "]"));
            return;
        }

        getIntent().getExtras().putString(ARG_BOOK_NUMBER, bookNumber + "");
        getIntent().getExtras().putString(ARG_CHAPTER_NUMBER, chapterNumber + "");

        setContentView(R.layout.activity_chapter_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_chapter_view_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        verseListAdapter = new AdapterVerseList(this, android.R.layout.simple_list_item_1,
                new ArrayList<String>(1));
        listViewCompat =
                (ListViewCompat) findViewById(R.id.activity_chapter_view_list_verses);
        if (listViewCompat != null) {
            listViewCompat.setAdapter(verseListAdapter);
            listViewCompat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    return handleVerseLongClick(view);
                }
            });
        }

        AppCompatButton notesButton = (AppCompatButton) findViewById(
                R.id.activity_chapter_view_button_notes);
        if (notesButton != null) {
            notesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleNotesButtonClick();
                }
            });
        }

        AppCompatButton searchButton = (AppCompatButton) findViewById(
                R.id.activity_chapter_view_button_search);
        if (searchButton != null) {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleSearchButtonClick();
                }
            });
        }

        AppCompatButton chaptersButton = (AppCompatButton) findViewById(
                R.id.activity_chapter_view_button_chapters);
        if (chaptersButton != null) {
            chaptersButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChapterSelectionDialog();
                }
            });
        }

        handleNewChapterSelected(chapterNumber);
    }

    private void handleNotesButtonClick() {
        SimpleBible.showNotesSection();
        finish();
    }

    private void handleSearchButtonClick() {
        SimpleBible.showSearchSection();
        finish();
    }

    private void handleNewChapterSelected(int chapter) {
        Log.d(TAG, "handleNewChapterSelected: " + chapter);
        BookNameContent.BookItem book = BookNameContent.getBookItem(bookNumber);
        if (book != null) {
            if (chapter < 1 || chapter > book.getChapterCount()) {
                Log.e(TAG, "onCreate", new RuntimeException("[BookNumber] + [Incorrect ChapterNumber] : ["
                        + bookNumber + "][" + chapterNumber + "]"));
            } else {
                chapterNumber = chapter;
                refreshVersesList();
            }
        }
    }

    private void showChapterSelectionDialog() {
        BookNameContent.BookItem book = BookNameContent.getBookItem(bookNumber);
        if (book == null) {
            return;
        }

        int cc = book.getChapterCount();
        String chaps[] = new String[cc];
        for (int i = 0; i < cc; i++) {
            chaps[i] = "" + (i + 1);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.fragment_goto_label_chapter_text));
        GridView gridView = new GridView(this);
        gridView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chaps));
        gridView.setNumColumns(5);
        builder.setView(gridView);
        final AlertDialog ad = builder.create();
        ad.show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ad.dismiss();
                handleNewChapterSelected(position + 1);
            }
        });

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

    public void refreshVersesList() {
        Log.d(TAG, "refreshVersesList() called with bookNumber = ["
                + bookNumber + "], chapterNumber = [" + chapterNumber + "]");
        ArrayList<String> versesList = DatabaseUtility.getInstance(this)
                .getAllVerseOfChapter(bookNumber + 1, chapterNumber);
        verseListAdapter.clear();
        verseListAdapter.addAll(versesList);
        verseListAdapter.notifyDataSetChanged();
        listViewCompat.setSelectionAfterHeaderView();

        BookNameContent.BookItem book = BookNameContent.getBookItem(bookNumber);
        String title = (book != null ? book.getName() : "Unknown Book") + " Chapter " + chapterNumber;
        setTitle(title);
    }
}
