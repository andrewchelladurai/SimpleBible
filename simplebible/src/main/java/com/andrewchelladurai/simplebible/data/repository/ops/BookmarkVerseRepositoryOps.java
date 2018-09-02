package com.andrewchelladurai.simplebible.data.repository.ops;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 01-Sep-2018 @ 9:48 PM.
 */
public interface BookmarkVerseRepositoryOps {

    boolean isCacheEmpty();

    boolean isCacheValid(@NonNull final Object... cacheParams);

    void clearCache();

    int getCacheSize();

    boolean populateCache(@NonNull final List<Verse> list,
                          @NonNull final Object... cacheParams);

    @NonNull
    List<Verse> getCachedRecords();

    LiveData<List<Verse>> queryBookmarkVerses(@NonNull final Object... cacheParams);

}
