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
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

    private DatabaseUtility(Context context) {
        super(context, DATABASE_NAME, null, 1);
        DatabaseUtility.context = context;
        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getParent();
        Log.d(TAG, "DatabaseUtility: DB_PATH : " + DB_PATH);
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
        Log.d(TAG, "openDataBase: Called");
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
            this.getReadableDatabase();
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

        InputStream externalDbStream = context.getAssets().open(DATABASE_NAME);
        Log.i(TAG, "copyDataBase : externalDBStream" + externalDbStream.toString());
        String outFileName = DB_PATH + File.separatorChar + DATABASE_NAME;
        Log.i(TAG, "copyDataBase : outFileName = " + outFileName);

        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        localDbStream.close();
        externalDbStream.close();
        Log.d(TAG, "copyDataBase: Finished");
    }

    private Cursor getDBRecords(int bookID, int chapterID) {
        Log.d(TAG, "getDBRecords called with: bookID [" + bookID
                + "], chapterID [" + chapterID + "]");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("bibleverses",
                new String[]{"bookid", "chapterid", "verseid", "verse"},
                "chapterid =? AND bookid=?",
                new String[]{chapterID + "", bookID + ""},
                null, null, null);
        Log.d(TAG, "getDBRecords() Exited");
        return cursor;
    }

    public ArrayList<String> getAllVerseOfChapter(int bookNumber, int chapterNumber) {
        Log.d(TAG, "getAllVerseOfChapter() called with bookNumber = [" + bookNumber +
                "], chapterNumber = [" + chapterNumber + "]");
        Cursor cursor = getDBRecords(bookNumber, chapterNumber);
        ArrayList<String> list = new ArrayList<>(0);

        if (cursor.moveToFirst()) {
            int verseIndex = cursor.getColumnIndex("Verse");
            int verseIdIndex = cursor.getColumnIndex("VerseId");
            //            int chapterIdIndex = cursor.getColumnIndex("ChapterId");
            //            int bookIdIndex = cursor.getColumnIndex("BookId");
            do {
                list.add(cursor.getInt(verseIdIndex) + " - " + cursor.getString(verseIndex));
            } while (cursor.moveToNext());
            if (list.size() > 0) {
                cursor.close();
            }
        }
        return list;
    }

    private Cursor searchForTextCursor(String textToSearch) {
        Log.d(TAG, "getDBRecords() called with: textToSearch = [" + textToSearch + "]");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("bibleverses",
                new String[]{"bookid", "chapterid", "verseid", "verse"},
                "verse like ?",
                new String[]{"%" + textToSearch + "%"},
                null, null, null);
        Log.d(TAG, "getDBRecords() Exited");
        return cursor;
    }

    public ArrayList<String> searchForText(String textToSearch) {
        Log.d(TAG, "searchForText() called with: textToSearch = [" + textToSearch + "]");
        ArrayList<String> results = new ArrayList<>(0);

        if (textToSearch != null && textToSearch.length() > 0) {
            Cursor cursor = searchForTextCursor(textToSearch);
            if (cursor.moveToFirst()) {
                int verseIndex = cursor.getColumnIndex("Verse");
                int verseIdIndex = cursor.getColumnIndex("VerseId");
                int bookIdIndex = cursor.getColumnIndex("BookId");
                int chapterIdIndex = cursor.getColumnIndex("ChapterId");
//                AllBooks.Book book;
                BookNameContent.BookNameItem book;
                StringBuilder entry = new StringBuilder();
                do {
                    entry.delete(0, entry.length());
                    book = BookNameContent.getBookItem(bookIdIndex + 1);
                    if (book != null) {
                        entry.append(book.getName())
                                .append(" (")
                                .append(cursor.getInt(chapterIdIndex))
                                .append(":")
                                .append(cursor.getInt(verseIdIndex))
                                .append(") - ")
                                .append(cursor.getString(verseIndex));
                        results.add(entry.toString());
                    }
//                    book = AllBooks.getBook(cursor.getInt(bookIdIndex));
                } while (cursor.moveToNext());
                if (results.size() > 0) {
                    cursor.close();
                }
            }
        }
        return results;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

}
