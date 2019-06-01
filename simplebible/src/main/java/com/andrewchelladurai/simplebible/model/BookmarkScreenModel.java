package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils.CreateBookmarkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookmarkScreenModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkScreenModel";

  private final BookmarkDao bookmarkDao;
  private final BookDao bookDao;
  private final ArrayList<Verse> list = new ArrayList<>();

  public BookmarkScreenModel(@NonNull final Application application) {
    super(application);
    bookDao = SbDatabase.getDatabase(getApplication()).getBookDao();
    bookmarkDao = SbDatabase.getDatabase(getApplication()).getBookmarkDao();
  }

  public void updateCacheList(final List<?> newList) {
    list.clear();
    for (final Object o : newList) {
      list.add((Verse) o);
    }
  }

  public List<?> getCachedList() {
    return list;
  }

  public Verse getCachedItemAt(final int position) {
    return list.get(position);
  }

  public int getCachedListSize() {
    return list.size();
  }

  public LiveData<Book> getBook(
      @IntRange(from = 1, to = BookUtils.EXPECTED_COUNT) final int bookNumber) {
    return bookDao.getRecordLive(bookNumber);
  }

  public String createBookmarkReference(@NonNull final List<?> newList) {
    return BookmarkUtils.createBookmarkReference(newList);
  }

  @NonNull
  public LiveData<Integer> doesBookmarkExist(final String reference) {
    return bookDao.doesBookmarkExist(reference);
  }

  @Nullable
  public Integer createBookmark(@NonNull final String note) {
    final String reference = BookmarkUtils.createBookmarkReference(list);
    Log.d(TAG, "createBookmark: note = [" + note + "], reference[" + reference + "]");
    final CreateBookmarkTask createBookmarkTask = new CreateBookmarkTask(bookmarkDao);
    createBookmarkTask.execute(new Bookmark(reference, note));
    try {
      return createBookmarkTask.get();
    } catch (ExecutionException e) {
      Log.e(TAG, "createBookmark: ", e);
    } catch (InterruptedException e) {
      Log.e(TAG, "createBookmark: ", e);
    }
    return null;
  }

}
