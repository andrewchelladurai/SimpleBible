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

import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.BookmarksTabOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList;
import com.andrewchelladurai.simplebible.utilities.Constants;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 15-Sep-2016 @ 4:47 PM
 */
public class BookmarksTabPresenter {

    private static final String TAG = "SB_BM_Presenter";
    private BookmarksTabOperations mFragment;

    public BookmarksTabPresenter(BookmarksTabOperations fragment) {mFragment = fragment;}

    /**
     * Will check if the clciked bookmark is already present in the database.
     *
     * @param item
     *
     * @return Constants.Error | PRESENT_IN_DATABASE | ABSENT_IN_DATABASE
     */
    public String isBookmarkAlreadyPresentInDatabase(BookmarkList.BookmarkItem item) {
        if (null == item) {
            return Constants.ERROR;
        }
        return Constants.PRESENT_IN_DATABASE;
//        return Constants.ABSENT_IN_DATABASE;
    }

    public void deleteButtonClicked(BookmarkList.BookmarkItem item) {
        Log.d(TAG, "deleteButtonClicked() called with: " + "item = [" + item + "]");
    }

    public void shareButtonClicked(BookmarkList.BookmarkItem item) {
        Log.d(TAG, "shareButtonClicked() called with: " + "item = [" + item + "]");
    }
}
