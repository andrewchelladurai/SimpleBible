package com.andrewchelladurai.simplebible.data;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BookRepository
    extends AndroidViewModel
    implements RepositoryOps {

    private static final String               TAG       = "BookRepository";
    private static       LiveData<List<Book>> sLiveData = new MutableLiveData<>();
    private static       ArrayList<Book>      mBookList = new ArrayList<>();
    private static BookRepository THIS_INSTANCE;

    public BookRepository(final Application application) {
        super(application);
        sLiveData = SbDatabase.getInstance(application).getBookDao().getAllBooks();
        THIS_INSTANCE = this;
    }

    public static BookRepository getInstance() {
        return THIS_INSTANCE;
    }

    @Override
    public boolean populate(final List<?> list) {
        for (final Object object : list) {
            mBookList.add((Book) object);
        }
        Log.d(TAG, "populate: populated [" + size() + "] books");
        return true;
    }

    @Override
    public void clear() {
        mBookList.clear();
    }

    @Override
    public boolean isEmpty() {
        return mBookList.isEmpty();
    }

    @Override
    public int size() {
        return mBookList.size();
    }

    @Override
    public Object getRecordUsingKey(final Object key) {
        return null;
    }

    @Override
    public Object getRecordUsingValue(final Object value) {
        return null;
    }

    @Override
    public ArrayList<Book> getList() {
        return mBookList;
    }

    public LiveData<List<Book>> getLiveData() {
        return sLiveData;
    }

    @Override
    public boolean validate(final Object... objects) {
        return false;
    }
}
