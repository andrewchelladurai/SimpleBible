package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.EntityVerse;

public interface ScreenSearchOps {

  void updateSearchResultView(@NonNull EntityVerse verse, @NonNull TextView textView);

  void toggleSelectedActionsView();

}
