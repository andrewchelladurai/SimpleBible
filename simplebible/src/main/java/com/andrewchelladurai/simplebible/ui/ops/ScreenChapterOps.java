package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.Verse;

public interface ScreenChapterOps {

  void handleClickChapter(@IntRange(from = 1) int chapterNumber);

  void handleClickVerse();

  void updateVerseView(@NonNull Verse verse, TextView verseView);

}
