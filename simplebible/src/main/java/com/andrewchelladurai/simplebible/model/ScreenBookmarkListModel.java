package com.andrewchelladurai.simplebible.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookmarkDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenBookmarkListModel
    extends AndroidViewModel {

  private static final String TAG = "ScreenBookmarkListModel";
  private final BookmarkDao bookmarkDao;
  private final VerseDao verseDao;

  public ScreenBookmarkListModel(@NonNull final Application application) {
    super(application);
    bookmarkDao = SbDatabase.getDatabase(getApplication())
                            .getBookmarkDao();
    verseDao = SbDatabase.getDatabase(getApplication())
                         .getVerseDao();
  }

  @NonNull
  public LiveData<List<Bookmark>> getBookmarkList() {
    return bookmarkDao.getAllRecords();
  }

  public LiveData<List<Verse>> getBookmarkedVerse(@NonNull final String bookmarkReference) {
    final String[] verseReferences = bookmarkReference.split(BookmarkUtils.SEPARATOR);

    String[] verseParts;
    final ArrayList<String> bookNumbers = new ArrayList<>();
    final ArrayList<String> chapterNumbers = new ArrayList<>();
    final ArrayList<String> verseNumbers = new ArrayList<>();

    for (final String verseReference : verseReferences) {
      verseParts = verseReference.split(VerseUtils.SEPARATOR);
      bookNumbers.add(verseParts[0]);
      chapterNumbers.add(verseParts[1]);
      verseNumbers.add(verseParts[2]);
    }

    return verseDao.getLiveVerses(bookNumbers, chapterNumbers, verseNumbers);
  }

}
