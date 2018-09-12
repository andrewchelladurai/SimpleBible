package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.ops.VerseRepositoryOps;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 13-Aug-2018 @ 1:17 AM.
 */
public class ChapterScreenPresenter {

    private final ChapterScreenOps   mOps;
    private final VerseRepositoryOps mRepositoryOps;

    public ChapterScreenPresenter(final ChapterScreenOps ops,
                                  final VerseRepositoryOps repositoryOps) {
        mOps = ops;
        mRepositoryOps = repositoryOps;
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

    @Nullable
    public String getSelectedVerseReferences(final ArrayList<Verse> selectedVerses) {
        return Utilities.getInstance().createBookmarkReference(selectedVerses);
    }
}
