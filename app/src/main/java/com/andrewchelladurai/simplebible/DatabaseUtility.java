/*
 * This file 'DatabaseUtility.java' is part of SimpleBible :
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
 */

package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 26-Feb-2016 @ 1:15 AM
 */
public class DatabaseUtility
        extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseUtility";

    private static final String DATABASE_NAME = "Bible_NIV.db";
    private static DatabaseUtility staticInstance = null;
    private static String DB_PATH;
    private static SQLiteDatabase database;
    private static Context context;
    private final String BIBLE_NIV_TABLE = "bibleverses";
    private final String BIBLE_COLUMNS[] = {"bookid", "chapterid", "verseid", "verse"};
    private final String BIBLE_COLUMN_BOOK_ID = "bookid";
    private final String BIBLE_COLUMN_CHAPTER_ID = "chapterid";
    private final String BIBLE_COLUMN_VERSE_ID = "verseid";
    private final String BIBLE_COLUMN_VERSE_TEXT = "Verse";

    private DatabaseUtility(Context context) {
        super(context, DATABASE_NAME, null, 1);
        DatabaseUtility.context = context;
        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getParent();
        Log.d(TAG, "DatabaseUtility() called DB_PATH = [" + DB_PATH + "]");
        openDataBase();
    }

    public static DatabaseUtility getInstance(Context context)
            throws NullPointerException {
        if (staticInstance == null) {
            if (context == null) {
                throw new NullPointerException("NULL Context passed for instantiating DB");
            }
            staticInstance = new DatabaseUtility(context);
        }
        return staticInstance;
    }

    private void openDataBase()
            throws SQLException {
        Log.d(TAG, "openDataBase: Entered");
        if (database == null) {
            createDataBase();
            String path = DB_PATH + File.separatorChar + DATABASE_NAME;
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
    }

    private void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            Log.d(TAG, "createDataBase: DB Does not Exist");
            getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.d(TAG, "createDataBase: Exception Copying Bible.db");
                throw new Error("Error copying Bible.db!");
            }
        } else {
            Log.d(TAG, "createDataBase: Database already exists");
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        String path = DB_PATH + File.separatorChar + DATABASE_NAME;
        Log.d(TAG, "checkDataBase: at path" + path);
        File f = new File(path);
        if (f.exists()) {
            try {
                checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException sqle) {
                Log.d(TAG, "checkDataBase: " + sqle.getLocalizedMessage());
                sqle.printStackTrace();
            } finally {
                //Android does not like resource leaks, everything should be closed
                if (checkDb != null) {
                    checkDb.close();
                }
            }
            Log.d(TAG, "checkDataBase: Exited returning checkDb != null");
            return checkDb != null;
        } else {
            return false;
        }
    }

    private void copyDataBase()
            throws IOException {
        Log.d(TAG, "copyDataBase: Called");

        InputStream assetDatabase = context.getAssets().open(DATABASE_NAME);
        Log.i(TAG, "copyDataBase : externalDBStream" + assetDatabase.toString());
        String outFileName = DB_PATH + File.separatorChar + DATABASE_NAME;
        Log.i(TAG, "copyDataBase : outFileName = " + outFileName);

        OutputStream localDatabase = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = assetDatabase.read(buffer)) > 0) {
            localDatabase.write(buffer, 0, bytesRead);
        }
        localDatabase.close();
        assetDatabase.close();
        Log.d(TAG, "copyDataBase: Finished");
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

/*    public String getSpecificVerse(int bookNumber, int chapterNumber, int verseNumber) {
        String verseText;
        Log.d(TAG, "getSpecificVerse() called with: bookNumber = [" + bookNumber +
                "], chapterNumber = [" + chapterNumber + "], verseNumber = [" + verseNumber + "]");

        if (bookNumber < 1 || verseNumber < 1 || chapterNumber < 1) {
            Log.d(TAG, "getSpecificVerse: One of the parameters is < 1, returning null");
            return null;
        }

        SQLiteDatabase db = getReadableDatabase();

        String[] selectColumns = {BIBLE_COLUMN_VERSE_TEXT};
        String whereCondition = BIBLE_COLUMN_BOOK_ID + "=? AND " +
                BIBLE_COLUMN_CHAPTER_ID + "=? AND " +
                BIBLE_COLUMN_VERSE_ID + "=?";
        String[] conditionParameters = {bookNumber + "", chapterNumber + "", verseNumber + ""};

        String sql = "query " + BIBLE_NIV_TABLE + " to show " + BIBLE_COLUMN_VERSE_TEXT +
                "where " + BIBLE_COLUMN_BOOK_ID + "=" + bookNumber + " AND " +
                BIBLE_COLUMN_CHAPTER_ID + "=" + chapterNumber + " AND " +
                BIBLE_COLUMN_VERSE_ID + "=" + verseNumber;
        Log.i(TAG, "getSpecificVerse: sql = " + sql);

        Cursor cursor = db.query(BIBLE_NIV_TABLE, selectColumns, whereCondition, conditionParameters,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(BIBLE_COLUMN_VERSE_TEXT);
            String[] cols = cursor.getColumnNames();
            for (String c : cols) {
                Log.d(TAG, "\ngetSpecificVerse: Column : " + c);
            }
            Log.d(TAG, "getSpecificVerse: index = "+index);
            verseText = cursor.getString(index);
        } else {
            Log.d(TAG, "getSpecificVerse: No record found, returning empty");
            verseText = "";
        }
        cursor.close();
        Log.d(TAG, "getSpecificVerse: returning : " + verseText);
        return verseText;
    }*/
}
