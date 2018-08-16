package com.andrewchelladurai.simplebible.data;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:57 PM.
 */
public class BookmarkRepository
    extends AndroidViewModel
    implements RepositoryOps {

    private static final String                    TAG       = "BookmarkRepository";
    private static final List<Bookmark>            CACHE     = new ArrayList<>();
    private static final HashMap<String, Bookmark> CACHE_MAP = new HashMap<>();
    private static BookmarkRepository THIS_INSTANCE;
    private static LiveData<List<Bookmark>> LIVE_DATA = new MutableLiveData<>();

    public BookmarkRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "BookmarkRepository: initialized");
    }

    public static BookmarkRepository getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    @Override
    public boolean populateCache(final List<?> list) {
        clearCache();
        Bookmark bookmark;
        for (final Object object : list) {
            bookmark = (Bookmark) object;
            CACHE.add(bookmark);
            CACHE_MAP.put(bookmark.getReference(), bookmark);
        }
        Log.d(TAG, "populateCache: updated cache with [" + getCacheSize() + "] records");
        return false;
    }

    @Override
    public void clearCache() {
        CACHE.clear();
        CACHE_MAP.clear();
    }

    @Override
    public boolean isCacheEmpty() {
        return CACHE.isEmpty() && CACHE_MAP.isEmpty();
    }

    @Override
    public int getCacheSize() {
        return (CACHE.size() == CACHE_MAP.size()) ? CACHE.size() : -1;
    }

    @Override
    public Bookmark getCachedRecordUsingKey(final Object key) {
        final String bookmarkReference = (String) key;
        return CACHE_MAP.get(bookmarkReference);
    }

    @Override
    @Nullable
    public Bookmark getCachedRecordUsingValue(final Object value) {
        final String note = (String) value;
        for (final Bookmark bookmark : CACHE) {
            if (bookmark.getNote().equalsIgnoreCase(note)) {
                return bookmark;
            }
        }
        return null;
    }

    @Override
    public List<Bookmark> getCachedList() {
        return CACHE;
    }

    @Override
    public LiveData<List<Bookmark>> queryDatabase() {
        LIVE_DATA = SbDatabase.getInstance(getApplication()).getBookmarkDao().getAllBookmarks();
        return LIVE_DATA;
    }

    @Override
    public LiveData<List<Bookmark>> queryDatabase(final Object... objects) {
        final String bookmarkReference = (String) objects[0];
        if (bookmarkReference.isEmpty()) {
            Log.e(TAG, "queryDatabase: empty reference passed");
            return null;
        }

        return SbDatabase.getInstance(getApplication()).getBookmarkDao()
                         .getBookmarkUsingReference(bookmarkReference);
    }

    @Override
    public boolean isCacheValid(final Object... objects) {
        return isCacheEmpty();
    }
}
