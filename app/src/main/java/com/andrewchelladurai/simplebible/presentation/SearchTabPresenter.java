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

import com.andrewchelladurai.simplebible.interaction.SearchTabOperations;
import com.andrewchelladurai.simplebible.model.SearchResultList.SearchResultItem;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.DBUtility;
import com.andrewchelladurai.simplebible.utilities.DBUtilityOperations;

import java.util.ArrayList;

/**
 * Created by andrew on 10/9/16. Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 10-Sep-2016 @ 10:45 PM
 */
public class SearchTabPresenter {

    private static final String TAG = "SB_SF_Presenter";
    private ArrayList<SearchResultItem> mSelectedItems;
    private SearchTabOperations         mInterface;

    public SearchTabPresenter(SearchTabOperations fragmentInterface) {
        mInterface = fragmentInterface;
    }

    public void init() {
        Log.d(TAG, "init() called:");
        if (null == mSelectedItems || mSelectedItems.isEmpty()) {
            mSelectedItems = new ArrayList<>(0);
            Log.d(TAG, "init: SelectedItems created");
        } else {
            Log.d(TAG, "init: " + mSelectedItems.size() + " SelectedItems exist");
        }
    }

    public String searchButtonClicked(@NonNull String input) {
        Log.d(TAG, "searchButtonClicked() called");
        String returnValue = Constants.SUCCESS;

        // check if the input is empty or null
        if (input.isEmpty()) {
            returnValue = mInterface.getEmptyInputErrorMessage();
            return returnValue;
        }
        // if input is only 2 chars, return
        if (input.length() < 3) {
            returnValue = mInterface.getInputMinLengthErrorMessage();
            return returnValue;
        }
        // if input is more than 50 chars, return
        if (input.length() > 50) {
            returnValue = mInterface.getInputMaxLengthErrorMessage();
            return returnValue;
        }
        return returnValue;
    }

    public void resetButtonClicked() {
        Log.d(TAG, "resetButtonClicked() called");
        mInterface.resetFields();
        mInterface.showSearchButton();
    }

    private String addOrRemoveSelectedItem(SearchResultItem item) {
        if (null == item) {
            return Constants.ERROR;
        }
        if (mSelectedItems.contains(item)) {
            mSelectedItems.remove(item);
            Log.d(TAG, "searchResultLongClicked: Item removed");
            return Constants.REMOVED;
        } else {
            mSelectedItems.add(item);
            Log.d(TAG, "searchResultLongClicked: Item Added");
            return Constants.ADDED;
        }
    }

    public String searchResultLongClicked(SearchResultItem item) {
        Log.d(TAG, "searchResultClicked() called with: " + "item = [" + item + "]");
        return addOrRemoveSelectedItem(item);
    }

    public boolean isItemSelected(SearchResultItem item) {
        return null != item && mSelectedItems.contains(item);
    }

    public void getSearchResultsForText(String input) {
        Log.d(TAG, "getSearchResultsForText() called with: input = [" + input + "]");
        DBUtilityOperations dbUtility = DBUtility.getInstance();
        ArrayList<String[]> versesList = dbUtility.searchForInput(input);

        if (versesList == null || versesList.size() < 1) {
            mInterface.showMessage(mInterface.getResultsCountString(0));
            return;
        }

        mInterface.showMessage(mInterface.getResultsCountString(versesList.size()));
        mInterface.showResetButton();
    }
}
