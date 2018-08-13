package com.andrewchelladurai.simplebible.ui.ops;

import android.view.View;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 13-Aug-2018 @ 1:16 AM.
 */
public interface ChapterScreenOps
    extends Toolbar.OnMenuItemClickListener, View.OnClickListener {

    void handleInteractionClickVerseItem(@NonNull Verse verse);
}
