package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.ops.VerseRepositoryOps;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class VerseRepository
    extends AndroidViewModel
    implements VerseRepositoryOps {

    private static final String TAG = "VerseRepository";

    private static VerseRepositoryOps THIS_INSTANCE;

    public VerseRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "VerseRepository: initialized");
    }

    public static VerseRepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException(
                TAG + "Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    @Override
    public LiveData<List<Verse>> queryDatabase(@NonNull final Object... cacheParams) {
        int sCachedBook = (int) cacheParams[0];
        int sCachedChapter = (int) cacheParams[1];

        return SbDatabase.getInstance(getApplication()).getVerseDao()
                         .getChapter(sCachedBook, sCachedChapter);
    }

    @Override
    public List<Verse> queryVerse(final int book, final int chapter, final int verse) {
        return SbDatabase.getInstance(getApplication()).getVerseDao()
                         .readRecord(book, chapter, verse);
    }
}
