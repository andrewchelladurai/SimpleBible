package com.andrewchelladurai.simplebible.model;

import android.app.Application;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.List;

public class ScreenSearchModel
    extends AndroidViewModel {

  private final VerseDao verseDao;
  private final BookDao bookDao;

  public ScreenSearchModel(@NonNull final Application application) {
    super(application);
    verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
    bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
  }

  public LiveData<List<Verse>> searchTextInVerses(@NonNull final String searchText) {
    final String queryText = "%" + searchText.toLowerCase() + "%";
    return verseDao.getLiveVersesWithText(queryText);
  }

  public LiveData<Book> getBook(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return bookDao.getBookUsingPositionLive(bookNumber);
  }

}
