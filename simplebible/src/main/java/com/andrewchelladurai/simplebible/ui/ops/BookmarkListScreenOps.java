package com.andrewchelladurai.simplebible.ui.ops;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkListAdapter.BookmarkListViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 26-Aug-2018 @ 2:01 PM.
 */
public interface BookmarkListScreenOps {

    void handleActionBookmarkClick(@NonNull Bookmark bookmark);

    void handleActionDelete(@NonNull Bookmark bookmark);

    void handleActionShare(@NonNull Bookmark bookmark);

    void updateBookmarkHeader(@NonNull Bookmark bookmark,
                              @NonNull BookmarkListViewHolder bookmarkListViewHolder);

    @NonNull
    String getHeaderTemplate();

    @NonNull
    String getDetailTemplate();

    @NonNull
    String getEmptyNoteText();

    @NonNull
    String getVerseDisplayTemplate();

    @NonNull
    List<Bookmark> getCachedBookmarks();
}
