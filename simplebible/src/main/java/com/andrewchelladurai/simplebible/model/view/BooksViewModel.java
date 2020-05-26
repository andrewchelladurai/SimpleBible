package com.andrewchelladurai.simplebible.model.view;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.db.SbDatabase;
import com.andrewchelladurai.simplebible.db.dao.SbDao;
import com.andrewchelladurai.simplebible.db.entities.EntityBook;

import java.util.List;

public class BooksViewModel
    extends AndroidViewModel {

  private static final String TAG = "BooksViewModel";

  private final SbDao dao;

  public BooksViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "BookListViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @NonNull
  public LiveData<List<EntityBook>> getAllBooks() {
    return dao.getAllBookRecords();
  }

}
