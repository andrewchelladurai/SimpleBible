/*
 *
 * This file 'DBUtility.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andrewchelladurai.simplebible.interaction.SimpleBibleActivityOperations;
import com.andrewchelladurai.simplebible.presentation.SimpleBibleActivityPresenter;
import com.andrewchelladurai.simplebible.utilities.Constants.SimpleBibleTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 23-Sep-2016 @ 1:00 AM
 */
public class DBUtility
        extends SQLiteOpenHelper
        implements DBUtilityOperations {

    private static final String    TAG          = "SB_DBUtility";
    private static       DBUtility thisInstance = null;
    private static SimpleBibleActivityOperations mActivityOperations;
    private boolean mVersionChanged = false;

    private DBUtility(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public static DBUtilityOperations getInstance(
            SimpleBibleActivityOperations activityOperations) {
        DBUtility.mActivityOperations = activityOperations;
        if (thisInstance == null) {
            thisInstance = new DBUtility(activityOperations.getThisApplicationContext());
            Log.d(TAG, "getInstance: Initialized Static Instance");
        }
        return thisInstance;
    }

    public static DBUtilityOperations getInstance()
    throws NullPointerException {
        if (thisInstance == null) {
            throw new NullPointerException("Static Instance is not yet initialized");
        } else {
            return thisInstance;
        }
    }

    @Override public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate() called");
        SimpleBibleActivityPresenter presenter = mActivityOperations.getPresenter();

        InputStreamReader mBaseScript = presenter.getMainScript();
        if (mBaseScript != null) {
            boolean result = executeScriptFile(mBaseScript, db);
            String msg = (result) ? "DB Setup Successfully" : "DB Setup Unsuccessful";
            Log.d(TAG, "onCreate: " + msg);
        }
// CREATE TABLE BOOK_MARKS ( "REFERENCE" TEXT PRIMARY KEY, "NOTE" TEXT );
    }

    private boolean executeScriptFile(InputStreamReader stream, SQLiteDatabase db) {
        Log.d(TAG, "executeScriptFile() called");
        BufferedReader reader = null;
        boolean isCreated = true, isClosed = true;
        try {
            reader = new BufferedReader(stream);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    db.execSQL(line);
                }
            }
            isCreated = true;
            Log.d(TAG, "executeScriptFile: Script finished running");
        } catch (IOException ioe) {
            isCreated = false;
            Log.d(TAG, "executeScriptFile: IOException : " + ioe.getLocalizedMessage());
            ioe.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                    isClosed = true;
                    Log.d(TAG, "executeScriptFile: Script closed");
                } catch (IOException e) {
                    isClosed = false;
                    Log.d(TAG, "executeScriptFile: IOE when closing file");
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "executeScriptFile() returned "
                   + "created = [" + isCreated + "] & closed = [" + isClosed + "]");
        return (isCreated & isClosed);
    }

    @Override public void onDowngrade(SQLiteDatabase db, int oldV, int newV) {
        Log.d(TAG, "onDowngrade() called with oldV = [" + oldV + "], newV = [" + newV + "]");
        SimpleBibleActivityPresenter presenter = mActivityOperations.getPresenter();
        InputStreamReader mDowngradeScript = presenter.getDowngradeScript();

        mVersionChanged = false;
        if (null != mDowngradeScript) {
            mVersionChanged = executeScriptFile(mDowngradeScript, db);
            Log.d(TAG, "onDowngrade: successful = " + mVersionChanged);
            if (mVersionChanged) {
                onCreate(db);
            }
        }
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        Log.d(TAG, "onUpgrade() called with: oldV = [" + oldV + "], newV = [" + newV + "]");
        SimpleBibleActivityPresenter presenter = mActivityOperations.getPresenter();
        InputStreamReader mUpgradeScript = presenter.getUpgradeScript();

        if (null != mUpgradeScript) {
            mVersionChanged = executeScriptFile(mUpgradeScript, db);
            Log.d(TAG, "onUpgrade: successful = " + mVersionChanged);
            if (mVersionChanged) {
                onCreate(db);
            }
        }
    }

    public String getVerseForReference(int bookNumber, int chapterNumber, int verseNumber) {
        String table = SimpleBibleTable.NAME;
        String[] columns = {SimpleBibleTable.COLUMN_BOOK_NUMBER,
                            SimpleBibleTable.COLUMN_CHAPTER_NUMBER,
                            SimpleBibleTable.COLUMN_VERSE_NUMBER,
                            SimpleBibleTable.COLUMN_VERSE_TEXT};

        String where = SimpleBibleTable.COLUMN_BOOK_NUMBER + " = ? AND " +
                       SimpleBibleTable.COLUMN_CHAPTER_NUMBER + " = ? AND " +
                       SimpleBibleTable.COLUMN_VERSE_NUMBER + " = ? ";
        String[] args = {bookNumber + "", chapterNumber + "", verseNumber + ""};

/*
        String query = SQLiteQueryBuilder.buildQueryString(
                true, table, columns, where, null, null, null, null);
        Log.d(TAG, "getVerseForReference: Query : " + query);
*/

        SQLiteDatabase database = getReadableDatabase();
        database.beginTransaction();
        Cursor cursor = database.query(
                true, table, columns, where, args, null, null, null, null);
        database.setTransactionSuccessful();
        database.endTransaction();

        if (cursor == null) {
            Log.d(TAG, "getVerseForReference: cursor = null, returning null");
            return null;
        }
        if (cursor.getCount() == 0) {
            Log.d(TAG, "getVerseForReference: got Zero rows, returning null");
            cursor.close();
            return null;
        }
        if (cursor.moveToFirst()) {
            String verseText = cursor.getString(
                    cursor.getColumnIndex(SimpleBibleTable.COLUMN_VERSE_TEXT));
            cursor.close();
            Log.d(TAG, "getVerseForReference() returning " + verseText.length() + " chars");
            return verseText;
        } else {
            cursor.close();
            Log.d(TAG, "getVerseForReference: did not moveToFirst, returning null");
            return null;
        }
    }

    public ArrayList<String> getAllVerseForChapter(int bookNumber, int chapterNumber) {
        Log.d(TAG, "getAllVerseForChapter() called with: bookNumber = [" + bookNumber
                   + "], chapterNumber = [" + chapterNumber + "]");

        String table = SimpleBibleTable.NAME;
        String[] columns = {SimpleBibleTable.COLUMN_VERSE_TEXT};
        String where = SimpleBibleTable.COLUMN_BOOK_NUMBER + " = ? AND " +
                       SimpleBibleTable.COLUMN_CHAPTER_NUMBER + " = ? ";
        String[] selectionArgs = {bookNumber + "", chapterNumber + ""};
        String orderBy = SimpleBibleTable.COLUMN_VERSE_NUMBER + " ASC";

        SQLiteDatabase database = getReadableDatabase();
        database.beginTransaction();
        Cursor cursor = database.query(
                true, table, columns, where, selectionArgs, null, null, orderBy, null);
        database.setTransactionSuccessful();
        database.endTransaction();

        if (cursor == null || cursor.getCount() < 1) {
            Log.d(TAG, "getAllVerseForChapter: cursor is null or no results found");
            return null;
        }

        int verseTextIdx = cursor.getColumnIndex(SimpleBibleTable.COLUMN_VERSE_TEXT);
        ArrayList<String> versesList = new ArrayList<>(0);

        while (cursor.moveToNext()) {
            versesList.add(cursor.getString(verseTextIdx));
        }
        Log.d(TAG, "getAllVerseForChapter() returned: " + versesList.size() + " records");
        cursor.close();
        return versesList;
    }

    @Override public ArrayList<String[]> searchForInput(@NonNull String input) {
        Log.d(TAG, "searchForInput() called with: input = [" + input + "]");
        if (input.isEmpty()) {
            Log.d(TAG, "searchForInput: passed input isEmpty, returning null");
            return null;
        }

        String table = SimpleBibleTable.NAME;
        String[] columns = {SimpleBibleTable.COLUMN_BOOK_NUMBER,
                            SimpleBibleTable.COLUMN_CHAPTER_NUMBER,
                            SimpleBibleTable.COLUMN_VERSE_NUMBER,
                            SimpleBibleTable.COLUMN_VERSE_TEXT};
        String where = "lower(" + SimpleBibleTable.COLUMN_VERSE_TEXT + ") like ?";
        String[] whereArgs = {"%" + input.toLowerCase() + "%"};

        /*String queryString = SQLiteQueryBuilder
                .buildQueryString(true, table, columns, where, null, null, null, null);
        Log.d(TAG, "searchForInput: queryString = " + queryString);*/

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(true, table, columns, where, whereArgs,
                                       null, null, null, null);
        if (cursor == null) {
            Log.d(TAG, "searchForInput: got a Null cursor, returning null");
            return null;
        }

        ArrayList<String[]> list = new ArrayList<>(0);
        if (cursor.getCount() < 1) {
            Log.d(TAG, "searchForInput: No results found, returning empty list");
            return list;
        }
        Log.d(TAG, "searchForInput: Cursor has " + cursor.getCount() + " rows");
        String reference, verseText;

        int bookNumberIdx = cursor.getColumnIndex(SimpleBibleTable.COLUMN_BOOK_NUMBER);
        int chapterNumberIdx = cursor.getColumnIndex(SimpleBibleTable.COLUMN_CHAPTER_NUMBER);
        int verseNumberIdx = cursor.getColumnIndex(SimpleBibleTable.COLUMN_VERSE_NUMBER);
        int verseTextIdx = cursor.getColumnIndex(SimpleBibleTable.COLUMN_VERSE_TEXT);

        while (cursor.moveToNext()) {
            reference = Utilities.prepareReferenceString(cursor.getInt(bookNumberIdx),
                                                         cursor.getInt(chapterNumberIdx),
                                                         cursor.getInt(verseNumberIdx));
            verseText = cursor.getString(verseTextIdx);
            list.add(new String[]{reference, verseText});
        }
        cursor.close();
        Log.d(TAG, "searchForInput() returned: " + list.size() + " results");
        return list;
    }
}
