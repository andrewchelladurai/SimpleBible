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
    final Application app = getApplication();
    try {
      return dao.validateTableData(
          app.getString(R.string.db_setup_validation_book_name_first).trim(),
          app.getString(R.string.db_setup_validation_book_name_last).trim(),
          Integer.parseInt(app.getString(R.string.db_setup_validation_book_number_last).trim()),
          Integer.parseInt(app.getString(R.string.db_setup_validation_verse_number_last).trim()));
    } catch (NumberFormatException nfe) {
      Log.e(TAG, "validateBookTable: Failure getting validation values", nfe);
      return new MutableLiveData<Integer>(-1);
    }
  }

  public boolean setupDatabase() {
    Log.d(TAG, "setupDatabase:");
    if (!createBooksTable()) {
      Log.e(TAG, "setupDatabase: Failed to create sb_books");
      return false;
    }

    if (!createVersesTable()) {
      Log.e(TAG, "setupDatabase: Failed to create sb_verses");
      return false;
    }

    return true;
  }

  private boolean createBooksTable() {
    Log.e(TAG, "createBooksTable:");
    return false;
  }

  private boolean createVersesTable() {
    Log.e(TAG, "createVersesTable:");
    return false;
  }

}
