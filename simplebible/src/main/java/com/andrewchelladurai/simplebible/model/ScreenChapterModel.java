package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.andrewchelladurai.simplebible.data.entity.Book;

public class ScreenChapterModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenChapterModel";

  @NonNull
  private Book book;

  public ScreenChapterModel(@NonNull final Application application) {
    super(application);
  }

  public void setBookArgument(@NonNull final Book book) {
    this.book = book;
    Log.d(TAG, "setBookArgument: [" + this.book + "]");
  }

  @NonNull
  public Book getBook() {
    return book;
  }

}
