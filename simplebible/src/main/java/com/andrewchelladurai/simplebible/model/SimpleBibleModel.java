package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.andrewchelladurai.simplebible.data.BookDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.VerseDao;

public class SimpleBibleModel
    extends AndroidViewModel {

  private static final String TAG = "SimpleBibleModel";

  private final BookDao daoBook;

  private final VerseDao daoVerse;

  public SimpleBibleModel(@NonNull final Application application) {
    super(application);

    final SbDatabase database = SbDatabase.getDatabase(getApplication());
    daoBook = database.getDaoBook();
    daoVerse = database.getDaoVerse();

    Log.d(TAG, "SimpleBibleModel:");
  }

  /**
   * Query the database and get the sum of book and verse table records.
   * This can be used to identify if the both the essential tables have the expected number of
   * records.
   */
  public void getBooksAndVerseRecordCount() {
  }

}
