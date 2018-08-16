package com.andrewchelladurai.simplebible.ui.ops;

import android.content.Context;
import android.view.View;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:35 PM.
 */
public interface BookmarkScreenOps
    extends View.OnClickListener {

    void handleInteractionClick(@NonNull Verse verse);

    @NonNull
    Context getSystemContent();

    @NonNull
    String getReferences();

    @NonNull
    String getNote();

    void handleClickButSave();

    void handleClickButEdit();

    void handleClickButDelete();

    void handleClickButShare();

    void handleClickButReset();

    void handleClickButSettings();

    void showMessageSaved();

    void showErrorSaveFailed();

    void showErrorDeleteFailed();

    void showMessageDeleted();
}
