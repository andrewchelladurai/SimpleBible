/*
 *
 * This file 'HomeScreenPresenter.java' is part of SimpleBible :
 *
 * Copyright (c) 2018.
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

package com.andrewchelladurai.simplebible.presenter;

import android.content.Context;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.data.BookDao;
import com.andrewchelladurai.simplebible.data.Bookmark;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.Verse;
import com.andrewchelladurai.simplebible.data.VerseDao;
import com.andrewchelladurai.simplebible.ops.HomeScreenOps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import static com.andrewchelladurai.simplebible.data.Book.EXPECTED_BOOK_COUNT;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 16-Sep-2018 @ 6:42 PM
 */
public class HomeScreenPresenter {

    private static HomeScreenOps mOps;
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "HomeScreenPresenter";

    public HomeScreenPresenter(final HomeScreenOps ops) {
        mOps = ops;
    }

    @NonNull
    public String getDailyVerseReference(@NonNull final String[] referenceArray,
                                         @NonNull final String defaultReference) {
        if (referenceArray == null || referenceArray.length < 1) {
            Log.e(TAG, "getDailyVerseReference: empty or null referenceArray");
            return defaultReference;
        }

        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        final String reference = referenceArray[dayOfYear];
        if (!Verse.validateReference(reference)) {
            Log.e(TAG, "getDailyVerseReference: invalid reference at location [" + dayOfYear + "]");
            return defaultReference;
        }

        Log.d(TAG, "getDailyVerseReference() returned: [" + reference + "] for dayOfYear "
                   + "[" + dayOfYear + "]");
        return reference;
    }

    public static class DbInitLoader
        extends AsyncTaskLoader<Boolean> {

        private static final String TAG = "DbInitLoader";

        private final String FILE_VERSE_DETAILS = "init_WEB_Translation.txt";
        private final String FILE_BOOK_DETAILS = "init_Stats.txt";
        private final String SPLIT_REGEX = Bookmark.SEPARATOR;
        @SuppressWarnings("FieldCanBeLocal")
        private final int EXPECTED_VERSE_COUNT = 31098;

        public DbInitLoader() {
            super(mOps.getSystemContext());
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            mOps.startLoadingScreen();
        }

        @Override
        public Boolean loadInBackground() {
            try {
                final Context context = getContext();
                final SbDatabase sbDatabase = SbDatabase.getInstance(context);
                final BookDao bookDao = sbDatabase.getBookDao();
                int count = bookDao.getNumberOfBooks();
                Log.d(TAG, " [" + count + "] books exist");
                if (count != EXPECTED_BOOK_COUNT) {
                    Log.e(TAG, " EXPECTED_BOOK_COUNT is [" + EXPECTED_BOOK_COUNT + "]");
                    bookDao.deleteAllRecords();
                    Log.d(TAG, " truncated Books table");
                    populateInitialData(sbDatabase, context, FILE_BOOK_DETAILS);
                    count = bookDao.getNumberOfBooks();
                    Log.d(TAG, " now there are [" + count + "] books");
                }

                final VerseDao verseDao = sbDatabase.getVerseDao();
                count = verseDao.getNumberOfRecords();
                Log.d(TAG, " [" + count + "] verses exist");
                if (count != EXPECTED_VERSE_COUNT) {
                    Log.e(TAG, " EXPECTED_VERSE_COUNT is [" + EXPECTED_VERSE_COUNT + "]");
                    verseDao.deleteAllRecords();
                    Log.d(TAG, " truncated Verses table");
                    populateInitialData(sbDatabase, context, FILE_VERSE_DETAILS);
                    count = verseDao.getNumberOfRecords();
                    Log.d(TAG, " now there are [" + count + "] verses");
                }
            } catch (IOException ioe) {
                Log.e(TAG, "database creation failed: retuning false", ioe);
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
            verseDao.createVerse(new Verse(translation, book, chapter, verse, text));
        }

        private void populateInitialData(final @NonNull SbDatabase sbDatabase,
                                         final @NonNull Context context,
                                         final @NonNull String fileName)
        throws IOException {
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
                Log.d(TAG, "populateInitialData: finished " + fileName);
            } catch (IOException ioe) {
                isCreated = false;
                Log.e(TAG, "populateInitialData: failure", ioe);
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                        isClosed = true;
                    } catch (IOException e) {
                        isClosed = false;
                        Log.e(TAG, "populateInitialData: failure", e);
                    }
                }
            }
            String msg = (isCreated & isClosed) ? "Processed " + fileName
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
}
