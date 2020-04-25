package com.andrewchelladurai.simplebible.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DbSetupWorker
    extends Worker {

  private static final String TAG = "DbSetupWorker";

  private final SbDao dao;

  public DbSetupWorker(@NonNull final Context context,
                       @NonNull final WorkerParameters workerParams) {
    super(context, workerParams);
    dao = SbDatabase.getDatabase(getApplicationContext()).getDao();
  }

  @NonNull
  @Override
  public Result doWork() {
    return (populateTableBooks() && populateTableVerses())
           ? Result.success()
           : Result.failure();
  }

  private boolean populateTableBooks() {
    Log.d(TAG, "populateTableBooks:");

    final Resources res = getApplicationContext().getResources();

    String[] parts;
    String line;
    int number;
    int chapters;
    int verses;

    final String fileName = res.getString(R.string.db_setup_asset_file_table_books);
    final String separator = res.getString(R.string.db_setup_asset_file_table_books_separator);

    Log.d(TAG, "populateTableBooks: asset file [" + fileName
               + "] separated by [" + separator + "]");

    try {
      final BufferedReader read = new BufferedReader(
          new InputStreamReader(getApplicationContext().getAssets().open(fileName)));

      final String newT = res.getString(R.string.new_testament);
      final String oldT = res.getString(R.string.old_testament);

      while ((line = read.readLine()) != null) {
        parts = line.split(separator);

        // we are expecting 5 parts in each line [description~number~name~chapters~verses]
        if (parts.length != 5) {
          Log.d(TAG, "populateTableBooks: : [" + line + "] does not have 5 parts");
          continue;
        }

        // check that we do nt have empty values in description & name
        if (parts[0].isEmpty() || parts[2].isEmpty()) {
          Log.e(TAG, "populateTableBooks: empty description or name in [" + line + "]");
          continue;
        }

        // validate book number
        try {
          number = Integer.parseInt(parts[1]);
          if (number < 1 || number > Utils.MAX_BOOKS) {
            Log.e(TAG, "populateTableBooks: book number invalid in [" + line + "]");
            continue;
          }
        } catch (NumberFormatException e) {
          Log.e(TAG, "populateTableBooks: book number invalid in [" + line + "]", e);
          continue;
        }

        // validate chapters count
        try {
          chapters = Integer.parseInt(parts[3]);
          if (chapters < 1) {
            Log.e(TAG, "populateTableBooks: invalid chapters count in [" + line + "]");
            continue;
          }
        } catch (NumberFormatException e) {
          Log.e(TAG, "populateTableBooks: invalid chapters count in [" + line + "]", e);
          continue;
        }

        // validate verses count
        try {
          verses = Integer.parseInt(parts[4]);
          if (verses < 1) {
            Log.e(TAG, "populateTableBooks: invalid verses count in [" + line + "]");
            continue;
          }
        } catch (NumberFormatException e) {
          Log.e(TAG, "populateTableBooks: invalid verses count in [" + line + "]", e);
          continue;
        }

        dao.createBook(new EntityBook(
            parts[0], number, parts[2], chapters, verses, (number > 39) ? newT : oldT));

      }
    } catch (IOException ioe) {
      Log.e(TAG, "populateTableBooks: failed to open asset file [" + fileName + "]", ioe);
      return false;
    }

    return true;
  }

  private boolean populateTableVerses() {
    Log.d(TAG, "populateTableVerses:");

    final Resources res = getApplicationContext().getResources();

    String[] parts;
    String line;
    int number;
    int chapters;
    int verses;

    final String fileName = res.getString(R.string.db_setup_asset_file_table_verses);
    final String separator = res.getString(R.string.db_setup_asset_file_table_verses_separator);

    Log.d(TAG, "populateTableVerses: asset file [" + fileName
               + "] separated by [" + separator + "]");

    try {
      final BufferedReader read = new BufferedReader(
          new InputStreamReader(getApplicationContext().getAssets().open(fileName)));

      while ((line = read.readLine()) != null) {
        parts = line.split(separator);

        // we are expecting 5 parts in each line [translation~number~chapter~verse~text]
        if (parts.length != 5) {
          Log.e(TAG, "populateTableVerses: line does not have 5 values [" + line + "]");
          continue;
        }

        if (parts[0].isEmpty() || parts[4].isEmpty()) {
          Log.e(TAG, "populateTableVerses: translation or verse is empty in [" + line + "]");
          continue;
        }

        // validate book number
        try {
          number = Integer.parseInt(parts[1]);
          if (number < 1 || number > Utils.MAX_BOOKS) {
            Log.e(TAG, "populateTableVerses: book number invalid in [" + line + "]");
            continue;
          }
        } catch (NumberFormatException e) {
          Log.e(TAG, "populateTableVerses: book number invalid in [" + line + "]", e);
          continue;
        }

        // validate chapter
        try {
          chapters = Integer.parseInt(parts[2]);
          if (chapters < 1) {
            Log.e(TAG, "populateTableVerses: invalid chapters count in [" + line + "]");
            continue;
          }
        } catch (NumberFormatException e) {
          Log.e(TAG, "populateTableVerses: invalid chapters count in [" + line + "]", e);
          continue;
        }

        // validate verses
        try {
          verses = Integer.parseInt(parts[3]);
          if (verses < 1) {
            Log.e(TAG, "populateTableVerses: invalid verses count in [" + line + "]");
            continue;
          }
        } catch (NumberFormatException e) {
          Log.e(TAG, "populateTableVerses: invalid verses count in [" + line + "]", e);
          continue;
        }

        dao.createVerse(new EntityVerse(parts[0], number, chapters, verses, parts[4]));

      }

      read.close();

    } catch (IOException ioe) {
      Log.e(TAG, "doWork: failed to open asset file [" + fileName + "]", ioe);
      return false;
    }

    return true;
  }

}
