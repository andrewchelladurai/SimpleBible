package com.andrewchelladurai.simplebible.presenter;

import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkListScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 26-Aug-2018 @ 2:01 PM.
 */
public class BookmarkListScreenPresenter {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String TAG = "BookmarkListScreenPrese";

    private final BookmarkListScreenOps mOps;

    public BookmarkListScreenPresenter(final BookmarkListScreenOps ops) {
        mOps = ops;
    }

    public int getVerseCount(@NonNull final Bookmark bookmark) {
        String[] references = Utilities.getInstance().splitReferences(bookmark.getReference());
        if (references != null) {
            return references.length;
        }

        Log.e(TAG, "getVerseCount: invalid references in passed bookmark");
        return -1;
    }

    @NonNull
    public String getFirstVerse(@NonNull final Bookmark bookmark,
                                @NonNull final String verseDisplayTemplate) {

/*
        Verse verse;
        String.format(
            verseDisplayTemplate,
            Utilities.getInstance().getBookName(verse.getBook()),
            verse.getChapter(),
            verse.getVerse(),
            verse.getText());
*/

        return bookmark.getReference();
    }
}
