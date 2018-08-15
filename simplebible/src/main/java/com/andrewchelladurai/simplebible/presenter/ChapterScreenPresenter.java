package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.VerseRepository;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;

import java.util.List;

import androidx.annotation.NonNull;

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
}
