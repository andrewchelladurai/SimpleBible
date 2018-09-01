package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class VerseRepository
    extends AndroidViewModel {

    private static final String TAG = "VerseRepository";

    private static VerseRepository THIS_INSTANCE;
    private static int sCachedBook    = 0;
    private static int sCachedChapter = 0;

    private final ArrayList<Verse>       mCacheList = new ArrayList<>();
    private final HashMap<String, Verse> mCacheMap  = new HashMap<>();
    private       LiveData<List<Verse>>  mLiveData  = new MutableLiveData<>();

    public VerseRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "VerseRepository: initialized");
    }

    public static VerseRepository getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    public void clearCache() {
        mCacheList.clear();
        mCacheMap.clear();
    }

    public boolean isCacheValid(@NonNull final Object... cacheParams) {
        if (isCacheEmpty()) {
            Log.d(TAG, "cache is empty");
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

    public boolean populateCache(@NonNull final List<?> list,
                                 @NonNull final Object... cacheParams) {
        if (isCacheValid(cacheParams)) {
            Log.d(TAG, "already cached same data");
            return true;
        }

        clearCache();
        Verse verse;
        for (final Object object : list) {
            verse = (Verse) object;
            mCacheList.add((Verse) object);
            mCacheMap.put(verse.getReference(), verse);
        }

        Log.d(TAG, "cached [" + getCacheSize() + "] records for [book="
                   + sCachedBook + "][chapter=" + sCachedChapter + "]");
        return true;
    }

    public boolean isCacheEmpty() {
        return mCacheList.isEmpty() & mCacheMap.isEmpty();
    }

    public int getCacheSize() {
        return (mCacheMap.size() == mCacheList.size()) ? mCacheList.size() : -1;
    }

    public Object getCachedRecordUsingKey(@NonNull final Object key) {
        final String reference = (String) key;
        if (mCacheMap.containsKey(reference)) {
            return mCacheMap.get(reference);
        }
        return null;
    }

    public Object getCachedRecordUsingValue(@NonNull final Object value) {
        throw new UnsupportedOperationException("Do not look for verse using value");
    }

    public List<Verse> getCachedList() {
        return mCacheList;
    }

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

    public boolean createRecord(final Object entityObject) {
        SbDatabase.getInstance(getApplication()).getVerseDao()
                  .createRecord((Verse) entityObject);
        return true;
    }

    public boolean deleteRecord(final Object entityObject) {
        throw new UnsupportedOperationException("should not be used");
    }

    public List<Verse> getVerse(final int book, final int chapter, final int verse) {
        return SbDatabase.getInstance(getApplication()).getVerseDao()
                         .readRecord(book, chapter, verse);
    }
}
