package com.andrewchelladurai.simplebible.data.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BookRepository
    extends AndroidViewModel
    implements RepositoryOps {

    private static final String TAG = "BookRepository";

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, Book> CACHE_MAP  = new HashMap<>();
    private final List<Book>         CACHE_LIST = new ArrayList<>();
    private LiveData<List<Book>> LIVE_DATA;

    private static BookRepository THIS_INSTANCE = null;

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
        // FIXME: 16/8/18 pass cache checking parameters and skip is chache is valid
        clearCache();
        Book book;
        for (final Object object : list) {
            book = (Book) object;
            CACHE_LIST.add(book);
            CACHE_MAP.put(book.getNumber(), book);
        }
        Log.d(TAG, "updated cache with [" + getCacheSize() + "] books");
        return !isCacheEmpty();
    }

    @Override
    public void clearCache() {
        CACHE_MAP.clear();
        CACHE_LIST.clear();
    }

    @Override
    public boolean isCacheEmpty() {
        return CACHE_MAP.isEmpty() || CACHE_LIST.isEmpty();
    }

    @Override
    public int getCacheSize() {
        return (CACHE_LIST.size() == CACHE_MAP.size()) ? CACHE_LIST.size() : -1;
    }

    @Override
    @Nullable
    public Book getCachedRecordUsingKey(@NonNull final Object bookNumber) {
        if (isCacheEmpty()) {
            Log.e(TAG, "getCachedRecordUsingKey: cache is empty");
            return null;
        }

        final int number = (int) bookNumber;
        return CACHE_MAP.get(number);
    }

    @Override
    @Nullable
    public Book getCachedRecordUsingValue(@NonNull final Object bookName) {
        if (isCacheEmpty()) {
            Log.e(TAG, "getCachedRecordUsingValue: cache is empty");
            return null;
        }

        final String name = (String) bookName;

        for (final Book book : CACHE_LIST) {
            if (book.getName().equalsIgnoreCase(name)) {
                return book;
            }
        }
        return null;
    }

    @Override
    @Nullable
    public List<Book> getCachedList() {
        if (isCacheEmpty()) {
            Log.e(TAG, "getCachedList: cache is empty");
            return null;
        }

        return CACHE_LIST;
    }

    @Override
    @Nullable
    public LiveData<List<Book>> queryDatabase() {
        throw new UnsupportedOperationException("do not use this");
    }

    @Override
    @Nullable
    public LiveData<List<Book>> queryDatabase(final Object... objects) {
        if (isCacheValid(objects)) {
            Log.d(TAG, "returning cached data");
            return LIVE_DATA;
        }

        LIVE_DATA = SbDatabase.getInstance(getApplication()).getBookDao().getAllBooks();
        return LIVE_DATA;
    }

    @Override
    public boolean isCacheValid(final Object... objects) {
        final int bookLimit = (int) objects[0];
        final String firstBook = (String) objects[1];
        final String lastBook = (String) objects[2];

        if (bookLimit < 0 || firstBook.isEmpty() || lastBook.isEmpty()) {
            throw new UnsupportedOperationException("isCacheValid: invalid values passed");
        }

        if (isCacheEmpty()) {
            Log.d(TAG, "isCacheValid: [" + false + "]");
            return false;
        }

        final List<Book> cachedList = getCachedList();
        final int cacheSize = cachedList.size();
        final String cachedFirstBook = cachedList.get(0).getName();
        final String cachedLastBook = cachedList.get(cacheSize - 1).getName();

        boolean isValid = (cacheSize == bookLimit
                           && cachedFirstBook.equalsIgnoreCase(firstBook)
                           && cachedLastBook.equalsIgnoreCase(lastBook));

        Log.d(TAG, "isCacheValid: [" + isValid + "]");
        return isValid;
    }
}
