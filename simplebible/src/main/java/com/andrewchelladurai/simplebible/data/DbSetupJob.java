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
import com.andrewchelladurai.simplebible.data.entity.EntityBook;
import com.andrewchelladurai.simplebible.data.entity.VerseEntity;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbSetupJob
    extends JobIntentService {

  public static final int NOT_STARTED = 0;
  public static final int STARTED = NOT_STARTED + 1;
  public static final int FAILED = STARTED + 1;
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

    final BookDao bookDao = SbDatabase.getDatabase(getApplication())
                                      .getBookDao();
    final VerseDao verseDao = SbDatabase.getDatabase(getApplication())
                                        .getVerseDao();

    final boolean isBookTableValid = validateTableBooks(bookDao);
    final boolean isVerseTableValid = validateTableVerses(verseDao);

    if (!isBookTableValid || !isVerseTableValid) {

      startForegroundNotification();

      // check if contents of books table is valid
      if (!isBookTableValid) {
        // if not then populate the table
        if (!populateTableBooks(bookDao)) {
          // if population of table fails, broadcast failure
          Log.e(TAG, "onHandleWork: populateTableBooks() failed");
          RESULT_RECEIVER.send(FAILED, Bundle.EMPTY);
          return;
        }
      }

      // check if contents of verses table is valid
      if (!isVerseTableValid) {
        // if not then populate the table
        if (!populateTableVerses(verseDao)) {
          //if population of table fails, broadcast failure
          Log.e(TAG, "onHandleWork: populateTableVerses() failed");
          RESULT_RECEIVER.send(FAILED, Bundle.EMPTY);
        }
      }
    }

    RESULT_RECEIVER.send(FINISHED, Bundle.EMPTY);
  }

  private void startForegroundNotification() {
    Log.d(TAG, "startForegroundNotification");
    final Intent intent = new Intent(this, DbSetupJob.class);
    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    startForeground(JOB_ID, new NotificationCompat.Builder(this, getPackageName())
                                .setContentTitle(getString(R.string.db_setup_notify_title))
                                .setContentText(getString(R.string.db_setup_notify_message))
                                .setContentIntent(pendingIntent)
                                .setOngoing(true)
                                .setSmallIcon(R.drawable.ic_logo)
                                .setOnlyAlertOnce(true)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .build());
  }

  /**
   * Check the books table to check if it's contents are valid.
   *
   * @param bookDao
   *
   * @return true if table meets expectations, false otherwise
   */
  private boolean validateTableBooks(@NonNull final BookDao bookDao) {
    Log.d(TAG, "validateTableBooks:");
    final int expectedCount = BookUtils.EXPECTED_COUNT;
    int count = bookDao.getBookCount();

    if (count != expectedCount) {
      Log.e(TAG, "validateTableBooks: count[" + count
                 + "] != expectedCount[" + expectedCount + "], returning false");
      return false;
    }
    Log.d(TAG, "validateTableBooks: expected count of books [" + expectedCount + "] exist");

    final String[] expectedValue = new String[]{getString(R.string.db_setup_1st_book_name)};
    final EntityBook firstBook = bookDao.getBookUsingPosition(1);
    if (firstBook != null && !firstBook.getName()
                                       .equalsIgnoreCase(expectedValue[0])) {
      Log.e(TAG, "validateTableBooks: first book's name didn't match, returning false");
      return false;
    }
    Log.d(TAG, "validateTableBooks: first book's name matches [" + expectedValue[0] + "]");

    expectedValue[0] = getString(R.string.db_setup_nth_book_name);
    final EntityBook lastBook = bookDao.getBookUsingPosition(expectedCount);
    if (lastBook != null && !lastBook.getName()
                                     .equalsIgnoreCase(expectedValue[0])) {
      Log.e(TAG, "validateTableBooks: last book's name didn't match, returning false");
      return false;
    }
    Log.d(TAG, "validateTableBooks: last book's name matches [" + expectedValue[0] + "]");
    return true;
  }

  /**
   * Check the verses table to check if it's contents are valid.
   *
   * @param verseDao
   *
   * @return true if table meets expectations, false otherwise.
   */
  private boolean validateTableVerses(@NonNull final VerseDao verseDao) {
    Log.d(TAG, "validateTableVerses:");

    final int expectedCount = VerseUtils.EXPECTED_COUNT;
    int count = verseDao.getVerseCount();

    if (count != expectedCount) {
      Log.e(TAG, "validateTableVerses: count[" + count
                 + "] != expectedCount[" + expectedCount + "], returning false");
      return false;
    }
    Log.d(TAG, "validateTableVerses: expected count of verses [" + expectedCount + "] exist");

    return true;
  }

  /**
   * Fill the books table in the database by reading contents of an asset file.
   *
   * @param bookDao
   *
   * @return true if a record is successfully created, false otherwise.
   */
  private boolean populateTableBooks(@NonNull final BookDao bookDao) {
    Log.d(TAG, "populateTableBooks:");

    final String fileName = BookUtils.SETUP_FILE;
    final String separator = BookUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = BookUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;
    Log.d(TAG, "populateTableBooks: processing [" + fileName + "]");

    String[] parts;
    String testament;
    String description;
    String name;
    int position;
    int chapters;
    int verses;
    try {
      final InputStream stream = getResources().getAssets()
                                               .open(fileName);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line;
      while (null != (line = reader.readLine())) {
        if (!line.contains(separator)) {
          Log.e(TAG, "populateTableBooks: skipping [" + line + "], no [" + separator + "] found");
          continue;
        }
        parts = line.split(separator);
        if (parts.length != separatorCount + 1) {
          Log.e(TAG, "populateTableBooks: skipping [" + line + "] " + "it does't have ["
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
          Log.e(TAG, "populateTableBooks: part of line [" + line + "] is NAN", nfe);
          continue;
        }
        if (testament.isEmpty()) {
          Log.e(TAG, "populateTableBooks: empty testament in line [" + line + "]");
          continue;
        }
        if (description.isEmpty()) {
          Log.e(TAG, "populateTableBooks: empty description in line [" + line + "]");
          continue;
        }
        if (name.isEmpty()) {
          Log.e(TAG, "populateTableBooks: empty name in line [" + line + "]");
          continue;
        }
        if (position < 1 || position > 66) {
          Log.e(TAG, "populateTableBooks: invalid book position in line [" + line + "]");
          continue;
        }
        if (chapters < 1) {
          Log.e(TAG, "populateTableBooks: invalid chapters count in line [" + line + "]");
          continue;
        }
        if (verses < 1) {
          Log.e(TAG, "populateTableBooks: invalid  verses count in line [" + line + "]");
          continue;
        }
        // using the data values, create a new book record in the database
        bookDao
            .createBook(new EntityBook(testament, description, position, name, chapters, verses));
        lineProgressValue = lineProgressValue + 1;
        final Bundle bundle = new Bundle();
        bundle.putInt(LINE_PROGRESS, lineProgressValue);
        RESULT_RECEIVER.send(STARTED, bundle);
      }
    } catch (IOException e) {
      Log.e(TAG, "populateTableBooks: exception processing [" + fileName + "]", e);
      return false;
    }
    Log.d(TAG, "populateTableBooks: now currentBookCount[" + bookDao.getBookCount()
               + "] == expectedCount[" + BookUtils.EXPECTED_COUNT + "]");
    return true;
  }

  /**
   * Fill the verses table in the database by reading contents of an asset file.
   *
   * @return true if record is successfully created, false otherwise.
   */
  private boolean populateTableVerses(@NonNull VerseDao verseDao) {
    Log.d(TAG, "populateTableVerses:");

    final String fileName = VerseUtils.SETUP_FILE;
    final String separator = VerseUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = VerseUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;
    Log.d(TAG, "populateTableVerses: processing [" + fileName + "]");

    String[] parts;
    String translation;
    String text;
    int book;
    int chapter;
    int verse;
    try {
      final InputStream stream = getResources().getAssets()
                                               .open(fileName);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line;
      while (null != (line = reader.readLine())) {
        if (!line.contains(separator)) {
          Log.e(TAG, "populateTableVerses: skipping [" + line + "], no [" + separator + "] found");
          continue;
        }
        parts = line.split(separator);
        if (parts.length != separatorCount + 1) {
          Log.e(TAG, "populateTableVerses: skipping [" + line + "] " + "it does't have ["
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
          Log.e(TAG, "populateTableVerses: part of line [" + line + "]  is NAN", nfe);
          continue;
        }
        if (translation.isEmpty()) {
          Log.e(TAG, "populateTableVerses: empty translation in line [" + line + "]");
          continue;
        }
        if (text.isEmpty()) {
          Log.e(TAG, "populateTableVerses: empty text in line [" + line + "]");
          continue;
        }
        if (book < 1 || book > 66) {
          Log.e(TAG, "populateTableVerses: invalid book number in line [" + line + "]");
          continue;
        }
        if (chapter < 1) {
          Log.e(TAG, "populateTableVerses: invalid chapters count in line [" + line + "]");
          continue;
        }
        if (verse < 1) {
          Log.e(TAG, "populateTableVerses: invalid  verses count in line [" + line + "]");
          continue;
        }
        // using the values, create a new verse in the database.
        verseDao.createVerse(new VerseEntity(translation, book, chapter, verse, text));
        lineProgressValue = lineProgressValue + 1;
        final Bundle bundle = new Bundle();
        bundle.putInt(LINE_PROGRESS, lineProgressValue);
        RESULT_RECEIVER.send(STARTED, bundle);
      }
    } catch (IOException e) {
      Log.e(TAG, "populateTableVerses: exception processing [" + fileName + "]", e);
      return false;
    }
    Log.d(TAG, "populateTableVerses: now currentVerseCount[" + verseDao.getVerseCount()
               + "] == expectedCount[" + VerseUtils.EXPECTED_COUNT + "]");
    return true;
  }

}
