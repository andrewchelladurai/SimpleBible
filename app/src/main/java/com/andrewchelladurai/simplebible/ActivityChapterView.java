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

import java.util.ArrayList;

public class ActivityChapterView
        extends AppCompatActivity
        implements View.OnClickListener {

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
                    handleVerseLongClick(i);
                    return true;
                }
            });
        }

        bindButton(R.id.activity_chapter_view_button_notes);
        bindButton(R.id.activity_chapter_view_button_search);
        bindButton(R.id.activity_chapter_view_button_chapters);

        handleNewChapterSelected(chapterNumber);
    }

    private void bindButton(int buttonID) {
        AppCompatButton button = (AppCompatButton) findViewById(buttonID);
        if (button != null) {
            button.setOnClickListener(this);
        }
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
        gridView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chaps));
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

    private void handleVerseLongClick(int position) {
        VerseLongClickAlert alert = VerseLongClickAlert.newInstance(
                getTitle().toString(), position, this, listViewCompat);
        alert.showDialog();
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

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick() called with: viewID = [" + view.getId() + "]");
        switch (view.getId()) {
            case R.id.activity_chapter_view_button_chapters:
                showChapterSelectionDialog();
                break;
            case R.id.activity_chapter_view_button_notes:
                handleNotesButtonClick();
                break;
            case R.id.activity_chapter_view_button_search:
                handleSearchButtonClick();
                break;
            default:
                Log.e(TAG, "onClick: ", new RuntimeException(
                        "View with ID = " + view.getId() + " is not handled"));
        }
    }
}
