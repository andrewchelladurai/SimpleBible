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
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 23-Sep-2016 @ 1:00 AM
 */
public class DBUtility
        extends SQLiteOpenHelper {

    private static final String            TAG              = "SB_DBUtility";
    private static       DBUtility         thisInstance     = null;
    private static       InputStreamReader mBaseScript      = null;
    private static       InputStreamReader mUpgradeScript   = null;
    private static       InputStreamReader mDowngradeScript = null;

    public static DBUtility getInstance(Context context,
                                        InputStreamReader mainDbStream,
                                        InputStreamReader upgradeStream,
                                        InputStreamReader downgradeStream) {
        if (thisInstance == null) {
            thisInstance = new DBUtility(context);
            DBUtility.mBaseScript = mainDbStream;
            DBUtility.mUpgradeScript = upgradeStream;
            DBUtility.mDowngradeScript = downgradeStream;
            Log.d(TAG, "getInstance: Initialized Static Instance");
        }
        return thisInstance;
    }

    private DBUtility(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate() called");
        if (null != mBaseScript) {
            Log.d(TAG, "onCreate: null != mBaseScript");
            boolean successful = executeScriptFile(mBaseScript, db);
            if (successful) {
                Log.d(TAG, "onCreate: DB Created");
            } else {
                Log.d(TAG, "onCreate: DB NOT Created");
            }
        } else {
            Log.d(TAG, "onCreate: dbExists = true");
        }
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
                   + "isCreated = [" + isCreated + "] & isClosed = [" + isClosed + "]");
        return (isCreated & isClosed);
    }

    @Override public void onDowngrade(SQLiteDatabase db, int oldV, int newV) {
        Log.d(TAG, "onDowngrade() called with oldV = [" + oldV + "], newV = [" + newV + "]");
        boolean downgradeSuccessful = false;
        if (null != mDowngradeScript) {
            downgradeSuccessful = executeScriptFile(mDowngradeScript, db);
            Log.d(TAG, "onDowngrade: successful = " + downgradeSuccessful);
            if (downgradeSuccessful) {
                onCreate(db);
            }
        }
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        Log.d(TAG, "onUpgrade() called with: oldV = [" + oldV + "], newV = [" + newV + "]");
        boolean upgradeSuccessful = false;
        if (null != mUpgradeScript) {
            upgradeSuccessful = executeScriptFile(mUpgradeScript, db);
            Log.d(TAG, "onUpgrade: successful = " + upgradeSuccessful);
            if (upgradeSuccessful) {
                onCreate(db);
            }
        }
    }

    public void getVerseOfTheDay() {
        Log.d(TAG, "getVerseOfTheDay: called");

        String table = Constants.SimpleBibleTable.NAME;
        String[] columns = {Constants.SimpleBibleTable.COLUMN_BOOK_NUMBER,
                            Constants.SimpleBibleTable.COLUMN_CHAPTER_NUMBER,
                            Constants.SimpleBibleTable.COLUMN_VERSE_NUMBER,
                            Constants.SimpleBibleTable.COLUMN_VERSE_TEXT};

        String where = Constants.SimpleBibleTable.COLUMN_BOOK_NUMBER + " = ? AND " +
                       Constants.SimpleBibleTable.COLUMN_CHAPTER_NUMBER + " = ? AND " +
                       Constants.SimpleBibleTable.COLUMN_VERSE_NUMBER + " = ? ";
        String[] args = {"1", "1", "1"};

        String query = SQLiteQueryBuilder.buildQueryString(
                true, table, columns, where, null, null, null, null);
        Log.d(TAG, "getVerseOfTheDay: Query : " + query);

        SQLiteDatabase database = getReadableDatabase();
        database.beginTransaction();
        Cursor cursor = database.query(
                true, table, columns, where, args, null, null, null, null);
        database.setTransactionSuccessful();
        database.endTransaction();

        if (cursor == null) {
            Log.d(TAG, "getVerseOfTheDay: cursor = null");
        } else {
            Log.d(TAG, "getVerseOfTheDay: returned " + cursor.getCount() + " rows");
            cursor.close();
        }
    }
}
