package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;
import java.util.ArrayList;
import java.util.List;

public class BookmarkListScreenModel
    extends AndroidViewModel {

  private static final String TAG = "BookmarkListScreenModel";

  private final LiveData<List<Bookmark>> list;

  public BookmarkListScreenModel(@NonNull final Application application) {
    super(application);
    list = SbDatabase.getDatabase(application).getBookmarkDao().getAllRecords();
  }

  public LiveData<List<Bookmark>> getAllRecords() {
    return list;
  }

  public LiveData<List<Verse>> getVerses(@NonNull final String bookmarkReference) {
    if (bookmarkReference.isEmpty()) {
      Log.e(TAG, "getVerses: empty bookmarkReference passed");
      throw new IllegalArgumentException(TAG + " getVerses: empty bookmarkReference passed");
    }

    Log.d(TAG, "getVerses: bookmarkReference [" + bookmarkReference + "]");

    final ArrayList<Integer> bookList = new ArrayList<>();
    final ArrayList<Integer> chapterList = new ArrayList<>();
    final ArrayList<Integer> verseList = new ArrayList<>();
    int[] parts;

    final String[] verseReferences = bookmarkReference.split(BookmarkUtils.SEPARATOR);
    for (final String reference : verseReferences) {
      parts = VerseUtils.splitReference(reference);
      bookList.add(parts[0]);
      chapterList.add(parts[1]);
      verseList.add(parts[2]);
    }

    return SbDatabase.getDatabase(getApplication())
                     .getVerseDao()
                     .getRecords(bookList, chapterList, verseList);

  }

  public LiveData<Integer> doesBookmarkExist(@NonNull final String reference) {
    if (reference.isEmpty()) {
      throw new IllegalArgumentException(TAG + " bookmarkExists: Empty reference passed");
    }
    return SbDatabase.getDatabase(getApplication())
                     .getBookmarkDao()
                     .doesRecordExistLive(reference);
  }

}
