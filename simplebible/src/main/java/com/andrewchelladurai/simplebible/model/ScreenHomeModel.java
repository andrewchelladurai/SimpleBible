package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.andrewchelladurai.simplebible.data.DbSetupJob;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;

public class ScreenHomeModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenHomeModel";
  private final VerseDao verseDao;
  private final BookDao bookDao;
  private MutableLiveData<Integer> dbSetupJobState = new MutableLiveData<>();

  public ScreenHomeModel(@NonNull final Application application) {
    super(application);
    verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
    bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
  }

  @NonNull
  public MutableLiveData<Integer> getDbSetupJobState() {
    return dbSetupJobState;
  }

  public void setDbSetupJobState(
      @IntRange(from = DbSetupJob.STARTED, to = DbSetupJob.FINISHED) final int state) {
    Log.d(TAG, "setDbSetupJobState: state = [" + state + "]");
    dbSetupJobState.postValue(state);
  }

  public LiveData<Integer> getVerseCount() {
    return verseDao.getLiveVerseCount();
  }

}
