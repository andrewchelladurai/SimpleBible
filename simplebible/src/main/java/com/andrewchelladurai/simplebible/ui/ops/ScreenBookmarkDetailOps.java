package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.VerseEntity;

public interface ScreenBookmarkDetailOps {

  void updateBookmarkVerseView(@NonNull VerseEntity verse, @NonNull TextView textView);

}
