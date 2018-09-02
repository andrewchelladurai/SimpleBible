package com.andrewchelladurai.simplebible.ui.ops;

import android.text.TextWatcher;
import android.view.View;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 24-Aug-2018 @ 11:03 PM.
 */
public interface SearchScreenOps
    extends View.OnClickListener, Toolbar.OnMenuItemClickListener, TextWatcher {

    @NonNull
    String getSearchVerseTemplateString();

    void actionVerseClicked(@NonNull Verse verse);

    void showErrorEmptySearchText();

    void showErrorMinLimit();

    void showErrorMaxLimit();
}
