package com.andrewchelladurai.simplebible.data;

import android.app.Application;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BookRepository
    extends AndroidViewModel {

    private static final String               TAG       = "BookRepository";
    private static       LiveData<List<Book>> sLiveData = new MutableLiveData<>();

    public BookRepository(final Application application) {
        super(application);
        sLiveData = SbDatabase.getInstance(application).getBookDao().getAllBooks();
    }

    public LiveData<List<Book>> getList() {
        return sLiveData;
    }
}
