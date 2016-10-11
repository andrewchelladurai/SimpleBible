/*
 *
 * This file 'SearchFragmentPresenter.java' is part of SimpleBible :
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
import com.andrewchelladurai.simplebible.interaction.SearchTabOperations;
import com.andrewchelladurai.simplebible.model.SearchResultList;
import com.andrewchelladurai.simplebible.model.SearchResultList.SearchResultItem;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by andrew on 10/9/16. Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 10-Sep-2016 @ 10:45 PM
 */
public class SearchTabPresenter {

    private static final String TAG = "SB_SF_Presenter";
    private SearchTabOperations mOperations;

    public SearchTabPresenter(SearchTabOperations fragmentInterface) {
        mOperations = fragmentInterface;
    }

    public void init() {
        Log.d(TAG, "init() called:");
    }

    public String searchButtonClicked(@NonNull String input) {
        Log.d(TAG, "searchButtonClicked() called");
        String returnValue = Constants.SUCCESS;
        SearchResultList.clearList();
        mOperations.refreshList();

        // check if the input is empty or null
        if (input.isEmpty()) {
            returnValue = mOperations.getEmptyInputErrorMessage();
            return returnValue;
        }
        // if input is only 2 chars, return
        if (input.length() < 3) {
            returnValue = mOperations.getInputMinLengthErrorMessage();
            return returnValue;
        }
        // if input is more than 50 chars, return
        if (input.length() > 50) {
            returnValue = mOperations.getInputMaxLengthErrorMessage();
            return returnValue;
        }
        return returnValue;
    }

    public void resetButtonClicked() {
        Log.d(TAG, "resetButtonClicked() called");
        SearchResultList.clearList();
        mOperations.resetFields();
        mOperations.showSearchButton();
    }

    public void getSearchResultsForText(String input) {
        Log.d(TAG, "getSearchResultsForText() called with: input = [" + input + "]");
        DBUtilityOperations dbUtility = DBUtility.getInstance();
        ArrayList<String[]> versesList = dbUtility.searchForInput(input);

        if (versesList == null || versesList.size() < 1) {
            mOperations.showMessage(mOperations.getResultsCountString(0));
            return;
        }

        boolean successful = SearchResultList.populateList(input, versesList);
        if (successful) {
            mOperations.showMessage(mOperations.getResultsCountString(versesList.size()));
            mOperations.refreshList();
            mOperations.showResetButton();
        } else {
            mOperations.resetFields();
        }
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
        Collection<SearchResultItem> items = SearchResultList.getSelectedItems();
        String shareTextTemplate = mOperations.getShareTemplate();
        String bookName;
        int chapterNumber, verseNumber;
        String verseText;
        StringBuilder shareText = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (SearchResultItem item : items) {
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
}
