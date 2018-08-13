package com.andrewchelladurai.simplebible.ui.ops;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 13-Aug-2018 @ 1:16 AM.
 */
public interface ChapterScreenOps {

    void handleInteractionClickVerseItem(@NonNull Verse verse);
}
