package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SearchRepository
    extends BaseRepository {

    private static final String             TAG         = "SearchRepository";
    private static final List<Verse>        mCacheList  = new ArrayList<>();
    private static final Map<String, Verse> mCacheMap   = new HashMap<>();
    private static final StringBuilder      mCachedText = new StringBuilder();
    private static RepositoryOps         THIS_INSTANCE;
    private static LiveData<List<Verse>> mLiveData;

    SearchRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "SearchRepository: initialized");
    }

    public static RepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    @Override
    boolean isCacheValid(final Object... cacheParams) {
        final String searchText = (String) cacheParams[0];
        if (searchText.isEmpty()) {
            throw new UnsupportedOperationException(TAG + " isCacheValid: empty searchText passed");
        }

        if (isCacheEmpty()) {
            Log.d(TAG, "isCacheValid: empty cache");
            return false;
        }

        return searchText.equalsIgnoreCase(mCachedText.toString());
    }

    @Override
    public boolean populateCache(final List<?> list, final Object... cacheParams) {
        final String searchText = (String) cacheParams[0];
        clearCache();
        mCachedText.append(searchText);

        Verse verse;
        for (final Object object : list) {
            verse = (Verse) object;
            mCacheList.add(verse);
            mCacheMap.put(verse.getReference(), verse);
        }

        Log.d(TAG,
              "populateCache: cached [" + getCacheSize() + "] records for [" + mCachedText + "]");
        return !isCacheEmpty();
    }

    @Override
    public boolean isCacheEmpty() {
        return mCacheList.isEmpty() && mCacheMap.isEmpty() && mCachedText.toString().isEmpty();
    }

    @Override
    public int getCacheSize() {
        return (mCacheList.size() == mCacheMap.size()) ? mCacheList.size() : -1;
    }

    @Override
    @Nullable
    public Verse getCachedRecordUsingKey(@NonNull final Object key) {
        final String reference = (String) key;
        if (reference.isEmpty()) {
            throw new UnsupportedOperationException(
                TAG + "getCachedRecordUsingKey: empty verse reference passed");
        }
        return mCacheMap.get(reference);
    }

    @Override
    @Nullable
    public Verse getCachedRecordUsingValue(@NonNull final Object value) {
        throw new UnsupportedOperationException("use method getCachedRecordUsingKey() instead");
    }

    @Override
    public List<Verse> getCachedList() {
        return mCacheList;
    }

    @Override
    public LiveData<List<Verse>> queryDatabase(@NonNull final Object... cacheParams) {
        final String searchText = (String) cacheParams[0];
        if (isCacheValid(cacheParams)) {
            Log.d(TAG,
                  "queryDatabase: already ached results for [searchText = " + mCachedText + "]");
            return mLiveData;
        }

        mLiveData = SbDatabase.getInstance(getApplication()).getVerseDao()
                              .getVerseWithText("%" + searchText + "%");
        Log.d(TAG, "queryDatabase: retrieved results for [searchText:" + searchText + "]");
        return mLiveData;
    }

    @Override
    public boolean createRecord(final Object entityObject) {
        throw new UnsupportedOperationException(TAG + " createRecord: do not use this method");
    }

    @Override
    public boolean deleteRecord(final Object entityObject) {
        throw new UnsupportedOperationException(TAG + " deleteRecord: do not use this method");
    }

    @Override
    public void clearCache() {
        mCacheMap.clear();
        mCacheList.clear();
        mCachedText.delete(0, mCachedText.length());
    }
}
