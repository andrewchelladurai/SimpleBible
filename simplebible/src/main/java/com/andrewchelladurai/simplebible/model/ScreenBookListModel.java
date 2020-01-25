package com.andrewchelladurai.simplebible.model;

import android.app.Application;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.List;

public class ScreenBookListModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenBookListModel";
  private final BookDao bookDao;

  public ScreenBookListModel(@NonNull final Application application) {
    super(application);
    bookDao = SbDatabase.getDatabase(getApplication())
                        .getBookDao();
  }

  public LiveData<List<Book>> getAllBooks() {
    return bookDao.getAllBooksLive();
  }

  public LiveData<Book> getBookUsingNumber(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return bookDao.getBookUsingPositionLive(bookNumber);
  }

}
