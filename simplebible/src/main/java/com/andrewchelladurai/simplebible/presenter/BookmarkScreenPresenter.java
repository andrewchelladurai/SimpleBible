package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.BookmarkVerseRepository;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:39 PM.
 */
public class BookmarkScreenPresenter {

    private static final String TAG = "BookmarkScreenPresenter";
    private final BookmarkScreenOps mOps;

    public BookmarkScreenPresenter(@NonNull BookmarkScreenOps ops) {
        mOps = ops;
    }

    public boolean populateCache(final List<Verse> verses, final String references) {
        return BookmarkVerseRepository.getInstance().populateCache(verses, references);
    }

/*
    @Nullable
    public List<Verse> getBookmarkVerses(@Nullable final String references) {
        Log.d(TAG, "getBookmarkVerses called with: [" + references + "]");
        final Utilities utilities = Utilities.getInstance();

        boolean valid = utilities.isValidBookmarkReference(references);
        if (!valid) {
            Log.e(TAG, "getBookmarkVerses: invalid bookmark reference");
            return null;
        }

        final String[] verseReferences = utilities.splitReferences(references);
        valid = verseReferences.length > 0;
        if (!valid) {
            Log.e(TAG, "getBookmarkVerses: less than Zero references");
            return null;
        }

        final BookmarkVerseRepository verseRepository = BookmarkVerseRepository.getInstance();
        if (verseRepository.isCacheValid(references)) {
            return verseRepository.getCachedList();
        }
        return verseRepository.queryDatabase(references).getValue();
    }

    public void populateCache(final List<Verse> verses, final String references) {
        BookmarkVerseRepository.getInstance().populateCache(verses);
    }

    public List<Verse> getCachedList() {
        return BookmarkVerseRepository.getInstance().getCachedList();
    }

    @Nullable
    public List<Bookmark> isBookmarkPresent(final String references) {
        if (references.isEmpty()) {
            Log.e(TAG, "isBookmarkPresent: empty references");
            return null;
        }

        final LiveData<List<Bookmark>> liveData =
            BookmarkRepository.getInstance().queryDatabase(references);

        if (liveData == null || liveData.getValue() == null || liveData.getValue().isEmpty()) {
            Log.e(TAG, "isBookmarkPresent: no bookmarks for reference found");
            return null;
        }

        return liveData.getValue();
    }

    public void clearRepository() {
        BookmarkRepository.getInstance().clearCache();
    }

    public void getReferenceVerses(@NonNull final Context context,
                                   @NonNull final String bookmarkReferences) {
        Log.d(TAG, "getReferenceVerses: references = [" + bookmarkReferences + "]");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Utilities utilities = Utilities.getInstance();
                final String[] verseReferences = utilities.splitReferences(bookmarkReferences);
                final ArrayList<Verse> verses = new ArrayList<>();
                boolean validReference;
                int[] parts;
                LiveData<Verse> verse;
                for (String reference : verseReferences) {
                    validReference = utilities.isValidReference(reference);
                    if (validReference) {
                        parts = utilities.splitReference(reference);
                        verse = SbDatabase.getInstance(context).getVerseDao()
                                          .getVerse(parts[0], parts[1], parts[2]);
                        Log.d(TAG, "run: verse is null" + (verse == null));
                        if (verse != null) {
                            verses.add(verse.getValue());
                            Log.d(TAG, "run: verse added " + verse.getValue().getReference());
                        }
                    } else {
                        Log.d(TAG, "loadInBackground: invalid reference[" + reference + "]");
                    }
                }
                BookmarkRepository.populateList(verses);
            }
        }).start();
    }

    public String formatBookmarkToShare(@NonNull final String note,
                                        @NonNull final String bookmarkVerseTemplate,
                                        @NonNull final String bookmarkShareTemplate) {
        final List<Verse> list = BookmarkRepository.getInstance().getCachedList();
        final StringBuilder verses = new StringBuilder();
        final Utilities utilities = Utilities.getInstance();

        for (Verse verse : list) {
            verses.append(
                String.format(bookmarkVerseTemplate, utilities.getBookName(verse.getBook()),
                              verse.getChapter(), verse.getVerse(), verse.getText()))
                  .append("\n");
        }
        return String.format(bookmarkShareTemplate, verses, note);
    }

    public boolean saveBookmark(@NonNull final String references, @NonNull final String note) {
        if (!Utilities.getInstance().isValidBookmarkReference(references)) {
            Log.e(TAG, "saveBookmark: invalid bookmark reference");
            return false;
        }

        return BookmarkRepository.getInstance().createBookmark(references, note);
    }

    public boolean deleteBookmark(final String references, final String note) {
        if (!Utilities.getInstance().isValidBookmarkReference(references)) {
            Log.e(TAG, "deleteBookmark: invalid bookmark reference");
            return false;
        }

        return BookmarkRepository.getInstance().deleteBookmark(references, note);
    }
*/

}
