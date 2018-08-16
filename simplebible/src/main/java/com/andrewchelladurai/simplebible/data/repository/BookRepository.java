package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BookRepository
    extends AndroidViewModel
    implements RepositoryOps {

    private static final String          TAG       = "BookRepository";
    private static final ArrayList<Book> mBookList = new ArrayList<>();

    private static LiveData<List<Book>> sLiveData     = null;
    private static BookRepository       THIS_INSTANCE = null;

    public BookRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "BookRepository: initialized");
    }

    public static BookRepository getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    @Override
    public boolean populateCache(final List<?> list) {
        clearCache();
        for (final Object object : list) {
            mBookList.add((Book) object);
        }
        Log.d(TAG, "populated [" + getCacheSize() + "] books in cache");
        return true;
    }

    @Override
    public void clearCache() {
        mBookList.clear();
    }

    @Override
    public boolean isCacheEmpty() {
        return mBookList.isEmpty();
    }

    @Override
    public int getCacheSize() {
        return mBookList.size();
    }

    @Override
    public Object getCachedRecordUsingKey(@NonNull final Object key) {
        int number = (int) key;
        for (final Book book : mBookList) {
            if (book.getNumber() == number) {
                return book;
            }
        }
        Log.e(TAG, " no book found with key [" + number + "]");
        return null;
    }

    @Override
    public Book getCachedRecordUsingValue(@NonNull final Object value) {
        String bookName = (String) value;
        for (final Book book : mBookList) {
            if (book.getName().equalsIgnoreCase(bookName)) {
                return book;
            }
        }
        Log.e(TAG, " no book found with name [" + bookName + "]");
        return null;
    }

    @Override
    public ArrayList<Book> getCachedList() {
        return mBookList;
    }

    public LiveData<List<Book>> queryDatabase() {
        if (sLiveData == null) {
            sLiveData = SbDatabase.getInstance(getApplication())
                                  .getBookDao().getAllBooks();
        }
        return sLiveData;
    }

    @Override
    public LiveData<?> queryDatabase(final Object... objects) {
        throw new UnsupportedOperationException("This method must not be used");
    }

    @Override
    public boolean isCacheValid(final Object... objects) {
        if (isCacheEmpty() || getCacheSize() != 66) {
            Log.d(TAG, "cache is empty or does not have 66 records");
            return false;
        }
        if (!mBookList.get(0).getName().equalsIgnoreCase((String) objects[0])
            || !mBookList.get(getCacheSize() - 1).getName().equalsIgnoreCase((String) objects[1])) {
            Log.d(TAG, "cache's first and last book is invalid");
            return false;
        }
        Log.d(TAG, "valid cached data");
        return true;
    }
}
