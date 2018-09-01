package com.andrewchelladurai.simplebible.data.repository.ops;

import com.andrewchelladurai.simplebible.data.entities.Book;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 01-Sep-2018 @ 2:45 PM.
 */
public interface BookRepositoryOps {

    boolean createBook(@NonNull Book book);

    boolean updateBook(@NonNull Book book);

    boolean deleteBook(@NonNull Book book);

    LiveData<List<Book>> queryAllBooks();

    LiveData<List<Book>> queryBookUsingNumber(@IntRange(from = 1, to = 66) int bookNumber);

    LiveData<List<Book>> queryBookUsingName(String bookName);

    List<Book> getAllBooks();

    Book getBookUsingNumber(@IntRange(from = 1, to = 66) int bookNumber);

    Book getBookUsingName(String bookName);

    int getNumberOfBooks();

    boolean deleteAllRecords();

    boolean populateCache(@NonNull List<Book> list,
                          @IntRange(from = 1, to = 66) final int count,
                          @NonNull final String firstBook,
                          @NonNull final String lastBook);

    public boolean isCacheValid();

    public int getCachedRecordCount();

}
