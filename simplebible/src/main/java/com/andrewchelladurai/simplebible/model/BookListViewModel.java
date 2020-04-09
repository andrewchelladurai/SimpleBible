package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.SbDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;

import java.util.List;

public class BookListViewModel
    extends AndroidViewModel {

  private static final String TAG = "BookListViewModel";

  private final SbDao dao;

  public BookListViewModel(@NonNull final Application application) {
    super(application);
    Log.d(TAG, "BookListViewModel:");
    dao = SbDatabase.getDatabase(getApplication()).getDao();
  }

  @NonNull
  public LiveData<List<EntityBook>> getAllBooks() {
    return dao.getAllBookRecords();
  }

}
