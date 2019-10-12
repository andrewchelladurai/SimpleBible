package com.andrewchelladurai.simplebible.model;

import android.app.Application;

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
    dbSetupJobState.postValue(state);
  }

  public LiveData<Integer> getVerseCount() {
    return verseDao.getLiveVerseCount();
  }

  public int getDayNumber() {
    return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
  }

  public LiveData<Verse> getVerse(final String reference) {
    final int[] parts = VerseUtils.getInstance().splitReference(reference);
    return verseDao.getLiveVerse(parts[0], parts[1], parts[2]);
  }

  public LiveData<Book> getBook(@IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int book) {
    return bookDao.getBookUsingPositionLive(book);
  }

}
