package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.ArrayList;
import java.util.List;

public class BookmarkScreenModel
    extends AndroidViewModel {

  private ArrayList<Verse> list = new ArrayList<>();

  public BookmarkScreenModel(@NonNull final Application application) {
    super(application);
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
    return SbDatabase.getDatabase(getApplication()).getBookDao().getRecordLive(bookNumber);
  }

}
