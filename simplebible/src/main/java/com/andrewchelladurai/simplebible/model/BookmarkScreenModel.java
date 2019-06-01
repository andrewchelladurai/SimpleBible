package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.utils.BookUtils;

public class BookmarkScreenModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkScreenModel";

  private final BookmarkDao bookmarkDao;

  public BookmarkScreenModel(@NonNull final Application application) {
    super(application);
    bookmarkDao = SbDatabase.getDatabase(getApplication()).getBookmarkDao();
  }

  public LiveData<Book> getBook(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return SbDatabase.getDatabase(getApplication())
                     .getBookDao()
                     .getRecordLive(bookNumber);
  }

}
