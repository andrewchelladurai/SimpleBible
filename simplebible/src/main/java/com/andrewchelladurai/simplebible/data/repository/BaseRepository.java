package com.andrewchelladurai.simplebible.data.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 11-Aug-2018 @ 7:31 PM.
 */
abstract class BaseRepository
    extends AndroidViewModel
    implements RepositoryOps {

    BaseRepository(final Application application) {
        super(application);
    }

    abstract boolean isCacheValid(@NonNull Object... cacheParams);
}
