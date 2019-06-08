package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import java.util.List;

public class BookmarkListScreenModel
    extends AndroidViewModel {

  private final LiveData<List<Bookmark>> list;

  public BookmarkListScreenModel(@NonNull final Application application) {
    super(application);
    list = SbDatabase.getDatabase(application).getBookmarkDao().getAllRecords();
  }

  public LiveData<List<Bookmark>> getAllRecords() {
    return list;
  }

}
