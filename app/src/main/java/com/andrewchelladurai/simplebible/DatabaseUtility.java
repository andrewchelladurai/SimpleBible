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
 *
 */

package com.andrewchelladurai.simplebible;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 26-Feb-2016 @ 1:15 AM
 */
public class DatabaseUtility
        extends SQLiteOpenHelper {

    private static final String TAG = "SB_DatabaseUtility";

    private static final String DATABASE_NAME     = "Bible.db";
    private final static String BIBLE_TABLE       = "BIBLE_VERSES";
    private final static String DAILY_VERSE_TABLE = "DAILY_VERSE";
    private final static String BOOK_NUMBER       = "BOOK_NUMBER";
    private final static String CHAPTER_NUMBER    = "CHAPTER_NUMBER";
    private final static String VERSE_NUMBER      = "VERSE_NUMBER";
    private final static String VERSE_TEXT        = "VERSE_TEXT";

    private final static String BOOKMARK_TABLE      = "BOOK_MARKS";
    private final static String BM_TABLE_REFERENCES = "REFERENCE";
    private final static String BM_TABLE_ID         = "BM_ID";
    private final static String BM_TABLE_NOTES      = "NOTE";

    private static DatabaseUtility staticInstance = null;
    private static String         DB_PATH;
    private static SQLiteDatabase database;
    private static Context        context;

    private DatabaseUtility(Context context) {
        super(context, DATABASE_NAME, null, 1);
        DatabaseUtility.context = context;
        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getParent();
        Log.d(TAG, "DatabaseUtility() called DB_PATH = [" + DB_PATH + "]");
        openDataBase();
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

    public ArrayList<String> getAllVersesOfChapter(
            final int pBookNumber, final int pChapterNumber) {
        Log.d(TAG, "getAllVersesOfChapter() called BookNumber = [" + pBookNumber +
                   "], ChapterNumber = [" + pChapterNumber + "]");

        final SQLiteDatabase db = getReadableDatabase();

        String[] selectCols = {VERSE_NUMBER, CHAPTER_NUMBER, BOOK_NUMBER, VERSE_TEXT};
        String whereCondition = BOOK_NUMBER + " = ? AND " +
                                CHAPTER_NUMBER + " = ?";
        String[] conditionParams = {pBookNumber + "", pChapterNumber + ""};

        Cursor cursor = db.query(BIBLE_TABLE, selectCols, whereCondition,
                                 conditionParams, null, null, VERSE_NUMBER, null);

        ArrayList<String> list = new ArrayList<>(0);

        if (null != cursor && cursor.moveToFirst()) {
            int verseIndex = cursor.getColumnIndex(VERSE_TEXT);
            int verseIdIndex = cursor.getColumnIndex(VERSE_NUMBER);
            //            int chapterIdIndex = cursor.getColumnIndex("ChapterId");
            //            int bookIdIndex = cursor.getColumnIndex("BookId");
            do {
                list.add(cursor.getInt(verseIdIndex) + " - " + cursor.getString(verseIndex));
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.d(TAG, "getAllVersesOfChapter() returned " + list.size() + " results");
        return list;
    }

    public ArrayList<String> findText(String pInput) {
        Log.d(TAG, "findText() called  pInput = [" + pInput + "]");
        ArrayList<String> values = new ArrayList<>(0);
        final SQLiteDatabase db = getReadableDatabase();
        String[] selectCols = {VERSE_NUMBER, CHAPTER_NUMBER, BOOK_NUMBER, VERSE_TEXT};
        String whereCondition = VERSE_TEXT + " like ?";
        String[] conditionParams = {"%" + pInput + "%"};

        Cursor cursor = db.query(BIBLE_TABLE, selectCols, whereCondition, conditionParams,
                                 null, null, BOOK_NUMBER);

        if (cursor != null && cursor.moveToFirst()) {
            int verseNumberIndex = cursor.getColumnIndex(VERSE_NUMBER);
            int chapterIndex = cursor.getColumnIndex(CHAPTER_NUMBER);
            int bookIndex = cursor.getColumnIndex(BOOK_NUMBER);
            int bookValue, chapterValue, verseValue;
            StringBuilder entry = new StringBuilder();

            do {
                bookValue = cursor.getInt(bookIndex);
                chapterValue = cursor.getInt(chapterIndex);
                verseValue = cursor.getInt(verseNumberIndex);
                entry.append(bookValue)
                     .append(":")
                     .append(chapterValue)
                     .append(":")
                     .append(verseValue);
                values.add(entry.toString());
                entry.delete(0, entry.length());
            } while (cursor.moveToNext());
            cursor.close();
        }

        Log.d(TAG, "findText() returned: " + values.size());
        return values;
    }

    public String getVerseReferenceForToday() {
        String verseId = "43:3:16";
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        final SQLiteDatabase db = getReadableDatabase();
        String[] selectCols = {BOOK_NUMBER, CHAPTER_NUMBER, VERSE_NUMBER};
        String whereCondition = " rowid = ?";
        String[] conditionParams = {"" + dayOfYear};

/*
        String query = SQLiteQueryBuilder.buildQueryString(
                true, DAILY_VERSE_TABLE, selectCols, whereCondition, null, null, null, null);
        Log.d(TAG, "getVerseReferenceForToday: Query = " + query);
*/

        Cursor cursor = db.query(DAILY_VERSE_TABLE, selectCols, whereCondition, conditionParams,
                                 null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int verseNumberIndex = cursor.getColumnIndex(VERSE_NUMBER);
            int chapterIndex = cursor.getColumnIndex(CHAPTER_NUMBER);
            int bookIndex = cursor.getColumnIndex(BOOK_NUMBER);
            int bookValue, chapterValue, verseValue;
            StringBuilder entry = new StringBuilder();
            bookValue = cursor.getInt(bookIndex);
            chapterValue = cursor.getInt(chapterIndex);
            verseValue = cursor.getInt(verseNumberIndex);
            entry.append(bookValue)
                 .append(":")
                 .append(chapterValue)
                 .append(":")
                 .append(verseValue);
            verseId = entry.toString();
            entry.delete(0, entry.length());
            cursor.close();
        }
        Log.d(TAG, "getVerseReferenceForToday() returned: " + verseId + " for dayOfYear = " + dayOfYear);
        return verseId;
    }

    public String getSpecificVerse(int pBook, int pChapter, int pVerse) {
        String value = "";
        final SQLiteDatabase dbu = getReadableDatabase();

        String[] showColumns = {VERSE_TEXT};
        String whereCondition =
                BOOK_NUMBER + "=? AND " + CHAPTER_NUMBER + "=? AND " + VERSE_NUMBER + "=?";
        String[] conditionParams = {pBook + "", pChapter + "", pVerse + ""};
/*
        String query = SQLiteQueryBuilder.buildQueryString(
                true, BIBLE_TABLE, showColumns, whereCondition, null, null, null, null);
        Log.d(TAG, "getSpecificVerse: Query = " + query);
*/

        Cursor cursor = dbu.query(BIBLE_TABLE, showColumns, whereCondition, conditionParams,
                                  null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndex(VERSE_TEXT));
            cursor.close();
        }
        return value;
    }

    public String[] isReferencePresent(String references) {
        final SQLiteDatabase db = getReadableDatabase();
        String[] selectCols = {BM_TABLE_REFERENCES, BM_TABLE_NOTES};
        String where = BM_TABLE_REFERENCES + " like ?";
        String[] params = {"%" + references + "%"};
        Cursor cursor = db.query(true, BOOKMARK_TABLE, selectCols, where, params, null, null, null,
                                 null);

        String query = SQLiteQueryBuilder.buildQueryString(
                true, BOOKMARK_TABLE, selectCols, where, null, null, null, null);
        Log.d(TAG, "isReferencePresent: Query = " + query);

        if (null != cursor && cursor.moveToFirst()) {
            String results[] = new String[2];
            results[0] = cursor.getString(0);
            results[1] = cursor.getString(1);

            cursor.close();
            return results;
        }
        return null;
    }

    public boolean createNewBookmark(String references, String notes) {
        Log.d(TAG, "createNewBookmark() : [" + references + "], [" + notes + "]");
        boolean created;
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(BM_TABLE_REFERENCES, references);
        values.put(BM_TABLE_NOTES, notes);

        long rowcount = -1;
        try {
            rowcount = db.insert(BOOKMARK_TABLE, null, values);
            db.close();
            created = true;
        } catch (Exception e) {
            Log.wtf(TAG, "createNewBookmark: Bookmark Creation failed", e);
            created = false;
        }
        Log.d(TAG, "createNewBookmark() returned: " + created + " : " + rowcount + " saved");
        return created;
    }

    public boolean updateExistingBookmark(String references, String notes) {
        Log.d(TAG, "updateExistingBookmark() called [" + references + "], [" + notes + "]");
        boolean updated;
        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(BM_TABLE_REFERENCES, references);
        values.put(BM_TABLE_NOTES, notes);
        String whereClause = BM_TABLE_REFERENCES + " = ?";
        String whereParams[] = {references};
        int rowcount = -1;
        try {
            rowcount = db.update(BOOKMARK_TABLE, values, whereClause, whereParams);
            db.close();
            updated = true;
        } catch (Exception e) {
            updated = false;
            Log.wtf(TAG, "updateExistingBookmark: Bookmark update failed", e);
        }
        Log.d(TAG, "updateExistingBookmark() returned: " + updated + " : " + rowcount + " updated");
        return updated;
    }
}
