package com.andrewchelladurai.simplebible.data;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbSetupJob
    extends JobIntentService {

  public static final int STARTED = 0;
  public static final int RUNNING = STARTED + 1;
  public static final int FAILED = RUNNING + 1;
  public static final int FINISHED = FAILED + 1;
  public static final String LINE_PROGRESS = "LINE_PROGRESS";
  private static final String TAG = "DbSetupJob";
  private static final int JOB_ID = 131416;
  private static ResultReceiver RESULT_RECEIVER;
  private int lineProgressValue = 0;

  public static void startWork(@NonNull Context context, @NonNull Intent work,
                               @NonNull final ResultReceiver resultReceiver) {
    RESULT_RECEIVER = resultReceiver;
    enqueueWork(context, DbSetupJob.class, JOB_ID, work);
  }

  @Override
  protected void onHandleWork(@NonNull final Intent intent) {
    Log.d(TAG, "onHandleWork");
    RESULT_RECEIVER.send(STARTED, Bundle.EMPTY);
    startForegroundNotification();
    // check if contents of books table is valid
    if (!validateBooksTable()) {
      // if not then populate the table
      if (!populateBooksTable()) {
        // if population of table fails, broadcast failure
        RESULT_RECEIVER.send(FAILED, Bundle.EMPTY);
        return;
      }
    }
    // check if contents of verses table is valid
    if (!validateVersesTable()) {
      // if not then populate the table
      if (!populateVersesTable()) {
        //if population of table fails, broadcast failure
        RESULT_RECEIVER.send(FAILED, Bundle.EMPTY);
      }
    }
    RESULT_RECEIVER.send(FINISHED, Bundle.EMPTY);
  }

  private void startForegroundNotification() {
    Log.d(TAG, "startForegroundNotification");
    final Intent intent = new Intent(this, DbSetupJob.class);
    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    startForeground(JOB_ID, new NotificationCompat.Builder(this, getPackageName())
        .setContentTitle(getString(R.string.dbSetupNotificationTitle))
        .setContentText(getString(R.string.dbSetupNotificationMessage))
        .setContentIntent(pendingIntent)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_logo)
        .setOnlyAlertOnce(true)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .build());
  }

  /**
   * Fill the verses table in the database by reading contents of an asset file.
   *
   * @return true if record is successfully created, false otherwise.
   */
  private boolean populateVersesTable() {
    final VerseDao verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
    final String fileName = VerseUtils.SETUP_FILE;
    final String separator = VerseUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = VerseUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;
    Log.d(TAG, "populateVersesTable: processing [" + fileName + "]");

    String[] parts;
    String translation;
    String text;
    int book;
    int chapter;
    int verse;
    try {
      final InputStream stream = getResources().getAssets().open(fileName);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line;
      while (null != (line = reader.readLine())) {
        if (!line.contains(separator)) {
          Log.e(TAG, "populateVersesTable: skipping [" + line + "], no [" + separator + "] found");
          continue;
        }
        parts = line.split(separator);
        if (parts.length != separatorCount + 1) {
          Log.e(TAG, "populateVersesTable: skipping [" + line + "] " + "it does't have ["
                     + separatorCount + "] [" + separator + "]");
          continue;
        }
        translation = parts[0];
        text = parts[4];
        try {
          book = Integer.parseInt(parts[1]);
          chapter = Integer.parseInt(parts[2]);
          verse = Integer.parseInt(parts[3]);
        } catch (NumberFormatException nfe) {
          Log.e(TAG, "populateVersesTable: part of line [" + line + "]  is NAN", nfe);
          continue;
        }
        if (translation.isEmpty()) {
          Log.e(TAG, "populateVersesTable: empty translation in line [" + line + "]");
          continue;
        }
        if (text.isEmpty()) {
          Log.e(TAG, "populateVersesTable: empty text in line [" + line + "]");
          continue;
        }
        if (book < 1 || book > 66) {
          Log.e(TAG, "populateVersesTable: invalid book number in line [" + line + "]");
          continue;
        }
        if (chapter < 1) {
          Log.e(TAG, "populateVersesTable: invalid chapters count in line [" + line + "]");
          continue;
        }
        if (verse < 1) {
          Log.e(TAG, "populateVersesTable: invalid  verses count in line [" + line + "]");
          continue;
        }
        // using the values, create a new verse in the database.
        verseDao.createVerse(new Verse(translation, book, chapter, verse, text));
        lineProgressValue = lineProgressValue + 1;
        final Bundle bundle = new Bundle();
        bundle.putInt(LINE_PROGRESS, lineProgressValue);
        RESULT_RECEIVER.send(RUNNING, bundle);
      }
    } catch (IOException e) {
      Log.e(TAG, "populateVersesTable: exception processing [" + fileName + "]", e);
      return false;
    }
    Log.d(TAG, "populateVersesTable: now verseCount[" + verseDao.getVerseCount()
               + "] && expectedCount[" + VerseUtils.EXPECTED_COUNT + "]");
    return true;
  }

  /**
   * Check the verses table to check if it's contents are valid.
   *
   * @return true if table meets expectations, false otherwise.
   */
  private boolean validateVersesTable() {
    final VerseDao verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
    final int expectedCount = VerseUtils.EXPECTED_COUNT;
    int count = verseDao.getVerseCount();

    if (count != expectedCount) {
      Log.e(TAG, "validateVersesTable: count[" + count
                 + "] != " + "expectedCount[" + expectedCount + "]");
      return false;
    }
    Log.d(TAG, "validateVersesTable: [" + expectedCount + "] verses exist");
    return true;
  }

  /**
   * Fill the books table in the database by reading contents of an asset file.
   *
   * @return true if a record is successfully created, false otherwise.
   */
  private boolean populateBooksTable() {
    final BookDao bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
    final String fileName = BookUtils.SETUP_FILE;
    final String separator = BookUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = BookUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;
    Log.d(TAG, "populateBooksTable: processing [" + fileName + "]");

    String[] parts;
    String testament;
    String description;
    String name;
    int position;
    int chapters;
    int verses;
    try {
      final InputStream stream = getResources().getAssets().open(fileName);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line;
      while (null != (line = reader.readLine())) {
        if (!line.contains(separator)) {
          Log.e(TAG, "populateBooksTable: skipping [" + line + "], no [" + separator + "] found");
          continue;
        }
        parts = line.split(separator);
        if (parts.length != separatorCount + 1) {
          Log.e(TAG, "populateBooksTable: skipping [" + line + "] " + "it does't have ["
                     + separatorCount + "] [" + separator + "]");
          continue;
        }
        testament = parts[0];
        description = parts[1];
        name = parts[3];
        try {
          position = Integer.parseInt(parts[2]);
          chapters = Integer.parseInt(parts[4]);
          verses = Integer.parseInt(parts[5]);
        } catch (NumberFormatException nfe) {
          Log.e(TAG, "populateBooksTable: part of line [" + line + "] is NAN", nfe);
          continue;
        }
        if (testament.isEmpty()) {
          Log.e(TAG, "populateBooksTable: empty testament in line [" + line + "]");
          continue;
        }
        if (description.isEmpty()) {
          Log.e(TAG, "populateBooksTable: empty description in line [" + line + "]");
          continue;
        }
        if (name.isEmpty()) {
          Log.e(TAG, "populateBooksTable: empty name in line [" + line + "]");
          continue;
        }
        if (position < 1 || position > 66) {
          Log.e(TAG, "populateBooksTable: invalid book position in line [" + line + "]");
          continue;
        }
        if (chapters < 1) {
          Log.e(TAG, "populateBooksTable: invalid chapters count in line [" + line + "]");
          continue;
        }
        if (verses < 1) {
          Log.e(TAG, "populateBooksTable: invalid  verses count in line [" + line + "]");
          continue;
        }
        // using the data values, create a new book record in the database
        bookDao.createBook(new Book(testament, description, position, name, chapters, verses));
        lineProgressValue = lineProgressValue + 1;
        final Bundle bundle = new Bundle();
        bundle.putInt(LINE_PROGRESS, lineProgressValue);
        RESULT_RECEIVER.send(RUNNING, bundle);
      }
    } catch (IOException e) {
      Log.e(TAG, "populateBooksTable: exception processing [" + fileName + "]", e);
      return false;
    }
    Log.d(TAG, "populateBooksTable: now bookCount[" + bookDao.getBookCount()
               + "] && expectedCount[" + BookUtils.EXPECTED_COUNT + "]");
    return true;
  }

  /**
   * Check the books table to check if it's contents are valid.
   *
   * @return true if table meets expectations, false otherwise
   */
  private boolean validateBooksTable() {
    final BookDao dao = SbDatabase.getDatabase(getApplication()).getBookDao();
    final int expectedCount = BookUtils.EXPECTED_COUNT;
    int count = dao.getBookCount();

    if (count != expectedCount) {
      Log.e(TAG, "validateBooksTable: bookCount[" + count
                 + "] != expectedCount[" + expectedCount + "]");
      return false;
    }
    Log.d(TAG, "validateBooksTable: [" + expectedCount + "] books exist");

    String expectedValue = getString(R.string.bookNameFirst);
    final Book firstBook = dao.getBookUsingPosition(1);
    if (firstBook != null && !firstBook.getName().equalsIgnoreCase(expectedValue)) {
      Log.e(TAG, "validateBooksTable: incorrect first book in db");
      return false;
    }
    Log.d(TAG, "validateBooksTable: book[" + 1 + "] name is [" + expectedValue + "]");

    expectedValue = getString(R.string.bookNameLast);
    final Book lastBook = dao.getBookUsingPosition(expectedCount);
    if (lastBook != null && !lastBook.getName().equalsIgnoreCase(expectedValue)) {
      Log.e(TAG, "validateBooksTable: incorrect last book in db");
      return false;
    }
    Log.d(TAG, "validateBooksTable: book[" + expectedCount + "] name is [" + expectedValue + "]");
    return true;
  }

}
