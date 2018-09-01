package com.andrewchelladurai.simplebible.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.BookmarkRepository;
import com.andrewchelladurai.simplebible.data.repository.BookmarkVerseRepository;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:39 PM.
 */
public class BookmarkScreenPresenter {

    private static final String TAG = "BookmarkScreenPresenter";
    private static BookmarkScreenOps mOps;

    public BookmarkScreenPresenter(@NonNull BookmarkScreenOps ops) {
        mOps = ops;
    }

    public boolean populateCache(final List<Verse> verses, final String references) {
        return BookmarkVerseRepository.getInstance().populateCache(verses, references);
    }

    @Nullable
    public Bookmark getBookmarkUsingReference(@NonNull final String references) {
        return (Bookmark) BookmarkRepository.getInstance().getCachedRecordUsingKey(references);
    }

    @NonNull
    public String formatBookmarkToShare(@NonNull final String note,
                                        @NonNull final String bookmarkVerseTemplate,
                                        @NonNull final String bookmarkShareTemplate) {
        final List<?> list = BookmarkVerseRepository.getInstance().getCachedList();
        final StringBuilder verses = new StringBuilder();
        final Utilities utilities = Utilities.getInstance();

        Verse verse;
        for (Object object : list) {
            verse = (Verse) object;
            verses.append(String.format(bookmarkVerseTemplate,
                                        utilities.getBookName(verse.getBook()),
                                        verse.getChapter(),
                                        verse.getVerse(),
                                        verse.getText()))
                  .append("\n");
        }
        return String.format(bookmarkShareTemplate, verses, note);
    }

    public void createBookmark(@NonNull final String references, @NonNull final String note) {
        if (!Utilities.getInstance().isValidBookmarkReference(references)) {
            Log.e(TAG, "saveBookmark: invalid bookmark reference");
            mOps.showErrorSaveFailed();
        }

        new CreateBookmarkTask().execute(new Bookmark(references, note));
    }

    public void deleteBookmark(final String references, final String note) {
        if (!Utilities.getInstance().isValidBookmarkReference(references)) {
            mOps.showErrorDeleteFailed();
        }

        new DeleteBookmarkTask().execute(new Bookmark(references, note));
    }

    public void destroyCache() {
        BookmarkVerseRepository.getInstance().clearCache();
    }

    public LiveData<List<Bookmark>> doesBookmarkExist(final String bookmarkReference) {
        return BookmarkRepository.getInstance().doesRecordExist(bookmarkReference);
    }

    private static class CreateBookmarkTask
        extends AsyncTask<Bookmark, Void, Boolean> {

        private static final String TAG = "CreateBookmarkTask";

        @Override
        protected Boolean doInBackground(final Bookmark... bookmarks) {
            Log.d(TAG, "doInBackground");
            return BookmarkRepository.getInstance().createRecord(bookmarks[0]);
        }

        @Override
        protected void onPostExecute(final Boolean createdBookmark) {
            if (createdBookmark) {
                mOps.showMessageSaved();
            } else {
                mOps.showErrorSaveFailed();
            }
        }
    }

    private static class DeleteBookmarkTask
        extends AsyncTask<Bookmark, Void, Boolean> {

        private static final String TAG = "DeleteBookmarkTask";

        @Override
        protected Boolean doInBackground(final Bookmark... bookmarks) {
            Log.d(TAG, "doInBackground");
            return BookmarkRepository.getInstance().deleteRecord(bookmarks[0]);
        }

        @Override
        protected void onPostExecute(final Boolean deletedBookmark) {
            if (deletedBookmark) {
                mOps.showMessageDeleted();
            } else {
                mOps.showErrorDeleteFailed();
            }
        }
    }

}
