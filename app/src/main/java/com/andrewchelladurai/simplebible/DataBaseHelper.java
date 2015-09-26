/*
 * This file 'DataBaseHelper.java' is part of SimpleBible :  An Android Bible application
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

public class DataBaseHelper
        extends SQLiteOpenHelper {

    public static  String         DB_PATH;
    public static  String         DB_NAME;
    private static SQLiteDatabase database;
    public final   Context        context;
    private final String CLASS_NAME = "DataBaseHelper";

    public DataBaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        Log.i(CLASS_NAME, "Entering Constructor");
        this.context = context;

        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(databaseName).getParent();
        DB_NAME = databaseName;
        Log.d("DB_PATH", DB_PATH);
        Log.d("DB_NAME", DB_NAME);
        openDataBase();
        Log.i(CLASS_NAME, "Exiting Constructor");
    }

    //This piece of code will create a database if it’s not yet created
    private void createDataBase() {
        Log.i(CLASS_NAME, "Entering createDataBase");
        boolean dbExist = checkDataBase();
        Log.i(CLASS_NAME, "Inside createDataBase - dbExist = " + dbExist);
        if (!dbExist) {
            Log.i(CLASS_NAME, "Inside createDataBase - IF");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(CLASS_NAME, "Database already exists");
        }
        Log.i(CLASS_NAME, "Exiting createDataBase");
    }

    //Performing a database existence check
    private boolean checkDataBase() {
        Log.i(CLASS_NAME, "Entering checkDataBase");
        SQLiteDatabase checkDb = null;
        String path = DB_PATH + File.separatorChar + DB_NAME;

        File f = new File(path);
        Log.d(CLASS_NAME, "checkDataBase : checkDataBase_path = " + path);
        if (f.exists()) {
            try {
                checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException e) {
                Log.e(CLASS_NAME, "checkDataBase : Error while checking db");
            } finally {
                //Android does not like resource leaks, everything should be closed
                if (checkDb != null) {
                    checkDb.close();
                }
            }
            Log.i(CLASS_NAME, "Exiting checkDataBase");
            return checkDb != null;
        } else {
            Log.i(CLASS_NAME, "Exiting checkDataBase");
            return false;
        }

    }

    //Method for copying the database
    private void copyDataBase()
            throws IOException {
        Log.i(CLASS_NAME, "Entering CopyDatabase");
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);
        Log.i(CLASS_NAME, "copyDataBase : externalDbStream" + externalDbStream.toString());
        //Path to the created empty database on your Android device
        String outFileName = DB_PATH + File.separatorChar + DB_NAME;
        Log.i(CLASS_NAME, "copyDataBase : outFileName = " + outFileName);

        //Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
        Log.i(CLASS_NAME, "Exiting CopyDatabase");
    }

    public SQLiteDatabase openDataBase()
            throws SQLException {
        Log.i(CLASS_NAME, "Entering openDataBase");
        String path = DB_PATH + File.separatorChar + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        Log.i(CLASS_NAME, "Exiting openDataBase");
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
        Log.i(CLASS_NAME, "Entering onCreate");
        Log.i(CLASS_NAME, "Exiting onCreate");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int i1) {
        Log.i(CLASS_NAME, "Entering onUpgrade");
        Log.i(CLASS_NAME, "Exiting onUpgrade");
    }

    public Cursor getDBRecords(int bookID, int chapterID) {
        Log.i(CLASS_NAME, "Entering getDBRecords : " + bookID + " : " + chapterID);
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("bibleverses",
                                 new String[]{"bookid", "chapterid", "verseid", "verse"},
                                 "chapterid =? AND bookid=?",
                                 new String[]{chapterID + "", bookID + ""},
                                 null, null, null);
        Log.i(CLASS_NAME, "Exiting getDBRecords");
        return cursor;
    }

    protected Cursor getDBRecords(String paramTextToSearch) {
        Log.i(CLASS_NAME, "Entering getDBRecords Searching for " + paramTextToSearch);
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("bibleverses",
                                 new String[]{"bookid", "chapterid", "verseid", "verse"},
                                 "verse like ?",
                                 new String[]{"%" + paramTextToSearch + "%"},
                                 null, null, null);
        Log.i(CLASS_NAME, "Exiting getDBRecords");
        return cursor;
    }
}
