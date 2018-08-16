package com.andrewchelladurai.simplebible.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 7:31 PM.
 */
interface RepositoryOps {

    boolean populateCache(@NonNull List<?> list);

    void clearCache();

    boolean isCacheEmpty();

    int getCacheSize();

    @Nullable
    Object getCachedRecordUsingKey(@NonNull Object key);

    @Nullable
    Object getCachedRecordUsingValue(@NonNull Object value);

    @Nullable
    List<?> getCachedList();

    @Nullable
    LiveData<?> queryDatabase();

    @Nullable
    LiveData<?> queryDatabase(Object... objects);

    boolean isCacheValid(Object... objects);
}
