package com.andrewchelladurai.simplebible.data;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
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
        clear();
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
    public Object getRecordUsingKey(@NonNull final Object key) {
        int number = (int) key;
        for (final Book book : mBookList) {
            if (book.getNumber() == number) {
                return book;
            }
        }
        return null;
    }

    @Override
    public Book getRecordUsingValue(@NonNull final Object value) {
        String bookName = (String) value;
        for (final Book book : mBookList) {
            if (book.getName().equalsIgnoreCase(bookName)) {
                return book;
            }
        }
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
        if (isEmpty() || size() != 66) {
            Log.d(TAG, "validate: repository cache empty or does not have 66 records");
            return false;
        }
        if (!mBookList.get(0).getName().equalsIgnoreCase((String) objects[0])
            || !mBookList.get(size() - 1).getName().equalsIgnoreCase((String) objects[1])) {
            Log.d(TAG, "validate: repository cache's first and last book is invalid");
            return false;
        }
        Log.d(TAG, "validate: repository has cached data");
        return true;
    }
}
