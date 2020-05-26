package com.andrewchelladurai.simplebible.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupWorker;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.SbDao;
import com.andrewchelladurai.simplebible.data.entities.EntityBook;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.List;
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
    return dao.validateTableData(
        app.getString(R.string.db_setup_validation_book_name_first).trim(),
        app.getString(R.string.db_setup_validation_book_name_last).trim(),
        app.getString(R.string.db_setup_validation_book_number_last).trim(),
        app.getString(R.string.db_setup_validation_verse_number_last).trim());
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

  @NonNull
  public LiveData<List<EntityBook>> getAllBooks() {
    return dao.getAllBookRecords();
  }

  public void updateCacheBooks(@NonNull final List<EntityBook> bookList) {
    Utils.getInstance().updateCacheBooks(bookList);
  }

  public boolean isCacheUpdated() {
    return Utils.getInstance().isCacheUpdated();
  }

}
