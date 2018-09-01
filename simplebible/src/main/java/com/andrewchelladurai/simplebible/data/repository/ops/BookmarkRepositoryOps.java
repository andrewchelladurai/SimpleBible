package com.andrewchelladurai.simplebible.data.repository.ops;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 01-Sep-2018 @ 8:37 PM.
 */
public interface BookmarkRepositoryOps {

    void createBookmark(@NonNull Bookmark bookmark);

    LiveData<List<Bookmark>> queryBookmarkUsingReference(@NonNull String reference);

    LiveData<List<Bookmark>> queryBookmarkUsingNote(@NonNull String note);

    Bookmark getBookmarkUsingReference(@NonNull String reference);

    Bookmark getBookmarkUsingNote(@NonNull String note);

    void updateBookmark(@NonNull Bookmark bookmark);

    void deleteBookmark(@NonNull Bookmark bookmark);

    LiveData<List<Bookmark>> getAllBookmarks();

    int getNumberOfBookmarks();

    void deleteAllRecords();

    boolean populateCache(@NonNull List<Bookmark> list);

    public boolean isCacheValid();

    public int getCachedRecordCount();

    public void clearCache();
}
