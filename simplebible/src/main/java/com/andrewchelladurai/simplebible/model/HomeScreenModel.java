package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;

public class HomeScreenModel
    extends AndroidViewModel {

  private final VerseDao verseDao;

  private final BookDao bookDao;

  public HomeScreenModel(@NonNull final Application application) {
    super(application);
    bookDao = SbDatabase.getDatabase(application).getBookDao();
    verseDao = SbDatabase.getDatabase(application).getVerseDao();
  }

  public LiveData<Integer> getBookCount() {
    return bookDao.getLiveBookCount();
  }

  public LiveData<Integer> getVerseCount() {
    return verseDao.getLiveVerseCount();
  }

}
