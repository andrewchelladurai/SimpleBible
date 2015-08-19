/*
 *
 * This file is part of SimpleBible : A Holy Bible Application on the
 * Android Mobile platform with easy navigation and offline access.
 *
 * Copyright (c) 2015.
 * Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
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

    public DataBaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.context = context;

        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(databaseName).getParent();
        DB_NAME = databaseName;
        Log.d("DB_PATH", DB_PATH);
        Log.d("DB_NAME", DB_NAME);
        openDataBase();
    }

    //This piece of code will create a database if it’s not yet created
    private void createDataBase() {
        boolean dbExist = checkDataBase();
        Log.i("INFO = ", "Inside createDataBase - dbExist = " + dbExist);
        if (!dbExist) {
            Log.i("INFO = ", "Inside createDataBase - IF");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
    }

    //Performing a database existence check
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        String         path    = DB_PATH + File.separatorChar + DB_NAME;

        File f = new File(path);
        Log.d("checkDataBase_path = ", path);
        if (f.exists()) {
            try {
                checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException e) {
                Log.e(this.getClass().toString(), "Error while checking db");
            } finally {
                //Android does not like resource leaks, everything should be closed
                if (checkDb != null) {
                    checkDb.close();
                }
            }
            return checkDb != null;
        } else {
            return false;
        }

    }

    //Method for copying the database
    private void copyDataBase() throws IOException {
        Log.i("INFO = ", "Inside CopyDatabase");
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);
        Log.i("INFO = ", "externalDbStream" + externalDbStream.toString());
        //Path to the created empty database on your Android device
        String outFileName = DB_PATH + File.separatorChar + DB_NAME;
        Log.i("INFO = ", "outFileName = " + outFileName);

        //Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int    bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
        Log.i("INFO = ", "Exiting CopyDatabase");
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        Log.i("INFO = ", "Entering openDataBase()");
        String path = DB_PATH + File.separatorChar + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        Log.i("INFO = ", "Exiting openDataBase()");
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
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int i1) {
    }

    public Cursor getDBRecords(int bookID, int chapterID) {
        Log.i("getDBRecords()", "book : chapter = " + bookID + " : " + chapterID);
        SQLiteDatabase db = getReadableDatabase();

        return db.query("bibleverses", new String[]{"bookid", "chapterid", "verseid", "verse"},
                "chapterid =? AND bookid=?", new String[]{chapterID + "", bookID + ""}, null, null, null);
    }

    protected Cursor getDBRecords(String paramTextToSearch) {
        Log.i("getDBRecords()", "Searching for " + paramTextToSearch);
        SQLiteDatabase db = getReadableDatabase();

        return db.query("bibleverses", new String[]{"bookid", "chapterid", "verseid", "verse"},
                "verse like ?", new String[]{"%" + paramTextToSearch + "%"}, null, null, null);
    }
}
