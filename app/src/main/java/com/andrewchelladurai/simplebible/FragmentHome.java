/*
 *
 * This file 'FragmentHome.java' is part of SimpleBible :
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome
        extends Fragment
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "SB_FragmentHome";
    private ArrayAdapter<String>          mChapterAdapter;
    private AppCompatAutoCompleteTextView mChapterInput;
    private AppCompatTextView             mDailyVerse;
    private AppCompatAutoCompleteTextView mBookInput;

    public FragmentHome() {
    }

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        DatabaseUtility dbu = DatabaseUtility.getInstance(fragment.getContext());
        String todayVerseRef = dbu.getVerseReferenceForToday();

        args.putString(Utilities.TODAY_VERSE_REFERENCE, todayVerseRef);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mDailyVerse = (AppCompatTextView) view.findViewById(R.id.frag_home_daily_verse);
        displayVerse();

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.frag_home_but_goto);
        button.setOnClickListener(this);

        mBookInput = (AppCompatAutoCompleteTextView) view.findViewById(R.id.frag_home_book_name);
        List<ListBooks.Entry> items = ListBooks.getItems();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            list.add(i, items.get(i).getName());
        }
        mBookInput.setAdapter(
                new ArrayAdapter<>(getContext(),
                                   android.R.layout.simple_dropdown_item_1line, list));
        mBookInput.setOnItemClickListener(this);

        mChapterInput =
                (AppCompatAutoCompleteTextView) view.findViewById(R.id.frag_home_chapter_number);
        mChapterAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line,
                new ArrayList<String>(0));
        mChapterInput.setAdapter(mChapterAdapter);

        return view;
    }

    private void displayVerse() {
        String verseRef = getArguments().getString(Utilities.TODAY_VERSE_REFERENCE);
        if (verseRef == null || verseRef.isEmpty()) {
            Utilities.log(TAG, "displayVerse: incorrect verseRef = " + verseRef + " using default");
            verseRef = "43:3:16"; // John 3:16 - The Default verse
        }
        String parts[] = verseRef.split(Utilities.DELIMITER_IN_REFERENCE);
        if (parts.length != 3) {
            Utilities.log(TAG, "displayVerse: incorrect verseRef = " + verseRef + " using default");
            verseRef = "43:3:16"; // John 3:16 - The Default verse
        }
        parts = verseRef.split(Utilities.DELIMITER_IN_REFERENCE);

        mDailyVerse.setText(Html.fromHtml(Utilities.getFormattedDailyVerse(parts)));
    }

    @Override
    public void onClick(View view) {
        if (view instanceof AppCompatButton & view.getId() == R.id.frag_home_but_goto) {
            buttonGotoClicked();
        }
    }

    private void buttonGotoClicked() {
        ListBooks.Entry book = getBookDetails();
        if (book == null) {
            mBookInput.setError(getString(R.string.message_incorrect_book_name));
            return;
        }

        String chapterNumStr = getChapterNumber();
        int chapterNumber;

        if (chapterNumStr.isEmpty()) {
            chapterNumber = 1;
        } else {
            try {
                chapterNumber = Integer.parseInt(chapterNumStr);
            } catch (NumberFormatException npe) {
                chapterNumber = 1;
                mChapterInput.setError(getString(R.string.message_incorrect_chapter_number));
                Utilities.log(TAG, "buttonGotoClicked " + npe.getLocalizedMessage());
            }
            if (chapterNumber < 1 || chapterNumber > Integer.parseInt(book.getChapterCount())) {
                chapterNumber = 1;
                mChapterInput.setError(getString(R.string.message_incorrect_chapter_number));
            }
        }
        Utilities.log(TAG, "buttonGotoClicked() called [" + book + "] [" + chapterNumber + "]");

        String chapterText = getString(R.string.chapter_list_prepend_text).trim();
        ListChapter.populateList(Integer.parseInt(book.getChapterCount()), chapterText);
        ListChapter.Entry chapter = ListChapter.getItem(String.valueOf(chapterNumber));
        Utilities.log(TAG, "buttonGotoClicked() chapter parcel created");

        Bundle args = new Bundle();
        args.putParcelable(Utilities.CURRENT_BOOK, book);
        args.putString(Utilities.CURRENT_CHAPTER_NUMBER, String.valueOf(chapterNumber));
        args.putParcelable(Utilities.CURRENT_CHAPTER, chapter);
        args.putString(Utilities.LOAD_CHAPTER, Utilities.LOAD_CHAPTER_YES);

        Intent intent = new Intent(getContext(), ActivityChapterList.class);
        intent.putExtras(args);
        resetValues();
        startActivity(intent);
    }

    private ListBooks.Entry getBookDetails() {
        String bookName = mBookInput.getText().toString().trim();

        ListBooks.Entry book = ListBooks.getBook(bookName);
        Utilities.log(TAG, "getBookDetails() returned: " + book);
        return book;
    }

    private String getChapterNumber() {
        String chapterNumber = mChapterInput.getText().toString().trim();

        Utilities.log(TAG, "getChapterNumber() returned: " + chapterNumber);
        return chapterNumber;
    }

    private void resetValues() {
        Utilities.log(TAG, "resetValues() called");
        mChapterAdapter.clear();
        mChapterInput.setError(null);
        mBookInput.setError(null);
        mChapterAdapter.notifyDataSetChanged();
        mBookInput.setText("");
        mChapterInput.setText("");
        mBookInput.requestFocus();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListBooks.Entry item = getBookDetails();
        refreshChapterInput(item.getChapterCount());
    }

    private int refreshChapterInput(String chapterCount) {
        int count = Integer.parseInt(chapterCount);
        mChapterAdapter.clear();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(i, String.valueOf(i + 1));
        }
        mChapterAdapter.addAll(list);
        mChapterAdapter.notifyDataSetChanged();
        Utilities.log(TAG, "refreshChapterInput() refreshed " + list.size() + " items");
        return list.size();
    }
}
