package com.andrewchelladurai.simplebible.ui.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.andrewchelladurai.simplebible.common.Utilities.EXPECTED_BOOK_COUNT;
import static com.andrewchelladurai.simplebible.common.Utilities.EXPECTED_VERSE_COUNT;
import static com.andrewchelladurai.simplebible.common.Utilities.FILE_BOOK_DETAILS;
import static com.andrewchelladurai.simplebible.common.Utilities.FILE_VERSE_DETAILS;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 07-Aug-2018 @ 1:15 AM.
 */
class LoaderActivityPresenter {

    private static final String SPLIT_REGEX = "~";
    private static final String TAG         = "LoaderActivityPresenter";

    private static final LoaderActivityPresenter ourInstance = new LoaderActivityPresenter();

    private LoaderActivityPresenter() {
    }

    public static LoaderActivityPresenter getInstance() {
        return ourInstance;
    }

    public static class DbAsyncLoader
        extends AsyncTaskLoader<Boolean> {

        DbAsyncLoader(@NonNull final Context context) {
            super(context);
        }

        private static void populateInitialData(final @NonNull SbDatabase sbDatabase,
                                                final @NonNull Context context,
                                                final @NonNull String fileName) throws IOException {
            final VerseDao verseDao = sbDatabase.getVerseDao();
            final BookDao bookDao = sbDatabase.getBookDao();
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

        @Nullable
        @Override
        public Boolean loadInBackground() {
            try {
                final Context context = getContext();
                final SbDatabase sbDatabase = SbDatabase.getInstance(context);
                final BookDao bookDao = sbDatabase.getBookDao();
                int count = bookDao.getRecordCount();
                Log.d(TAG, "getInstance: [" + count + "] books exist");
                if (count != EXPECTED_BOOK_COUNT) { // number of books / lines in the file
                    Log.e(TAG, "getInstance: THERE MUST BE 66 BOOKS");
                    bookDao.deleteAllRecords();
                    Log.d(TAG, "getInstance: truncated Books table");
                    populateInitialData(sbDatabase, context, FILE_BOOK_DETAILS);
                    count = bookDao.getRecordCount();
                    Log.d(TAG, "getInstance: now there are [" + count + "] books");
                }

                final VerseDao verseDao = sbDatabase.getVerseDao();
                count = verseDao.getRecordCount();
                Log.d(TAG, "getInstance: [" + count + "] verses exist");
                if (count != EXPECTED_VERSE_COUNT) {
                    Log.e(TAG, "getInstance: THERE MUST BE 31098 verses");
                    verseDao.deleteAllRecords();
                    Log.d(TAG, "getInstance: truncated Verses table");
                    populateInitialData(sbDatabase, context, FILE_VERSE_DETAILS);
                    count = verseDao.getRecordCount();
                    Log.d(TAG, "getInstance: now there are [" + count + "] verses");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
