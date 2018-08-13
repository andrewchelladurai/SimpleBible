package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.VerseRepository;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;

import java.util.List;

import androidx.annotation.IntRange;
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

    public boolean isChapterCached(@IntRange(from = 1, to = 66) final int bookNumber,
                                   @IntRange(from = 1) final int chapterNumber) {
        return VerseRepository.getInstance().validate(bookNumber, chapterNumber);
    }

    public void updateRepositoryValues(@IntRange(from = 1, to = 66) final int bookNumber,
                                       @IntRange(from = 1) final int chapterNumber) {
        VerseRepository.getInstance().setUpNewChapter(bookNumber, chapterNumber);
    }

    public boolean updateRepositoryCache(@NonNull final List<Verse> verses) {
        return VerseRepository.getInstance().populate(verses);
    }
}
