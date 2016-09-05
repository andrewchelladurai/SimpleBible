/*
 *
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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 26-Feb-2016 @ 1:15 AM
 */
public class DatabaseUtility
        extends SQLiteOpenHelper {

    private static final String TAG = "SB_DatabaseUtility";
    /*
        private static final int[] VERSIONS = {1, 2};
        private static final int CURRENT_VERSION = VERSIONS[VERSIONS.length - 1];
    */
    private static final int CURRENT_VERSION = 1;

    private static final String DATABASE_NAME = "Bible.db";
    private final static String BIBLE_TABLE = "BIBLE_VERSES";
    private final static String DAILY_VERSE_TABLE = "DAILY_VERSE";
    private final static String BOOK_NUMBER = "BOOK_NUMBER";
    private final static String CHAPTER_NUMBER = "CHAPTER_NUMBER";
    private final static String VERSE_NUMBER = "VERSE_NUMBER";
    private final static String VERSE_TEXT = "VERSE_TEXT";

    private final static String BOOKMARK_TABLE = "BOOK_MARKS";
    private final static String BM_TABLE_REFERENCES = "REFERENCE";
    private final static String BM_TABLE_NOTES = "NOTE";

    private static DatabaseUtility staticInstance = null;
    private static String DB_PATH;
    private static Context context;

    private DatabaseUtility(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_VERSION);
        DatabaseUtility.context = context;
        //Write a full path to the databases of your application
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getParent();
        Utilities.log(TAG, "DatabaseUtility() called DB_PATH = [" + DB_PATH + "]");
        openDataBase();
    }

    private void openDataBase()
            throws SQLException {
        Utilities.log(TAG, "openDataBase: Entered");
        createDataBase();
/*
        SQLiteDatabase database = getWritableDatabase();
        int currentVersion = database.getVersion();
        upgradeDatabase(database, currentVersion);
*/
    }

    private void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            Utilities.log(TAG, "createDataBase: DB Does not Exist");
            getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.wtf(TAG, "createDataBase: createDataBase: Exception Copying Bible.db", e);
            }
        } else {
            Utilities.log(TAG, "createDataBase: Database already exists");
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        String path = DB_PATH + File.separatorChar + DATABASE_NAME;
        Utilities.log(TAG, "checkDataBase: at path" + path);
        File f = new File(path);
        if (f.exists()) {
            try {
                checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLException sqle) {
                Utilities.log(TAG, "checkDataBase: " + sqle.getLocalizedMessage());
                sqle.printStackTrace();
            } finally {
                if (checkDb != null) {
                    checkDb.close();
                }
            }
            Utilities.log(TAG, "checkDataBase: Exited returning checkDb != null");
            return checkDb != null;
        } else {
            return false;
        }
    }

    private void copyDataBase()
            throws IOException {
        Utilities.log(TAG, "copyDataBase: Called");

        InputStream assetDatabase = context.getAssets().open(DATABASE_NAME);
        Utilities.log(TAG, "copyDataBase : Steam obtained from  Source Asset");
        String outFileName = DB_PATH + File.separatorChar + DATABASE_NAME;
        Utilities.log(TAG, "copyDataBase : outFileName = " + outFileName);

        OutputStream localDatabase = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = assetDatabase.read(buffer)) > 0) {
            localDatabase.write(buffer, 0, bytesRead);
        }
        localDatabase.close();
        assetDatabase.close();
        Utilities.log(TAG, "copyDataBase: Finished");
    }

    public static DatabaseUtility getInstance(Context context)
            throws NullPointerException {
        if (staticInstance == null) {
            if (context == null) {
                Log.wtf(TAG, "createInstance:",
                        new NullPointerException("NULL Context passed for instantiating DB"));
            }
            staticInstance = new DatabaseUtility(context);
        }
        return staticInstance;
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        openDataBase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
//        upgradeDatabase(sqLiteDatabase, oldV);
    }

    private void upgradeDatabase(SQLiteDatabase db, int oldV) {
        Utilities.log(TAG, "Upgrading database from version " + oldV + " to " + CURRENT_VERSION);
        switch (oldV) {
            case 0:
            case 1:
                // Creation should ideally be handled in the Constructor
                // Which is copying the Asset DB to Local Storage

                // Now when moving to Version 1 => 2:
                // Remove the Daily_Verse table (Made it a Resource String Array)
                db.delete(DAILY_VERSE_TABLE, null, null);
                db.execSQL("DROP TABLE IF EXISTS " + DAILY_VERSE_TABLE);
                db.close();
                Utilities.log(TAG, DAILY_VERSE_TABLE + " deleted");
        }
        Log.d(TAG, "upgradeDatabase() returned");
    }

    public ArrayList<String> getAllVersesOfChapter(final int pBookNumber, final int
            pChapterNumber) {
        Utilities.log(TAG, "getAllVersesOfChapter() called BookNumber[" + pBookNumber +
                           "], ChapterNumber [" + pChapterNumber + "]");

        final SQLiteDatabase db = getReadableDatabase();

        String[] selectCols = {VERSE_NUMBER, CHAPTER_NUMBER, BOOK_NUMBER, VERSE_TEXT};
        String whereCondition = BOOK_NUMBER + " = ? AND " + CHAPTER_NUMBER + " = ?";
        String[] conditionParams = {pBookNumber + "", pChapterNumber + ""};

        Cursor cursor = db
                .query(BIBLE_TABLE, selectCols, whereCondition, conditionParams, null, null,
                       VERSE_NUMBER, null);

        ArrayList<String> list = new ArrayList<>(0);

        if (null != cursor && cursor.moveToFirst()) {
            int verseIndex = cursor.getColumnIndex(VERSE_TEXT);
            //            int verseIdIndex = cursor.getColumnIndex(VERSE_NUMBER);
            //            int chapterIdIndex = cursor.getColumnIndex("ChapterId");
            //            int bookIdIndex = cursor.getColumnIndex("BookId");
            do {
                list.add(cursor.getString(verseIndex));
                // list.add(cursor.getInt(verseIdIndex) + " - " + cursor.getString(verseIndex));
            } while (cursor.moveToNext());
            cursor.close();
        }
        Utilities.log(TAG, "getAllVersesOfChapter() returned " + list.size() + " results");
        return list;
    }

    public ArrayList<String> searchText(String pInput) {
        Utilities.log(TAG, "searchText() : using [" + pInput + "]");
        ArrayList<String> values = new ArrayList<>(0);
        final SQLiteDatabase db = getReadableDatabase();
        String[] selectCols = {VERSE_NUMBER, CHAPTER_NUMBER, BOOK_NUMBER, VERSE_TEXT};
        String whereCondition = VERSE_TEXT + " like ?";
        String[] conditionParams = {"%" + pInput + "%"};

        Cursor cursor = db
                .query(BIBLE_TABLE, selectCols, whereCondition, conditionParams, null, null,
                       BOOK_NUMBER);

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
                entry.append(bookValue).append(":").append(chapterValue).append(":")
                     .append(verseValue);
                values.add(entry.toString());
                entry.delete(0, entry.length());
            } while (cursor.moveToNext());
            cursor.close();
        }

        Utilities.log(TAG, "searchText() returned " + values.size());
        return values;
    }

    public String getVerseReferenceForToday() {
        String verseId;
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        String verseListArray[] = Utilities.getStringArray(R.array.daily_verse_list);
        if (null != verseListArray && verseListArray.length >= dayOfYear) {
            verseId = verseListArray[dayOfYear];
        } else {
            verseId = "43:3:16"; // John 3:16
            Utilities.log(TAG, "Using default reference for Daily_Verse");
        }

        Utilities.log(TAG,
                      "getVerseReferenceForToday() returned: " + verseId + " : dayOfYear " +
                      dayOfYear);
        return verseId;
    }

    public String getSpecificVerse(int pBook, int pChapter, int pVerse) {
        String value = "";
        final SQLiteDatabase dbu = getReadableDatabase();

        String[] showColumns = {VERSE_TEXT};
        String where = BOOK_NUMBER + "=? AND " + CHAPTER_NUMBER + "=? AND " + VERSE_NUMBER + "=?";
        String[] params = {pBook + "", pChapter + "", pVerse + ""};

/*
        String query = SQLiteQueryBuilder
                .buildQueryString(true, BIBLE_TABLE, showColumns, where, null, null, null, null);
        Utilities.log(TAG, "getSpecificVerse [" + query + "]:[" + pBook + ", " + pChapter + ", " +
                           pVerse + "]");
*/

        Cursor cursor = dbu.query(BIBLE_TABLE, showColumns, where, params, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndex(VERSE_TEXT));
            cursor.close();
        }
        return value;
    }

    public boolean isAlreadyBookmarked(String references) {
        final SQLiteDatabase db = getReadableDatabase();
        String[] selectCols = {"COUNT(" + BM_TABLE_REFERENCES + ")"};
        String where = BM_TABLE_REFERENCES + " = ?";
        String params[] = {references};
        Cursor cursor = db
                .query(true, BOOKMARK_TABLE, selectCols, where, params, null, null, null, null);

        String query = SQLiteQueryBuilder
                .buildQueryString(true, BOOKMARK_TABLE, selectCols, where, null, null, null, null);
        Utilities.log(TAG, "isAlreadyBookmarked [" + query + "]:[" + params[0] + "]");

        String result = "0"; // Zero to indicate no reference exists.
        if (null != cursor && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        Utilities.log(TAG, "isAlreadyBookmarked() returned: " + result);
        return Integer.parseInt(result) != 0;
    }

    public boolean createNewBookmark(String references, String notes) {
        Utilities.log(TAG,
                      "createNewBookmark() references [" + references + "], notes [" + notes + "]");
        boolean created;
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(BM_TABLE_REFERENCES, references);
        values.put(BM_TABLE_NOTES, notes);

        long rowCount = db.insert(BOOKMARK_TABLE, "", values);
        db.close();
        created = (rowCount > 0);
        Utilities.log(TAG, "createNewBookmark returned " + created + " : " + rowCount + " saved");
        return created;
    }

    public boolean updateExistingBookmark(String reference, String notes) {
        Utilities.log(TAG, "updateExistingBookmark() called [" + reference + "], [" + notes + "]");
        boolean updated;
        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(BM_TABLE_NOTES, notes);
        String whereClause = BM_TABLE_REFERENCES + " = ?";
        String whereParams[] = {reference};
        int rowCount = db.update(BOOKMARK_TABLE, values, whereClause, whereParams);
        db.close();
        updated = (rowCount > 0);
        Utilities.log(TAG, "updateExistingBookmark() " + updated + " : " + rowCount + " updated");
        return updated;
    }

    public CopyOnWriteArrayList<String[]> getAllBookmarkedEntries() {
        CopyOnWriteArrayList<String[]> results = new CopyOnWriteArrayList<>();
        final SQLiteDatabase db = getReadableDatabase();
        String selectCols[] = {BM_TABLE_REFERENCES, BM_TABLE_NOTES};

        String query = SQLiteQueryBuilder
                .buildQueryString(true, BOOKMARK_TABLE, selectCols, null, null, null, null, null);
        Utilities.log(TAG, "getAllBookmarkedEntries [" + query + "]");

        Cursor cursor = db
                .query(true, BOOKMARK_TABLE, selectCols, null, null, null, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            do {
                results.add(new String[]{cursor.getString(0), cursor.getString(1)});
            } while (cursor.moveToNext());
            cursor.close();
        }
        Utilities.log(TAG, "getAllBookmarkedEntries() returned " + results.size() + " entries");
        return results;
    }

    public String getBookmarkedEntry(String reference) {
        Utilities
                .log(TAG, "getBookmarkedEntry() called with: " + "reference = [" + reference + "]");
        String note = "";
        final SQLiteDatabase db = getReadableDatabase();
        String[] selectCols = {BM_TABLE_NOTES};
        String where = BM_TABLE_REFERENCES + " = ?";
        String[] params = {reference};
        Cursor cursor = db
                .query(true, BOOKMARK_TABLE, selectCols, where, params, null, null, null, null);

        String query = SQLiteQueryBuilder
                .buildQueryString(true, BOOKMARK_TABLE, selectCols, where, null, null, null, null);
        Utilities.log(TAG, "getBookmarkedEntry [" + query + "]:[" + params[0] + "]");

        if (null != cursor && cursor.moveToFirst()) {
            note = cursor.getString(0);
            cursor.close();
        }
        return note;
    }

    public boolean deleteBookmark(String reference) {
        final SQLiteDatabase db = getWritableDatabase();
        String where = BM_TABLE_REFERENCES + " = ?";
        String[] condition = {reference};
        int rowsDeleted = db.delete(BOOKMARK_TABLE, where, condition);
        return (rowsDeleted > 0);
    }
}
