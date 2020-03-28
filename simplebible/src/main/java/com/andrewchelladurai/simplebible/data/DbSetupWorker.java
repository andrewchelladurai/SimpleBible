package com.andrewchelladurai.simplebible.data;

import android.content.Context;
import android.content.res.AssetManager;
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

    final Resources res = getApplicationContext().getResources();
    final AssetManager assManager = getApplicationContext().getAssets();

    String fileName = res.getString(R.string.db_setup_asset_file_table_books);
    String separator = res.getString(R.string.db_setup_asset_file_table_books_separator);
    String line;
    String[] parts;

    Log.d(TAG, "doWork: asset file [" + fileName + "] separated by [" + separator + "]");

    try {
      final BufferedReader read =
          new BufferedReader(new InputStreamReader(assManager.open(fileName)));
      final String newT = res.getString(R.string.new_testament);
      final String oldT = res.getString(R.string.old_testament);
      int number;
      int chapters;
      int verses;

      while ((line = read.readLine()) != null) {
        parts = line.split(separator);

        // we are expecting 5 parts in each line [description~number~name~chapters~verses]
        if (parts.length != 5) {
          Log.d(TAG, "doWork: : [" + line + "] does not have 5 parts");
          continue;
        }

        // check that we do nt have empty values in description & name
        if (parts[0].isEmpty() || parts[2].isEmpty()) {
          Log.e(TAG, "doWork: empty description or name in [" + line + "]");
          continue;
        }

        // validate book number
        number = Integer.parseInt(parts[1]);
        if (number < 1 || number > Utils.MAX_BOOKS) {
          Log.e(TAG, "doWork: book number invalid in [" + line + "]");
          continue;
        }

        // validate chapters count
        chapters = Integer.parseInt(parts[3]);
        if (chapters < 1) {
          Log.e(TAG, "doWork: invalid chapters count in [" + line + "]");
          continue;
        }

        // validate verses count
        verses = Integer.parseInt(parts[4]);
        if (verses < 1) {
          Log.e(TAG, "doWork: invalid verses count in [" + line + "]");
          continue;
        }

        dao.createBook(new EntityBook(
            parts[0], number, parts[2], chapters, verses, (number > 39) ? newT : oldT));

      }
    } catch (IOException ioe) {
      Log.e(TAG, "doWork: failed to open asset file [" + fileName + "]", ioe);
      return Result.failure();
    }

    fileName = res.getString(R.string.db_setup_asset_file_table_verses);
    separator = res.getString(R.string.db_setup_asset_file_table_verses_separator);

    Log.d(TAG, "doWork: asset file [" + fileName + "] separated by [" + separator + "]");

    return Result.success();
  }

}
