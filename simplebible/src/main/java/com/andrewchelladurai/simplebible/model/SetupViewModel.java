package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.SbDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;

public class SetupViewModel
    extends AndroidViewModel {

  private static final String TAG = "SetupViewModel";

  private static final MutableLiveData<Boolean> IS_SETUP = new MutableLiveData<>(false);

  private final SbDao dao;

  public SetupViewModel(@NonNull final Application application) {
    super(application);
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @NonNull
  public MutableLiveData<Boolean> isSetup() {
    return IS_SETUP;
  }

  public LiveData<Integer> validateTableData() {
    final Application application = getApplication();

    final String bookNameFirst;
    final String bookNameLast;
    final int lastBookPos;
    final int lastVersePos;

    try {
      bookNameFirst = application.getString(R.string.db_setup_validation_book_name_first)
                                 .trim();

      bookNameLast = application.getString(R.string.db_setup_validation_book_name_last)
                                .trim();

      lastBookPos = Integer.parseInt(
          application.getString(R.string.db_setup_validation_book_number_last).trim());

      lastVersePos = Integer.parseInt(
          application.getString(R.string.db_setup_validation_verse_number_last).trim());

    } catch (NumberFormatException nfe) {
      Log.e(TAG, "validateTableData: Failure getting validation values", nfe);
      return null;
    }

    return dao.validateTableData(bookNameFirst, bookNameLast, lastBookPos, lastVersePos);
  }

  public void setupDatabase() {
    Log.d(TAG, "setupDatabase:");
  }

}
