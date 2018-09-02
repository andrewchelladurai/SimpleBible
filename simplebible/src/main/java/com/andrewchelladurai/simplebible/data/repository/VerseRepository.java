package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.ops.VerseRepositoryOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class VerseRepository
    extends AndroidViewModel
    implements VerseRepositoryOps {

    private static final String TAG = "VerseRepository";

    private final static ArrayList<Verse>       mCacheList     = new ArrayList<>();
    private static       int                    sCachedBook    = 0;
    private static       int                    sCachedChapter = 0;
    private final static HashMap<String, Verse> mCacheMap      = new HashMap<>();
    private static VerseRepositoryOps    THIS_INSTANCE;
    private        LiveData<List<Verse>> mLiveData;

    public VerseRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "VerseRepository: initialized");
    }

    public static VerseRepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    @Override
    public void clearCache() {
        mCacheList.clear();
        mCacheMap.clear();
        sCachedBook = sCachedChapter = 0;
        Log.d(TAG, "clearCache called");
    }

    @Override
    public boolean isCacheValid(@NonNull final Object... cacheParams) {
        if (isCacheEmpty()) {
            Log.d(TAG, "Empty cache");
            return false;
        }

        final int book = (int) cacheParams[0];
        final int chapter = (int) cacheParams[1];

        if (book == sCachedBook && chapter == sCachedChapter) {
            Log.d(TAG, "already cached [book=" + book + "][chapter=" + chapter + "]");
            return true;
        }
        Log.d(TAG, "invalid cache - book != sCachedBook || chapter != sCachedChapter");
        return false;
    }

    @Override
    public boolean populateCache(@NonNull final List<Verse> list,
                                 @NonNull final Object... cacheParams) {
        if (isCacheValid(cacheParams)) {
            Log.d(TAG, "already cached same data");
            return true;
        }

        clearCache();

        sCachedBook = (int) cacheParams[0];
        sCachedChapter = (int) cacheParams[1];
        for (Verse verse : list) {
            mCacheList.add(verse);
            mCacheMap.put(verse.getReference(), verse);
        }

        Log.d(TAG, "cached [" + getCacheSize() + "] records for [book="
                   + sCachedBook + "][chapter=" + sCachedChapter + "]");
        return true;
    }

    @Override
    public boolean isCacheEmpty() {
        return mCacheList.isEmpty() && mCacheMap.isEmpty() && getCacheSize() < 1;
    }

    @Override
    public int getCacheSize() {
        return (mCacheMap.size() == mCacheList.size()) ? mCacheList.size() : -1;
    }

    @Override
    public List<Verse> getCachedList() {
        return mCacheList;
    }

    @Override
    public LiveData<List<Verse>> queryDatabase(@NonNull final Object... cacheParams) {
        if (isCacheValid(cacheParams)) {
            Log.d(TAG, "returning cached live data");
            return mLiveData;
        }

        sCachedBook = (int) cacheParams[0];
        sCachedChapter = (int) cacheParams[1];

        if (sCachedBook == 0 || sCachedChapter == 0) {
            throw new UnsupportedOperationException("sCachedBook || sCachedChapter = 0");
        }

        mLiveData = SbDatabase.getInstance(getApplication()).getVerseDao()
                              .getChapter(sCachedBook, sCachedChapter);

        Log.d(TAG, "queried [Book=" + sCachedBook + "][Chapter=" + sCachedChapter + "]");

        return mLiveData;
    }

    @Override
    public boolean createRecord(final Verse verse) {
        SbDatabase.getInstance(getApplication()).getVerseDao().createRecord(verse);
        return true;
    }

    @Override
    public List<Verse> queryVerse(final int book, final int chapter, final int verse) {
        return SbDatabase.getInstance(getApplication()).getVerseDao()
                         .readRecord(book, chapter, verse);
    }
}
