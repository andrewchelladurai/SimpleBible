package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.ops.SearchRepositoryOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SearchRepository
    extends AndroidViewModel
    implements SearchRepositoryOps {

    private static final String             TAG         = "SearchRepository";
    private static final List<Verse>        mCacheList  = new ArrayList<>();
    private static final Map<String, Verse> mCacheMap   = new HashMap<>();
    private static final StringBuilder      mCachedText = new StringBuilder();

    private static LiveData<List<Verse>> mLiveData;
    private static SearchRepositoryOps THIS_INSTANCE = null;

    SearchRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "SearchRepository: initialized");
    }

    public static SearchRepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
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
        Log.d(TAG, "queryDatabase: queried for [searchText:" + searchText + "]");
        return mLiveData;
    }

    @Override
    public boolean isCacheValid(final Object... cacheParams) {
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
    public boolean isCacheEmpty() {
        return mCacheList.isEmpty() && mCacheMap.isEmpty() && mCachedText.toString().isEmpty();
    }

    @Override
    public int getCacheSize() {
        return (mCacheList.size() == mCacheMap.size()) ? mCacheList.size() : -1;
    }

    @Override
    public void clearCache() {
        mCacheMap.clear();
        mCacheList.clear();
        mCachedText.delete(0, mCachedText.length());
    }

    @Override
    public boolean populateCache(final List<Verse> list, final Object... cacheParams) {
        final String searchText = (String) cacheParams[0];
        clearCache();
        mCachedText.append(searchText);

        for (Verse verse : list) {
            mCacheList.add(verse);
            mCacheMap.put(verse.getReference(), verse);
        }

        Log.d(TAG,
              "populateCache: cached [" + getCacheSize() + "] records for [" + mCachedText + "]");
        return isCacheValid(cacheParams);
    }

    @Override
    public List<Verse> getCachedList() {
        return mCacheList;
    }
}
