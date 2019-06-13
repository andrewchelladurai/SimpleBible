package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.andrewchelladurai.simplebible.data.DbSetupJob;

public class ScreenHomeModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenHomeModel";
  private MutableLiveData<Integer> dbSetupJobState = new MutableLiveData<>();

  public ScreenHomeModel(@NonNull final Application application) {
    super(application);
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

}
