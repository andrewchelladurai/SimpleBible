package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:57 PM.
 */
public class BookmarkRepository
    extends BaseRepository {

    private static final String TAG = "BookmarkRepository";
    private static RepositoryOps THIS_INSTANCE;

    private final List<Bookmark>            mCacheList = new ArrayList<>();
    private final HashMap<String, Bookmark> mCacheMap  = new HashMap<>();

    private LiveData<List<Bookmark>> mLiveData;

    public BookmarkRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "BookmarkRepository: initialized");
    }

    public static RepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    @Override
    public boolean populateCache(@NonNull final List<?> list, @NonNull Object... cacheParams) {
        if (isCacheValid(cacheParams)) {
            Log.d(TAG, "already cached same data");
            return true;
        }

        clearCache();
        Bookmark bookmark;
        for (final Object object : list) {
            bookmark = (Bookmark) object;
            mCacheList.add(bookmark);
            mCacheMap.put(bookmark.getReference(), bookmark);
        }
        Log.d(TAG, "populateCache: updated cache with [" + getCacheSize() + "] bookmarks");
        return false;
    }

    @Override
    public void clearCache() {
        mCacheList.clear();
        mCacheMap.clear();
    }

    @Override
    public boolean isCacheEmpty() {
        return mCacheList.isEmpty() && mCacheMap.isEmpty();
    }

    @Override
    public int getCacheSize() {
        return (mCacheList.size() == mCacheMap.size()) ? mCacheList.size() : -1;
    }

    @Override
    public Bookmark getCachedRecordUsingKey(@NonNull final Object key) {
        final String bookmarkReference = (String) key;
        return mCacheMap.get(bookmarkReference);
    }

    @Override
    @Nullable
    public Bookmark getCachedRecordUsingValue(@NonNull final Object value) {
        final String note = (String) value;
        for (final Bookmark bookmark : mCacheList) {
            if (bookmark.getNote().equalsIgnoreCase(note)) {
                return bookmark;
            }
        }
        return null;
    }

    @Override
    public List<Bookmark> getCachedList() {
        return mCacheList;
    }

    @Override
    public LiveData<List<Bookmark>> queryDatabase(@NonNull final Object... cacheParams) {
        if (isCacheValid(cacheParams)) {
            Log.d(TAG, "returning cached live data");
            return mLiveData;
        }

        mLiveData = SbDatabase.getInstance(getApplication())
                              .getBookmarkDao()
                              .getAllBookmarks();
        return mLiveData;
    }

    @Override
    public boolean isCacheValid(final Object... cacheParams) {
        return isCacheEmpty();
    }

    public List<Bookmark> getBookmarkUsingReference(@NonNull final String references) {
        return SbDatabase.getInstance(getApplication())
                         .getBookmarkDao()
                         .getBookmarkUsingReference(references).getValue();
    }
}
