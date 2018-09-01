package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.SbDatabase;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.repository.ops.BookmarkRepositoryOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:57 PM.
 */
public class BookmarkRepository
    extends AndroidViewModel
    implements BookmarkRepositoryOps {

    private static final String TAG = "BookmarkRepository";
    private static BookmarkRepositoryOps THIS_INSTANCE;

    private final List<Bookmark>            mCacheList = new ArrayList<>();
    private final HashMap<String, Bookmark> mCacheMap  = new HashMap<>();

    public BookmarkRepository(final Application application) {
        super(application);
        THIS_INSTANCE = this;
        Log.d(TAG, "BookmarkRepository: initialized");
    }

    public static BookmarkRepositoryOps getInstance() {
        if (THIS_INSTANCE == null) {
            throw new UnsupportedOperationException("Singleton Instance is not yet initiated");
        }
        return THIS_INSTANCE;
    }

    @Override
    public void createBookmark(@NonNull final Bookmark bookmark) {
        SbDatabase.getInstance(getApplication()).getBookmarkDao()
                  .createBookmark(bookmark);
    }

    @Override
    @Nullable
    public LiveData<List<Bookmark>> queryBookmarkUsingReference(@NonNull final String reference) {
        return SbDatabase.getInstance(getApplication()).getBookmarkDao()
                         .queryBookmarkUsingReference(reference);
    }

    @Override
    @Nullable
    public LiveData<List<Bookmark>> queryBookmarkUsingNote(@NonNull final String note) {
        return SbDatabase.getInstance(getApplication()).getBookmarkDao()
                         .queryBookmarkUsingNote(note);
    }

    @Override
    @Nullable
    public Bookmark getBookmarkUsingReference(@NonNull final String reference) {
        if (!isCacheValid()) {
            throw new UnsupportedOperationException(
                TAG + " getBookmarkUsingReference: cache is inValid");
        }
        return mCacheMap.get(reference);
    }

    @Override
    @Nullable
    public Bookmark getBookmarkUsingNote(@NonNull final String note) {
        if (!isCacheValid()) {
            throw new UnsupportedOperationException(
                TAG + " getBookmarkUsingReference: cache is inValid");
        }
        for (final Bookmark bookmark : mCacheList) {
            if (bookmark.getNote().contains(note)) {
                return bookmark;
            }
        }
        return null;
    }

    @Override
    public void updateBookmark(@NonNull final Bookmark bookmark) {
        SbDatabase.getInstance(getApplication()).getBookmarkDao()
                  .updateBookmark(bookmark);
    }

    @Override
    public void deleteBookmark(@NonNull final Bookmark bookmark) {
        SbDatabase.getInstance(getApplication()).getBookmarkDao()
                  .deleteBookmark(bookmark);
    }

    @Override
    @Nullable
    public LiveData<List<Bookmark>> queryAllBookmarks() {
        return SbDatabase.getInstance(getApplication()).getBookmarkDao()
                         .queryAllBookmarks();
    }

    @Override
    public int getNumberOfBookmarks() {
        return SbDatabase.getInstance(getApplication()).getBookmarkDao()
                         .getNumberOfBookmarks();
    }

    @Override
    public void deleteAllRecords() {
        SbDatabase.getInstance(getApplication()).getBookmarkDao()
                  .deleteAllRecords();
    }

    @Override
    public boolean populateCache(@NonNull final List<Bookmark> list) {
        clearCache();
        Bookmark bookmark;
        for (final Object object : list) {
            bookmark = (Bookmark) object;
            mCacheList.add(bookmark);
            mCacheMap.put(bookmark.getReference(), bookmark);
        }
        Log.d(TAG, "populateCache: cached [" + getCachedRecordCount() + "] bookmarks");
        return true;
    }

    @Override
    public boolean isCacheValid() {
        return mCacheList.isEmpty() && mCacheMap.isEmpty();
    }

    @Override
    public int getCachedRecordCount() {
        return (mCacheList.size() == mCacheMap.size()) ? mCacheList.size() : -1;
    }

    public void clearCache() {
        mCacheList.clear();
        mCacheMap.clear();
    }

    @Override
    public List<Bookmark> getCachedRecords() {
        return mCacheList;
    }

}
