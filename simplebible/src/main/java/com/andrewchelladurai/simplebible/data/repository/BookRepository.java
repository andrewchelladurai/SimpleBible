package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.repository.ops.BookRepositoryOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BookRepository
    extends AndroidViewModel
    implements BookRepositoryOps {

    private static final String TAG = "BookRepository";

    private static BookRepositoryOps      THIS_INSTANCE = null;
    private static ArrayList<Book>        mCachedList   = new ArrayList<>();
    private static HashMap<Integer, Book> mCachedMap    = new HashMap<>();

    public BookRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "BookRepository: initialized");
    }

    public static BookRepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException(
                TAG + " getInstance: THIS_INSTANCE is not initialized");
        }
        return THIS_INSTANCE;
    }

    @Override
    public boolean createBook(final Book book) {
        SbDatabase.getInstance(getApplication()).getBookDao()
                  .createBook(book);
        return true;
    }

    @Override
    public boolean updateBook(@NonNull Book book) {
        SbDatabase.getInstance(getApplication()).getBookDao()
                  .updateBook(book);
        return true;
    }

    @Override
    public boolean deleteBook(@NonNull Book book) {
        SbDatabase.getInstance(getApplication()).getBookDao()
                  .deleteBook(book);
        return true;
    }

    @Override
    public LiveData<List<Book>> queryAllBooks() {
        return SbDatabase.getInstance(getApplication()).getBookDao().getAllRecords();
    }

    @Override
    public LiveData<List<Book>> queryBookUsingNumber(final int bookNumber) {
        return SbDatabase.getInstance(getApplication()).getBookDao().getBookUsingNumber(bookNumber);
    }

    @Override
    public LiveData<List<Book>> queryBookUsingName(final String bookName) {
        return SbDatabase.getInstance(getApplication()).getBookDao().getBookUsingName(bookName);
    }

    @Override
    public List<Book> getAllBooks() {
        if (!isCacheValid()) {
            throw new UnsupportedOperationException(TAG + " getAllBooks: cache is inValid");
        }
        return mCachedList;
    }

    @Override
    public Book getBookUsingNumber(final int bookNumber) {
        if (!isCacheValid()) {
            throw new UnsupportedOperationException(TAG + " getAllBooks: cache is inValid");
        }
        return mCachedMap.get(bookNumber);
    }

    @Override
    public Book getBookUsingName(final String bookName) {
        if (!isCacheValid()) {
            throw new UnsupportedOperationException(TAG + " getAllBooks: cache is inValid");
        }
        for (final Book book : mCachedList) {
            if (book.getBookName().equalsIgnoreCase(bookName)) {
                return book;
            }
        }
        return null;
    }

    @Override
    public int getNumberOfBooks() {
        return SbDatabase.getInstance(getApplication()).getBookDao().getNumberOfBooks();
    }

    @Override
    public boolean deleteAllRecords() {
        SbDatabase.getInstance(getApplication()).getBookDao().deleteAllRecords();
        return true;
    }

    @Override
    public boolean populateCache(@NonNull List<Book> list,
                                 @IntRange(from = 1, to = 66) final int count,
                                 @NonNull final String firstBook,
                                 @NonNull final String lastBook) {
        if (getCachedRecordCount() == count
            && mCachedList.get(0).getBookName().equalsIgnoreCase(firstBook)
            && mCachedList.get(mCachedList.size() - 1).getBookName().equalsIgnoreCase(lastBook)) {
            Log.d(TAG, "populateCache: already cached");
            return true;
        }

        mCachedList.clear();
        mCachedMap.clear();

        for (final Book book : list) {
            mCachedList.add(book);
            mCachedMap.put(book.getBookNumber(), book);
        }
        Log.d(TAG, "populateCache: [" + getCachedRecordCount() + "] books populated");

        boolean cacheValid = isCacheValid();
        Log.d(TAG, "populateCache: returned [cacheValid=" + cacheValid + "]");
        return cacheValid;
    }

    public boolean isCacheValid() {
        return !mCachedMap.isEmpty()
               && !mCachedList.isEmpty()
               && mCachedList.size() == mCachedMap.size();
    }

    @Override
    public int getCachedRecordCount() {
        return (mCachedList.size() == mCachedMap.size()) ? mCachedList.size() : -1;
    }
}
