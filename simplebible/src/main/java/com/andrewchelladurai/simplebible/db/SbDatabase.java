package com.andrewchelladurai.simplebible.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 04-Aug-2018 @ 9:26 PM.
 */

@Database(entities = {Verse.class, Book.class, Bookmark.class},
          // epoch time in seconds : date +%s
          version = 1520969806, // March 13, 2018 7:36:46 PM
          exportSchema = false)
public abstract class SbDatabase
    extends RoomDatabase {

    private static final String     TAG                  = "SbDatabase";
    private static final String     SPLIT_REGEX          = "~";
    private static final String     FILE_BOOK_DETAILS    = "mainStepsBooks.csv";
    private static final String     FILE_VERSE_DETAILS   = "mainStepsVerses.csv";
    private static final String     DATABASE_NAME        = "SimpleBible.db";
    private static final int        EXPECTED_BOOK_COUNT  = 66; // lines in the file
    private static final int        EXPECTED_VERSE_COUNT = 31098; // lines in the file
    private static       SbDatabase thisInstance         = null;

    public static SbDatabase getInstance(@NonNull final Context context) {
        synchronized (SbDatabase.class) {
            if (thisInstance == null) {
                thisInstance = Room.databaseBuilder(context.getApplicationContext(),
                                                    SbDatabase.class,
                                                    DATABASE_NAME)
                                   .fallbackToDestructiveMigration().build();
                Log.d(TAG, "getInstance: Database created");

                try {
                    final BookDao bookDao = getInstance(context).getBookDao();
                    int count = bookDao.getRecordCount();
                    Log.d(TAG, "getInstance: [" + count + "] books exist");
                    if (count != EXPECTED_BOOK_COUNT) { // number of books / lines in the file
                        Log.e(TAG, "getInstance: THERE MUST BE 66 BOOKS");
                        bookDao.deleteAllRecords();
                        Log.d(TAG, "getInstance: truncated Books table");
                        populateInitialData(context, FILE_BOOK_DETAILS);
                        count = bookDao.getRecordCount();
                        Log.d(TAG, "getInstance: now there are [" + count + "] books");
                    }

                    final VerseDao verseDao = getInstance(context).getVerseDao();
                    count = verseDao.getRecordCount();
                    Log.d(TAG, "getInstance: [" + count + "] verses exist");
                    if (count != EXPECTED_VERSE_COUNT) {
                        Log.e(TAG, "getInstance: THERE MUST BE 31098 verses");
                        verseDao.deleteAllRecords();
                        Log.d(TAG, "getInstance: truncated Verses table");
                        populateInitialData(context, FILE_VERSE_DETAILS);
                        count = verseDao.getRecordCount();
                        Log.d(TAG, "getInstance: now there are [" + count + "] verses");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return thisInstance;
    }

    private static void populateInitialData(final @NonNull Context context,
                                            final @NonNull String fileName) throws IOException {
        final VerseDao verseDao = getInstance(context).getVerseDao();
        final BookDao bookDao = getInstance(context).getBookDao();
        InputStreamReader script = getFileHandle(context, fileName);
        Log.d(TAG, "populateInitialData: beginning execution of " + fileName);

        BufferedReader reader = null;
        boolean isCreated;
        boolean isClosed = true;
        try {
            reader = new BufferedReader(script);
            String line;
            while (null != (line = reader.readLine())) {
                if (!line.isEmpty() && line.contains(SPLIT_REGEX)) { // valid line to process
                    switch (fileName) {
                        case FILE_VERSE_DETAILS:
                            createNewVerseRecord(verseDao, line);
                            break;
                        case FILE_BOOK_DETAILS:
                            createNewBookRecord(bookDao, line);
                            break;
                        default:
                            throw new UnsupportedOperationException(
                                "populateInitialData: unknown filename : " + fileName);
                    }
                }
            }
            isCreated = true;
            Log.d(TAG, "populateInitialData: finished executing " + fileName);
        } catch (IOException ioe) {
            isCreated = false;
            Log.e(TAG, "populateInitialData: " + ioe.getLocalizedMessage(), ioe);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                    isClosed = true;
                    Log.d(TAG, "populateInitialData: closed handle to " + fileName);
                } catch (IOException e) {
                    isClosed = false;
                    Log.e(TAG, "populateInitialData: " + e.getLocalizedMessage(), e);
                }
            }
        }
        String msg = (isCreated & isClosed) ? "Successfully executed " + fileName
                                            : "Failed executing " + fileName;
        Log.d(TAG, "populateInitialData: " + msg);
    }

    private static InputStreamReader getFileHandle(@NonNull final Context context,
                                                   @NonNull final String fileName)
        throws IOException {
        try {
            return new InputStreamReader(context.getAssets().open(fileName));
        } catch (IOException ioe) {
            Log.e(TAG, "getFileHandle: Error opening/accessing file [" + fileName + "]", ioe);
            throw ioe;
        }
    }

    private static void createNewBookRecord(@NonNull final BookDao bookDao,
                                            @NonNull final String line) {
        final String[] parts = line.split(SPLIT_REGEX);
        final String desc = parts[0];
        final int number = Integer.valueOf(parts[1]);
        final String name = parts[2];
        final int chapters = Integer.valueOf(parts[3]);
        final int verses = Integer.valueOf(parts[4]);
        bookDao.createNewBook(new Book(desc, number, name, chapters, verses));
    }

    private static void createNewVerseRecord(@NonNull final VerseDao verseDao,
                                             @NonNull final String line) {
        final String[] parts = line.split(SPLIT_REGEX);
        final String translation = parts[0];
        final int book = Integer.valueOf(parts[1]);
        final int chapter = Integer.valueOf(parts[2]);
        final int verse = Integer.valueOf(parts[3]);
        final String text = parts[4];
        verseDao.createNewVerse(new Verse(translation, book, chapter, verse, text));
    }

    public abstract VerseDao getVerseDao();

    public abstract BookDao getBookDao();

    public abstract BookmarkDao getBookmarkDao();

}
