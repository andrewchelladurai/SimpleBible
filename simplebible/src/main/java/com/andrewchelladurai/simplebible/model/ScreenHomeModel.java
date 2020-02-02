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
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

import java.util.Calendar;

public class ScreenHomeModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenHomeModel";

  @IntRange(from = 1, to = 365)
  private static int CACHED_DAY;
  @NonNull
  private static String CACHED_REFERENCE = "";
  @NonNull
  private static String CACHED_RAW_TEXT = "";
  @NonNull
  private final VerseDao verseDao;
  @NonNull
  private final BookDao bookDao;
  @NonNull
  private MutableLiveData<Integer> dbSetupJobState = new MutableLiveData<>();

  public ScreenHomeModel(@NonNull final Application application) {
    super(application);
    verseDao = SbDatabase.getDatabase(getApplication())
                         .getVerseDao();
    bookDao = SbDatabase.getDatabase(getApplication())
                        .getBookDao();
    Log.d(TAG, "ScreenHomeModel:");
  }

  @NonNull
  public MutableLiveData<Integer> getDbSetupJobState() {
    return dbSetupJobState;
  }

  public void setDbSetupJobState(
      @IntRange(from = DbSetupJob.STARTED, to = DbSetupJob.FINISHED) final int state) {
    dbSetupJobState.postValue(state);
  }

  public LiveData<Integer> getVerseCount() {
    return verseDao.getLiveVerseCount();
  }

  public int getDayNumber() {
    return Calendar.getInstance()
                   .get(Calendar.DAY_OF_YEAR);
  }

  @NonNull
  public LiveData<Verse> getVerse(@NonNull final String reference) {
    final int[] parts = VerseUtils.getInstance()
                                  .splitReference(reference);
    return verseDao.getLiveVerse(parts[0], parts[1], parts[2]);
  }

  @NonNull
  public LiveData<Book> getBook(@IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int book) {
    return bookDao.getBookUsingPositionLive(book);
  }

  @IntRange(from = 1, to = 365)
  public int getCachedDayOfYear() {
    return CACHED_DAY;
  }

  public void setCachedDayOfYear(@IntRange(from = 1, to = 365) final int dayNumber) {
    CACHED_DAY = dayNumber;
  }

  @NonNull
  public String getCachedReference() {
    return CACHED_REFERENCE;
  }

  public void setCachedReference(@NonNull final String reference) {
    CACHED_REFERENCE = reference;
  }

  @NonNull
  public String getCachedRawVerseText() {
    return CACHED_RAW_TEXT;
  }

  public void setCachedRawVerseText(@NonNull final String rawVerseText) {
    CACHED_RAW_TEXT = rawVerseText;
  }

}
