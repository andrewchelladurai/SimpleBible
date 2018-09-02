package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.ops.SearchRepositoryOps;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 24-Aug-2018 @ 11:08 PM.
 */
public class SearchScreenPresenter {

    private final SearchScreenOps     mOps;
    private final SearchRepositoryOps mRepositoryOps;

    public SearchScreenPresenter(final SearchScreenOps ops,
                                 final SearchRepositoryOps repositoryOps) {
        mOps = ops;
        mRepositoryOps = repositoryOps;
    }

    public boolean validateSearchText(final String searchText) {
        if (searchText.isEmpty()) {
            mOps.showErrorEmptySearchText();
            return false;
        }

        if (searchText.length() < 3) {
            mOps.showErrorMinLimit();
            return false;
        }

        if (searchText.length() > 50) {
            mOps.showErrorMaxLimit();
            return false;
        }

        return true;
    }

    public boolean populateCache(@NonNull final List<Verse> list,
                                 @NonNull final String searchText) {
        return mRepositoryOps.populateCache(list, searchText);
    }

    public String getSelectedVersesTextToShare(@NonNull final ArrayList<Verse> list,
                                               @NonNull final String verseContentTemplate) {
        final StringBuilder verses = new StringBuilder();
        String bookName, verseText;
        int chapterNumber, verseNumber;

        final Utilities utilities = Utilities.getInstance();
        for (Verse verse : list) {
            if (verse.isSelected()) {
                bookName = utilities.getBookName(verse.getBook());
                chapterNumber = verse.getChapter();
                verseNumber = verse.getVerse();
                verseText = verse.getText();

                verses.append(String.format(verseContentTemplate,
                                            bookName, chapterNumber, verseNumber, verseText))
                      .append("\n");
            }
        }

        return verses.toString();
    }

    @Nullable
    public String getSelectedVerseReferences(final ArrayList<Verse> selectedVerses) {
        return Utilities.getInstance().createBookmarkReference(selectedVerses);
    }
}
