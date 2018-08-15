package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.VerseRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;
import java.util.List;

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

    public boolean updateRepositoryCache(@NonNull final List<Verse> verses) {
        return VerseRepository.getInstance().populateCache(verses);
    }

    public String getSelectedVersesTextToShare(@NonNull final String verseContentTemplate) {
        final StringBuilder verses = new StringBuilder();
        final List<Verse> list = VerseRepository.getInstance().getCachedList();

        for (Verse verse : list) {
            if (verse.isSelected()) {
                verses.append(String.format(
                    verseContentTemplate, verse.getVerse(), verse.getText())).append("\n");
            }
        }
        return verses.toString();
    }

    @Nullable
    public String getSelectedVerseReferences() {
        final List<Verse> list = VerseRepository.getInstance().getCachedList();
        final ArrayList<Verse> selectedList = new ArrayList<>();
        for (Verse verse : list) {
            if (verse.isSelected()) {
                selectedList.add(verse);
            }
        }

        return Utilities.getInstance().createBookmarkReference(selectedList);
    }

    @Nullable
    public Book getBook(final int bookNumber) {
        return Utilities.getInstance().getBookUsingNumber(bookNumber);
    }
}
