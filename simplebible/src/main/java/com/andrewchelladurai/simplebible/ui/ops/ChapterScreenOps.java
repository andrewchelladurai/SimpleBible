package com.andrewchelladurai.simplebible.ui.ops;

import android.content.Context;
import android.view.View;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 13-Aug-2018 @ 1:16 AM.
 */
public interface ChapterScreenOps
    extends Toolbar.OnMenuItemClickListener, View.OnClickListener, Observer<List<Verse>> {

    void handleInteractionVerseClicked(@NonNull Verse verse);

    @NonNull
    String getVerseTemplateString();

    @NonNull
    Context getSystemContext();

    @IntRange(from = 1, to = 66)
    int getBook();

    @IntRange(from = 1)
    int getChapter();

    void handleInteractionChapterClicked(@IntRange(from = 1) int chapterNumber);
}
