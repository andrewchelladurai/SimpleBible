package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.entity.Book;
import java.util.List;

public class ScreenBookListModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenBookListModel";
  private final BookDao bookDao;

  public ScreenBookListModel(@NonNull final Application application) {
    super(application);
    bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
  }

  public LiveData<List<Book>> getAllBooks() {
    return bookDao.getAllBooksLive();
  }

}
