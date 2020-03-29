package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupWorker;
import com.andrewchelladurai.simplebible.data.SbDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;

import java.util.UUID;

public class SetupViewModel
    extends AndroidViewModel {

  private static final String TAG = "SetupViewModel";

  private final SbDao dao;

  private static UUID DB_SETUP_WORKER_UUID;

  public SetupViewModel(@NonNull final Application application) {
    super(application);
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @NonNull
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
      return new MutableLiveData<>(-1);
    }
  }

  @NonNull
  public UUID setupDatabase(@NonNull final WorkManager workManager) {
    if (DB_SETUP_WORKER_UUID != null) {
      return DB_SETUP_WORKER_UUID;
    }

    final OneTimeWorkRequest workRequest =
        new OneTimeWorkRequest.Builder(DbSetupWorker.class).build();
    workManager.enqueue(workRequest);
    return workRequest.getId();

  }

  public UUID getWorkerUuid() {
    return DB_SETUP_WORKER_UUID;
  }

  public void setWorkerUuid(@NonNull final UUID uuid) {
    DB_SETUP_WORKER_UUID = uuid;
  }

}
