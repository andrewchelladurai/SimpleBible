package com.andrewchelladurai.simplebible.data.repository.ops;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 02-Sep-2018 @ 11:52 AM.
 */
public interface SearchRepositoryOps {

    @NonNull
    LiveData<List<Verse>> queryDatabase(@NonNull final Object... cacheParams);

    boolean isCacheValid(@NonNull final Object... cacheParams);

    boolean isCacheEmpty();

    int getCacheSize();

    void clearCache();

    boolean populateCache(@NonNull final List<Verse> list,
                          @NonNull final Object... cacheParams);

    @NonNull
    List<Verse> getCachedList();
}
