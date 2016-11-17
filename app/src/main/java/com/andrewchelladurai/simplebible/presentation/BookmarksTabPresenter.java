/*
 *
 * This file 'BookmarksTabPresenter.java' is part of SimpleBible :
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

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.BookmarksTabOperations;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;
import com.andrewchelladurai.simplebible.utilities.DBUtility;
import com.andrewchelladurai.simplebible.utilities.Utilities;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 15-Sep-2016 @ 4:47 PM
 */
public class BookmarksTabPresenter {

    private static final String TAG = "SB_BM_Presenter";
    private static BookmarksTabOperations mOperations;

    public BookmarksTabPresenter(BookmarksTabOperations operations) {mOperations = operations;}

    static void refreshList() {
        mOperations.refresh();
    }

    public boolean doesBookmarkReferenceExist(@NonNull String references) {
        Log.d(TAG, "doesBookmarkReferenceExist() called with: references = [" + references + "]");
        DBUtilityOperations dbu = DBUtility.getInstance();
        return dbu.doesBookmarkReferenceExist(references);
    }

    public boolean deleteButtonClicked(@NonNull BookmarkItem item) {
        Log.d(TAG, "deleteButtonClicked() called with: " + "item = [" + item + "]");
        DBUtilityOperations dbu = DBUtility.getInstance();
        String references = item.getReferences();
        if (references == null || references.isEmpty()) {
            Log.e(TAG, "deleteButtonClicked: BookmarkItem has empty reference.");
            return false;
        }
        return dbu.deleteBookMarkEntry(references);
    }

    public void shareButtonClicked(@NonNull BookmarkItem item) {
        Log.d(TAG, "shareButtonClicked() called with: " + "item = [" + item + "]");
        String references = item.getReferences();
        if (references == null || references.isEmpty()) {
            Log.e(TAG, "shareButtonClicked: BookmarkItem has Zero References");
            return;
        }

        String verseTemplate = mOperations.getVerseTemplate();
        String verseText = Utilities.getShareableTextForReferences(references, verseTemplate);

        String note = item.getNote();
        note = (note.isEmpty()) ? mOperations.getResourceString(R.string.empty) : note;

        String shareBookmarkTemplate = mOperations.getShareBookmarkTemplate();
        String shareText = String.format(shareBookmarkTemplate, verseText, note);

        mOperations.shareSelectedVerses(shareText);
    }
}
