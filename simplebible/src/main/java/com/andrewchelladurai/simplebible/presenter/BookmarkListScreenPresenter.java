package com.andrewchelladurai.simplebible.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.VerseRepository;
import com.andrewchelladurai.simplebible.data.repository.ops.BookmarkRepositoryOps;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkListAdapter.BookmarkListViewHolder;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkListScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;
import java.util.List;

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
    private final BookmarkRepositoryOps mRepositoryOps;

    public BookmarkListScreenPresenter(final BookmarkListScreenOps ops,
                                       final BookmarkRepositoryOps repositoryOps) {
        mOps = ops;
        mRepositoryOps = repositoryOps;
    }

    public int getVerseCount(@NonNull final Bookmark bookmark) {
        String[] references = Utilities.getInstance().splitReferences(bookmark.getReference());
        if (references != null) {
            return references.length;
        }

        Log.e(TAG, "getVerseCount: invalid references in passed bookmark");
        return -1;
    }

    public void updateBookmarkHeader(@NonNull final Bookmark bookmark,
                                     @NonNull final BookmarkListViewHolder bookmarkListViewHolder) {
        if (bookmark == null || bookmarkListViewHolder == null) {
            throw new UnsupportedOperationException(
                TAG + "updateBookmarkHeader: Null params passed");
        }

        if (!Utilities.getInstance().isValidBookmarkReference(bookmark.getReference())) {
            Log.e(TAG,
                  "updateBookmarkHeader: splitting [bookmarkReference = "
                  + bookmark.getReference() + "] failed");
            return;
        }

        new GetBookmarkVerses(bookmarkListViewHolder).execute(bookmark.getReference());
    }

    public List<Bookmark> getCachedRecords() {
        return mRepositoryOps.getCachedRecords();
    }

    public boolean populateRepositoryCache(final List<Bookmark> list) {
        return mRepositoryOps.populateCache(list);
    }

    private static class GetBookmarkVerses
        extends AsyncTask<String, Void, ArrayList<Verse>> {

        private final BookmarkListViewHolder mBookmarkListViewHolder;

        GetBookmarkVerses(@NonNull final BookmarkListViewHolder bookmarkListViewHolder) {
            mBookmarkListViewHolder = bookmarkListViewHolder;
        }

        @Override
        protected ArrayList<Verse> doInBackground(final String... params) {
            final String bookmarkReference = params[0];
            Log.d(TAG, "doInBackground: passed bookmarkReference[" + bookmarkReference + "]");

            final ArrayList<Verse> verseList = new ArrayList<>();
            final Utilities utilities = Utilities.getInstance();
            final String[] verseReferences = utilities.splitReferences(bookmarkReference);
            final VerseRepository verseRepository = VerseRepository.getInstance();
            int[] parts;
            List<Verse> list;

            for (final String reference : verseReferences) {
                if (!utilities.isValidReference(reference)) {
                    Log.e(TAG, "doInBackground: invalid verse reference [" + reference + "]");
                    continue;
                }

                parts = utilities.splitReference(reference);
                list = verseRepository.getVerse(parts[0], parts[1], parts[2]);
                if (list == null || list.isEmpty()) {
                    Log.e(TAG, "doInBackground: no verse found for [reference=" + reference + "]");
                    continue;
                }

                verseList.add(list.get(0));
            }

            Log.d(TAG, "doInBackground() returned [" + verseList.size() + "] verses ");

            return verseList;
        }

        @Override
        protected void onPostExecute(final ArrayList<Verse> list) {
            if (list == null || list.isEmpty()) {
                Log.e(TAG, "onPostExecute: no verses found for passed reference");
            }
            mBookmarkListViewHolder.updateBookmarkHeader(list);
        }
    }

}
