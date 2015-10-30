/*
 * This file 'HelperDatabase.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Copyright (c) Andrew Chelladurai - 2015.
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

package com.andrewchelladurai.simplebible.utilities;

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

public class HelperDatabase
        extends SQLiteOpenHelper {

    private static String DB_PATH;
    private static String DB_NAME;
    private static SQLiteDatabase database;
    private final Context context;
    private final String TAG = "HelperDatabase";

    public HelperDatabase(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        Log.d(TAG, "HelperDatabase() called with: context = [" + context +
                "], databaseName = [" + databaseName + "]");
        this.context = context;

        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(databaseName).getParent();
        DB_NAME = databaseName;
        Log.d("DB_PATH", DB_PATH);
        Log.d("DB_NAME", DB_NAME);
        openDataBase();
        Log.d(TAG, "HelperDatabase() Exited");
    }

    //This piece of code will create a database if it’s not yet created
    private void createDataBase() {
        Log.d(TAG, "createDataBase() Entered");
        boolean dbExist = checkDataBase();
        Log.i(TAG, "dbExist = " + dbExist);
        if (!dbExist) {
            Log.i(TAG, "Inside createDataBase - IF");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, "DB copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(TAG, "Database already exists");
        }
        Log.d(TAG, "createDataBase() Exited");
    }

    //Performing a database existence check
    private boolean checkDataBase() {
        Log.d(TAG, "checkDataBase() Entered");
        SQLiteDatabase checkDb = null;
        String path = DB_PATH + File.separatorChar + DB_NAME;

        File f = new File(path);
        Log.d(TAG, "checkDataBase_path = " + path);
        if (f.exists()) {
            try {
                checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException e) {
                Log.e(TAG, "Error when checking DB");
            } finally {
                //Android does not like resource leaks, everything should be closed
                if (checkDb != null) {
                    checkDb.close();
                }
            }
            Log.d(TAG, "checkDataBase() Exited returning checkDb != null");
            return checkDb != null;
        } else {
            Log.d(TAG, "checkDataBase() Exited returning false");
            return false;
        }

    }

    //Method for copying the database
    private void copyDataBase()
            throws IOException {
        Log.d(TAG, "copyDataBase() Entered");

        InputStream externalDbStream = context.getAssets().open(DB_NAME);
        Log.i(TAG, "copyDataBase : externalDbStream" + externalDbStream.toString());
        String outFileName = DB_PATH + File.separatorChar + DB_NAME;
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
        Log.d(TAG, "copyDataBase() Exited");
    }

    public SQLiteDatabase openDataBase()
            throws SQLException {
        Log.d(TAG, "openDataBase() Entered");
        String path = DB_PATH + File.separatorChar + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        Log.d(TAG, "openDataBase() Exited");
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Entering onCreate");
        Log.i(TAG, "Exiting onCreate");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int i1) {
        Log.i(TAG, "Entering onUpgrade");
        Log.i(TAG, "Exiting onUpgrade");
    }

    public Cursor getDBRecords(int bookID, int chapterID) {
        Log.d(TAG, "getDBRecords() called with: bookID = [" + bookID +
                "], chapterID = [" + chapterID + "]");
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("bibleverses",
                new String[]{"bookid", "chapterid", "verseid", "verse"},
                "chapterid =? AND bookid=?",
                new String[]{chapterID + "", bookID + ""},
                null, null, null);
        Log.d(TAG, "getDBRecords() Exited");
        return cursor;
    }

    public Cursor getDBRecords(String paramTextToSearch) {
        Log.d(TAG, "getDBRecords() called with: paramTextToSearch = ["
                + paramTextToSearch + "]");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("bibleverses",
                new String[]{"bookid", "chapterid", "verseid", "verse"},
                "verse like ?",
                new String[]{"%" + paramTextToSearch + "%"},
                null, null, null);
        Log.d(TAG, "getDBRecords() Exited");
        return cursor;
    }
}
