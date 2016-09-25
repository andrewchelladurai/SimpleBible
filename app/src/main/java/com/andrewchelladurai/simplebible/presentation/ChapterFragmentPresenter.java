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

import com.andrewchelladurai.simplebible.interaction.ChapterFragmentOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.model.ChapterList;
import com.andrewchelladurai.simplebible.model.VerseList;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 26-Sep-2016 @ 1:13 AM
 */
public class ChapterFragmentPresenter {

    private static final String TAG = "SB_CF_Presenter";
    private ChapterFragmentOperations mOperations;

    public ChapterFragmentPresenter(ChapterFragmentOperations operations) {
        mOperations = operations;
    }

    public List<VerseList.VerseItem> getAllVersesForChapter(BooksList.BookItem bookItem,
                                                            ChapterList.ChapterItem chapterItem) {
        int bookNumber = bookItem.getBookNumber();
        int chapterNumber = chapterItem.getChapterNumber();
        Log.d(TAG, "getAllVersesForChapter() called with: bookNumber = ["
                   + bookNumber + "], chapterNumber = [" + chapterNumber + "]");

        if (bookNumber < 1 | chapterNumber < 1) {
            Log.d(TAG,
                  "getAllVersesForChapter() returning null coz : bookNumber OR chapterNumber < 1");
            return null;
        }

        DBUtility dbUtility = DBUtility.getInstance();
        ArrayList<String> verses = dbUtility.getAllVerseForChapter(bookNumber, chapterNumber);
        if (verses.size() < 1) {
            Log.d(TAG, "getAllVersesForChapter: returning null coz No verses found in DB");
        }

        boolean returnValue = VerseList.populateList(bookNumber, chapterNumber, verses);
        return (returnValue) ? VerseList.getItems() : null;
    }
}
