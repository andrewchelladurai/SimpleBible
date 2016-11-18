/*
 *
 * This file 'ChapterFragmentPresenter.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.presentation;

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.interaction.ChapterFragmentOperations;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.model.BooksList.BookItem;
import com.andrewchelladurai.simplebible.model.ChapterList.ChapterItem;
import com.andrewchelladurai.simplebible.model.VerseList;
import com.andrewchelladurai.simplebible.model.VerseList.VerseItem;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 26-Sep-2016 @ 1:13 AM
 */
public class ChapterFragmentPresenter {

    private static final String TAG = "SB_CF_Presenter";
    private final ChapterFragmentOperations mOperations;

    public ChapterFragmentPresenter(ChapterFragmentOperations operations) {
        mOperations = operations;
    }

    public void init() {
        Log.d(TAG, "init() called");
    }

    public List<VerseItem> getAllVersesForChapter(BookItem bookItem, ChapterItem chapterItem) {
        int bookNumber = bookItem.getBookNumber();
        int chapterNumber = chapterItem.getChapterNumber();
        Log.d(TAG, "getAllVersesForChapter() called with: bookNumber = ["
                   + bookNumber + "], chapterNumber = [" + chapterNumber + "]");

        if (bookNumber < 1 | chapterNumber < 1) {
            Log.e(TAG,
                  "getAllVersesForChapter() returning null coz : bookNumber OR chapterNumber < 1");
            return null;
        }

        DBUtilityOperations dbUtility = DBUtility.getInstance();
        ArrayList<String> verses = dbUtility.getAllVerseForChapter(bookNumber, chapterNumber);
        if (verses.size() < 1) {
            Log.e(TAG, "getAllVersesForChapter: returning null coz No verses found in DB");
            return null;
        }

        boolean returnValue = VerseList.populateList(bookNumber, chapterNumber, verses);
        return (returnValue) ? VerseList.getItems() : null;
    }

    public String bookmarkButtonClicked(String reference) {
        Log.d(TAG, "bookmarkButtonClicked() called with: reference = [" + reference + "]");

        DBUtilityOperations dbu = DBUtility.getInstance();

        boolean referenceExists = dbu.doesBookmarkReferenceExist(reference);
        if (referenceExists) {
            return BookmarkActivityOperations.VIEW;
        } else {
            return BookmarkActivityOperations.CREATE;
        }
    }

    public void shareButtonClicked() {
        Log.d(TAG, "shareButtonClicked() called");
        Collection<VerseItem> items = VerseList.getSelectedItems();

        String shareTextTemplate = mOperations.getShareTemplate();
        String bookName;
        int chapterNumber, verseNumber;
        String verseText;
        StringBuilder shareText = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for (VerseItem item : items) {
            temp.delete(0, temp.length());
            bookName = item.getBookName();
            chapterNumber = item.getChapterNumber();
            verseNumber = item.getVerseNumber();
            verseText = item.getVerseText();
            temp.append(String.format(
                    shareTextTemplate, verseText, bookName, chapterNumber, verseNumber))
                .append("\n");
            shareText.append(temp);
        }

        mOperations.shareSelectedVerses(shareText.toString());
    }

    public boolean resetButtonClicked() {
        Log.d(TAG, "resetButtonClicked() called");
        Collection<VerseItem> selectedItems = VerseList.getSelectedItems();
        return (selectedItems.size() > 0) && VerseList.clearSelectedItems();
    }
}
