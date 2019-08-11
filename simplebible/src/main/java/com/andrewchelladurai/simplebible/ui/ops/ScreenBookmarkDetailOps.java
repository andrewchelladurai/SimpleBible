package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.Verse;

public interface ScreenBookmarkDetailOps {

  void updateBookmarkVerseView(@NonNull Verse verse, @NonNull TextView textView);

}
