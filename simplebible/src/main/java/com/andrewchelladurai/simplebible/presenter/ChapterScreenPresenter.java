package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.VerseRepository;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 13-Aug-2018 @ 1:17 AM.
 */
public class ChapterScreenPresenter {

    private final ChapterScreenOps mOps;

    public ChapterScreenPresenter(final ChapterScreenOps ops) {
        mOps = ops;
    }

    public boolean populateCache(@NonNull final List<Verse> verses,
                                 @IntRange(from = 1, to = 66) final int book,
                                 @IntRange(from = 1) final int chapter) {
        return VerseRepository.getInstance().populateCache(verses, book, chapter);
    }

    public String getSelectedVersesTextToShare(@NonNull final ArrayList<Verse> list,
                                               @NonNull final String verseContentTemplate) {
        final StringBuilder verses = new StringBuilder();

        for (Verse verse : list) {
            if (verse.isSelected()) {
                verses.append(String.format(
                    verseContentTemplate, verse.getVerse(), verse.getText())).append("\n");
            }
        }
        return verses.toString();
    }

    @Nullable
    public Book getBook(final int bookNumber) {
        return Utilities.getInstance().getBookUsingNumber(bookNumber);
    }

    public void destroyCache() {
        VerseRepository.getInstance().clearCache();
    }

    @Nullable
    public String getSelectedVerseReferences(final ArrayList<Verse> selectedVerses) {
        return Utilities.getInstance().createBookmarkReference(selectedVerses);
    }
}
