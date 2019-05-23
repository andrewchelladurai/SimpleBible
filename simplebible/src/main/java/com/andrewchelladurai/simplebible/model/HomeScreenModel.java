package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.BookDao;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

import java.util.Calendar;

public class HomeScreenModel
    extends AndroidViewModel {

  private static final String TAG = "HomeScreenModel";

  private final VerseDao verseDao;

  private final BookDao bookDao;

  private String verseText = "";

  public HomeScreenModel(@NonNull final Application application) {
    super(application);
    bookDao = SbDatabase.getDatabase(application).getBookDao();
    verseDao = SbDatabase.getDatabase(application).getVerseDao();
  }

  public LiveData<Integer> getBookCount() {
    return bookDao.getLiveBookCount();
  }

  public LiveData<Integer> getVerseCount() {
    return verseDao.getLiveVerseCount();
  }

  @NonNull
  public String getVerseReferenceForToday(@NonNull final String defaultReference,
                                          @NonNull final String[] referenceArray) {
    final int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    if (referenceArray.length < dayOfYear - 1) {
      Log.e(TAG, "getVerseReferenceForToday: reference referenceArray doesn't have ["
                 + dayOfYear + "] item(s), returning defaultReference[" + defaultReference + "]");
      return defaultReference;
    }

    final String reference = referenceArray[dayOfYear - 1];
    if (!validateReference(reference)) {
      Log.e(TAG, "getVerseReferenceForToday: reference [" + reference
                 + "] is invalid, returning defaultReference[" + defaultReference + "]");
      return defaultReference;
    }

    return reference;
  }

  public boolean validateReference(final String reference) {
    return VerseUtils.validateReference(reference);
  }

  public LiveData<Verse> getVerseForToday(@NonNull final String reference) {
    final int[] referencePart = VerseUtils.splitReference(reference);
    return SbDatabase.getDatabase(getApplication())
                     .getVerseDao()
                     .getRecordLive(referencePart[0], referencePart[1], referencePart[2]);
  }

  public LiveData<Book> getBook(@IntRange(from = 1, to = 66) final int bookNumber) {
    return bookDao.getRecordLive(bookNumber);
  }

  @NonNull
  public String getCachedVerseText() {
    return verseText;
  }

  public void setCachedVerseText(@NonNull final String verseText) {
    this.verseText = verseText;
    Log.d(TAG, "setCachedVerseText: size [" + verseText.length() + "]");
  }

}
