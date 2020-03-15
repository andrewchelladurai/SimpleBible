package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.VerseEntity;

public interface ScreenSearchOps {

  void updateSearchResultView(@NonNull VerseEntity verse, @NonNull TextView textView);

  void toggleSelectedActionsView();

}
