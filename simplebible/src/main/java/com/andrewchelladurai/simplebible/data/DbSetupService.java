package com.andrewchelladurai.simplebible.data;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.SimpleBibleScreen;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbSetupService
    extends IntentService {

  public static final String ACTION_SETUP_FAILURE
      = "com.andrewchelladurai.android.simplebible.db.DbSetupService.FAILURE";

  private static final String TAG = "DbSetupService";

  public DbSetupService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    startNotification();

    final BookDao bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
    // check if contents of books table is valid*/
    if (!validateBooksTable(bookDao)) {
      // if not then populate the table
      if (!populateBooksTable(bookDao)) {
        // if population of table fails, broadcast failure
        broadcastFailedCompletion();
        return;
      }
    }

    final VerseDao verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
    // check if contents of verses table is valid*/
    if (!validateVersesTable(verseDao)) {
      // if not then populate the table
      if (!populateVersesTable(verseDao)) {
        // if population of table fails, broadcast failure
        broadcastFailedCompletion();
      }
    }
  }

  /**
   * Fill the verses table in the database by reading contents of an asset file. @param utils used
   * to invoke the utility method of creating a verse record in the table. @return true if record is
   * successfully created, false otherwise.
   *
   * @param utils
   */
  private boolean populateVersesTable(@NonNull final VerseDao utils) {
    final String fileName = VerseUtils.SETUP_FILE;
    final String separator = VerseUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = VerseUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;

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

        utils.createRecord(new Verse(translation, book, chapter, verse, text));
      }
    } catch (IOException e) {
      Log.e(TAG, "populateVersesTable: exception processing [" + fileName + "]", e);
      return false;
    }

    Log.d(TAG, "populateVersesTable() returned true after processing [" + fileName + "]");
    return true;
  }

  /**
   * Check the verses table to check if it's contents are valid. @param verseDao used for invoking
   * utility methods @return true if table meets expectations, false otherwise
   *
   * @param verseDao
   */
  private boolean validateVersesTable(@NonNull VerseDao verseDao) {
    final int expectedCount = VerseUtils.EXPECTED_COUNT;
    if (verseDao.getRecordCount() != expectedCount) {
      Log.e(TAG, "validateVersesTable: incorrect number of verses in db");
      return false;
    }

    Log.d(TAG, "validateVersesTable: [" + expectedCount + "] verses exist");
    return true;
  }

  /**
   * Fill the books table in the database by reading contents of an asset file. @param bookDao used
   * to invoke the utility method of creating a book record in the table. @return true if a record
   * is successfully created, false otherwise.
   */
  private boolean populateBooksTable(@NonNull final BookDao bookDao) {
    final String fileName = BookUtils.SETUP_FILE;
    final String separator = BookUtils.SETUP_FILE_RECORD_SEPARATOR;
    final int separatorCount = BookUtils.SETUP_FILE_RECORD_SEPARATOR_COUNT;

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
          Log.e(TAG,
                "populateBooksTable: skipping [" + line + "] " + "it does't have [" + separatorCount
                + "] [" + separator + "]");
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

        bookDao.createRecord(new Book(testament, description, position, name, chapters, verses));
      }
    } catch (IOException e) {
      Log.e(TAG, "populateBooksTable: exception processing [" + fileName + "]", e);
      return false;
    }

    Log.d(TAG, "populateBooksTable() returned true after processing [" + fileName + "]");
    return true;
  }

  /**
   * Check the books table to check if it's contents are valid. @param utils used for invoking
   * utility methods @return true if table meets expectations, false otherwise
   */
  private boolean validateBooksTable(@NonNull final BookDao dao) {
    final int expectedCount = BookUtils.EXPECTED_COUNT;

    if (dao.getRecordCount() != expectedCount) {
      Log.e(TAG, "validateBooksTable: incorrect number of books in db");
      return false;
    }
    Log.d(TAG, "validateBooksTable: [" + expectedCount + "] books exist");

    String expectedValue = getString(R.string.first_book_name);
    if (!dao.getRecord(1).getName().equalsIgnoreCase(expectedValue)) {
      Log.e(TAG, "validateBooksTable: incorrect first book in db");
      return false;
    }
    Log.d(TAG, "validateBooksTable: book[" + 1 + "] name is [" + expectedValue + "]");

    expectedValue = getString(R.string.last_book_name);
    if (!dao.getRecord(expectedCount).getName().equalsIgnoreCase(expectedValue)) {
      Log.e(TAG, "validateBooksTable: incorrect last book in db");
      return false;
    }

    Log.d(TAG, "validateBooksTable: book[" + expectedCount + "] name is [" + expectedValue + "]");
    return true;
  }

  /**
   * Send a broadcast message informing that the db setup failed.
   */
  private void broadcastFailedCompletion() {
    Intent actionIntent = new Intent(ACTION_SETUP_FAILURE);
    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
    manager.sendBroadcastSync(actionIntent);
  }

  /**
   * Start a high priority notification.
   */
  private void startNotification() {
    Log.d(TAG, "startNotification: ");
    final Intent intent = new Intent(this, SimpleBibleScreen.class);
    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    startForeground(13, new NotificationCompat.Builder(this, getPackageName())
                            .setContentTitle(getString(R.string.db_init_notification_title))
                            .setContentText(getString(R.string.db_init_notification_message))
                            .setContentIntent(pendingIntent)
                            .setOngoing(true)
                            .setOnlyAlertOnce(true)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .build());
  }

}
