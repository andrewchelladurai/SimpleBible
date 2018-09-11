package com.andrewchelladurai.simplebible.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.SplashScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 09-Aug-2018 @ 8:44 PM.
 */
public class SplashScreenPresenter {

    private static final String TAG = "SplashScreenPresenter";
    private static SplashScreenOps mOps;

    public SplashScreenPresenter(final SplashScreenOps ops) {
        mOps = ops;
    }

    /**
     * Created by : Andrew Chelladurai
     * Email : TheUnknownAndrew[at]GMail[dot]com
     * on : 09-Aug-2018 @ 9:37 PM.
     */
    public static class DbSetupAsyncTask
        extends AsyncTaskLoader<Boolean> {

        private static final String FILE_VERSE_DETAILS   = "mainStepsVerses.csv";
        private static final String FILE_BOOK_DETAILS    = "mainStepsBooks.csv";
        private static final String SPLIT_REGEX          = "~";
        @SuppressWarnings("FieldCanBeLocal")
        private static final int    EXPECTED_BOOK_COUNT  = 66;
        @SuppressWarnings("FieldCanBeLocal")
        private static final int    EXPECTED_VERSE_COUNT = 31098;

        public DbSetupAsyncTask() {
            super(mOps.getSystemContext());
        }

        @Override
        public Boolean loadInBackground() {
            try {
                final Context context = mOps.getSystemContext();
                final SbDatabase sbDatabase = SbDatabase.getInstance(context);
                final BookDao bookDao = sbDatabase.getBookDao();
                int count = bookDao.getNumberOfBooks();
                Log.d(TAG, "getInstance: [" + count + "] books exist");
                if (count != EXPECTED_BOOK_COUNT) {
                    Log.e(TAG, "getInstance: THERE MUST BE 66 BOOKS");
                    bookDao.deleteAllRecords();
                    Log.d(TAG, "getInstance: truncated Books table");
                    populateInitialData(sbDatabase, context, FILE_BOOK_DETAILS);
                    count = bookDao.getNumberOfBooks();
                    Log.d(TAG, "getInstance: now there are [" + count + "] books");
                }

                final VerseDao verseDao = sbDatabase.getVerseDao();
                count = verseDao.getNumberOfRecords();
                Log.d(TAG, "getInstance: [" + count + "] verses exist");
                if (count != EXPECTED_VERSE_COUNT) {
                    Log.e(TAG, "getInstance: THERE MUST BE 31098 verses");
                    verseDao.deleteAllRecords();
                    Log.d(TAG, "getInstance: truncated Verses table");
                    populateInitialData(sbDatabase, context, FILE_VERSE_DETAILS);
                    count = verseDao.getNumberOfRecords();
                    Log.d(TAG, "getInstance: now there are [" + count + "] verses");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        private void createNewBookRecord(@NonNull final BookDao bookDao,
                                         @NonNull final String line) {
            final String[] parts = line.split(SPLIT_REGEX);
            final String desc = parts[0];
            final int number = Integer.valueOf(parts[1]);
            final String name = parts[2];
            final int chapters = Integer.valueOf(parts[3]);
            final int verses = Integer.valueOf(parts[4]);
            bookDao.createBook(new Book(desc, number, name, chapters, verses));
        }

        private void createNewVerseRecord(@NonNull final VerseDao verseDao,
                                          @NonNull final String line) {
            final String[] parts = line.split(SPLIT_REGEX);
            final String translation = parts[0];
            final int book = Integer.valueOf(parts[1]);
            final int chapter = Integer.valueOf(parts[2]);
            final int verse = Integer.valueOf(parts[3]);
            final String text = parts[4];
            verseDao.createRecord(new Verse(translation, book, chapter, verse, text));
        }

        private void populateInitialData(final @NonNull SbDatabase sbDatabase,
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
                    // is this a valid line to process
                    if (!line.isEmpty() && line.contains(SPLIT_REGEX)) {
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

        private InputStreamReader getFileHandle(@NonNull final Context context,
                                                @NonNull final String fileName)
            throws IOException {
            try {
                return new InputStreamReader(context.getAssets().open(fileName));
            } catch (IOException ioe) {
                Log.e(TAG, "getFileHandle: Error opening/accessing file [" + fileName + "]", ioe);
                throw ioe;
            }
        }
    }

    public static class GetVerseForTodayTask
        extends AsyncTask<Void, Void, Verse> {

        private final String   mDefaultReference;
        private final String[] mVerseArray;

        public GetVerseForTodayTask(@NonNull final String defaultReference, String[] verseArray) {
            mDefaultReference = defaultReference;
            mVerseArray = verseArray;
        }

        @Override
        protected Verse doInBackground(final Void... voids) {
            int dayOfTheYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            Log.d(TAG, "doInBackground: dayOfTheYear [" + dayOfTheYear + "]");

            String reference = (dayOfTheYear > mVerseArray.length) ? mDefaultReference
                                                                   : mVerseArray[dayOfTheYear];

            Utilities utilities = Utilities.getInstance();
            int[] versePart = (utilities.isValidReference(reference))
                              ? utilities.splitReference(reference)
                              : utilities.splitReference(mDefaultReference);

            List<Verse> verseList = SbDatabase.getInstance(mOps.getSystemContext())
                                              .getVerseDao()
                                              .readRecord(versePart[0], versePart[1], versePart[2]);
            if (verseList == null || verseList.isEmpty()) {
                Log.e(TAG, "doInBackground: no verses found for reference [" + reference + "]");
                return null;
            }
            return verseList.get(0);
        }

        @Override
        protected void onPostExecute(final Verse verse) {
            mOps.displayVerseForToday(verse);
        }
    }

}
