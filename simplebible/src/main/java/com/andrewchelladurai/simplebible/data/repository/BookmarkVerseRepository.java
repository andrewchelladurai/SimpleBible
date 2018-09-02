package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.dao.VerseDao;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.ops.BookmarkVerseRepositoryOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 16-Aug-2018 @ 7:31 PM.
 */
public class BookmarkVerseRepository
    extends AndroidViewModel
    implements BookmarkVerseRepositoryOps {

    private static final String TAG = "BookmarkVerseRepository";
    private static BookmarkVerseRepositoryOps THIS_INSTANCE;

    private List<Verse>        mCacheList = new ArrayList<>();
    private Map<String, Verse> mCacheMap  = new HashMap<>();
    private String mCacheReference;

    public BookmarkVerseRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
    }

    public static BookmarkVerseRepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton instance not initialized");
        }
        return THIS_INSTANCE;
    }

    @Override
    public boolean isCacheEmpty() {
        return ((mCacheReference == null || mCacheReference.isEmpty())
                && (mCacheMap.isEmpty() && mCacheList.isEmpty()));
    }

    @Override
    public boolean isCacheValid(@NonNull final Object... cacheParams) {
        if (isCacheEmpty()) {
            Log.d(TAG, "empty cache");
            return false;
        }

        return mCacheReference.equalsIgnoreCase((String) cacheParams[0]);
    }

    @Override
    public void clearCache() {
        mCacheReference = null;
        mCacheList.clear();
        mCacheMap.clear();
    }

    @Override
    public int getCacheSize() {
        return (mCacheList.size() == mCacheMap.size()) ? mCacheList.size() : -1;
    }

    @Override
    public boolean populateCache(final List<Verse> list, final Object... cacheParams) {
        if (isCacheValid(cacheParams)) {
            Log.d(TAG, "already cached [" + mCacheReference + "]");
            return true;
        }

        clearCache();
        for (Verse verse : list) {
            mCacheList.add(verse);
            mCacheMap.put(verse.getReference(), verse);
        }

        mCacheReference = (String) cacheParams[0];
        Log.d(TAG, "updated cache with [" + getCacheSize()
                   + "] verses for reference[" + mCacheReference + "]");
        return !isCacheEmpty();
    }

    @Override
    public List<Verse> getCachedRecords() {
        return mCacheList;
    }

    @Override
    public LiveData<List<Verse>> queryBookmarkVerses(@NonNull final Object... cacheParams) {
        if (isCacheValid(cacheParams)) {
            Log.d(TAG, "returning cached live data for [" + mCacheReference + "]");
            return null;
        }

        mCacheReference = (String) cacheParams[0];
        final Utilities utilities = Utilities.getInstance();
        boolean valid = utilities.isValidBookmarkReference(mCacheReference);
        if (!valid) {
            throw new UnsupportedOperationException(
                TAG + ": queryDatabase: invalid bookmark reference [" + mCacheReference
                + "]");
        }

        final String[] references = utilities.splitReferences(mCacheReference);
        int[] parts;

        List<Integer> bookList = new ArrayList<>();
        List<Integer> chapterList = new ArrayList<>();
        List<Integer> verseList = new ArrayList<>();

        for (final String reference : references) {
            if (!utilities.isValidReference(reference)) {
                throw new UnsupportedOperationException(
                    TAG + "queryDatabase: invalid verse reference [" + reference
                    + "] as part of bookmark reference");
            }
            parts = utilities.splitReference(reference);
            bookList.add(parts[0]);
            chapterList.add(parts[1]);
            verseList.add(parts[2]);
        }

        final VerseDao verseDao = SbDatabase.getInstance(getApplication()).getVerseDao();

        references[0] = mCacheReference;
        clearCache();
        Log.d(TAG, "retrieved verses for reference [" + references[0] + "]");
        return verseDao.getRecordsContainingKey(bookList, chapterList, verseList);
    }

}
