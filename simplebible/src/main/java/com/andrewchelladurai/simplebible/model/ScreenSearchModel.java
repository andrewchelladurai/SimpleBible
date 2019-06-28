package com.andrewchelladurai.simplebible.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import java.util.List;

public class ScreenSearchModel
    extends AndroidViewModel {

  private final VerseDao verseDao;

  public ScreenSearchModel(@NonNull final Application application) {
    super(application);
    verseDao = SbDatabase.getDatabase(getApplication()).getVerseDao();
  }

  public LiveData<List<Verse>> searchTextInVerses(@NonNull final String searchText) {
    final String queryText = "%" + searchText.toLowerCase() + "%";
    return verseDao.getLiveVersesWithText(queryText);
  }

}
