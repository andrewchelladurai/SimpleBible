package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
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

  private WorkInfo.State state;

  public SetupViewModel(@NonNull final Application application) {
    super(application);
    dao = SbDatabase.getDatabase(getApplication()).getDao();
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
      return new MutableLiveData<>(-1);
    }
  }

  public UUID setupDatabase(@NonNull final WorkManager workManager) {
    final OneTimeWorkRequest workRequest =
        new OneTimeWorkRequest.Builder(DbSetupWorker.class).build();

    workManager.enqueue(workRequest);

    return workRequest.getId();
  }

  @NonNull
  public WorkInfo.State getDatabaseSetupWorkerState() {
    return state;
  }

  public void setDatabaseSetupWorkerState(@NonNull final WorkInfo.State state) {
    this.state = state;
  }

}
