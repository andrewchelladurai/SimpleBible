package com.andrewchelladurai.simplebible.ui.ops;

import android.content.Context;
import android.view.View;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:35 PM.
 */
public interface BookmarkScreenOps
    extends Toolbar.OnMenuItemClickListener, View.OnClickListener, Observer<List<Verse>> {

    void handleInteractionClick(@NonNull Verse verse);

    @NonNull
    Context getSystemContent();

    @NonNull
    String getReferences();

    @NonNull
    String getNote();

    void handleInteractionSave();

    void handleInteractionEdit();

    void handleInteractionDelete();

    void handleInteractionShare();

    void handleInteractionSettings();

    void showMessageSaved();

    void showErrorSaveFailed();

    void showErrorDeleteFailed();

    void showMessageDeleted();
}
