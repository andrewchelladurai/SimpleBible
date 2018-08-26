package com.andrewchelladurai.simplebible.ui.ops;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 26-Aug-2018 @ 2:01 PM.
 */
public interface BookmarkListScreenOps
    extends Observer<List<Bookmark>> {

    @NonNull
    String getFormattedBookmarkHeader(@NonNull Bookmark bookmark);

    @NonNull
    String getFormattedBookmarkDetails(@NonNull Bookmark bookmark);

    void handleActionBookmarkClick(@NonNull Bookmark bookmark);

    void handleActionDelete(@NonNull Bookmark bookmark);

    void handleActionShare(@NonNull Bookmark bookmark);
}
