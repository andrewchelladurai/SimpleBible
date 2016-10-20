/*
 *
 * This file 'BookmarkEntryActivityPresenter.java' is part of SimpleBible :
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

import android.support.annotation.NonNull;
import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.BookmarkActivityOperations;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 17-Sep-2016 @ 12:48 AM
 */
public class BookmarkActivityPresenter {

    private static final String TAG = "SB_BA_Presenter";
    private BookmarkActivityOperations mOperations;

    public BookmarkActivityPresenter(BookmarkActivityOperations operations) {
        mOperations = operations;
        Log.d(TAG, "BookmarkActivityPresenter: init done");
    }

    public boolean buttonShareClicked() {
        Log.d(TAG, "buttonShareClicked() called");
        return true;
    }

    public boolean buttonDeleteClicked(@NonNull String reference) {
        Log.d(TAG, "buttonDeleteClicked() called with: reference = [" + reference + "]");
        BookmarkItem item = BookmarkList.getItem(reference);
        if (item == null) {
            Log.e(TAG, "buttonDeleteClicked: No Bookmark Item exist for passed reference");
            return false;
        }

        DBUtilityOperations dbu = DBUtility.getInstance();
        String references = item.getReferences();
        if (references == null || references.isEmpty()) {
            Log.e(TAG, "buttonDeleteClicked: BookmarkItem has empty reference.");
            return false;
        }
        boolean isDeleted = dbu.deleteBookMarkEntry(references);
        if (isDeleted) {
            BookmarksTabPresenter.refreshList();
        }
        return isDeleted;
    }

    public boolean buttonEditClicked() {
        Log.d(TAG, "buttonEditClicked() called");
        return true;
    }

    public boolean buttonSaveClicked() {
        Log.d(TAG, "buttonSaveClicked() called");
        String references = mOperations.getPassedReference();
        String note = mOperations.getInputNote();
        Log.d(TAG, "buttonSaveClicked: reference [" + references + "] ,"
                   + "note Length [" + note.length() + "]");

        DBUtilityOperations dbu = DBUtility.getInstance();
        return dbu.createNewBookmark(references, note);
    }

    public String getSavedNote(@NonNull String reference) {
        Log.d(TAG, "getSavedNote() called with: reference = [" + reference + "]");
        DBUtilityOperations dbu = DBUtility.getInstance();
        return dbu.getNoteForReference(reference);
    }
}
