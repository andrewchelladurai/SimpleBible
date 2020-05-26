package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.model.Verse;

public interface ChapterScreenOps {

  int getCachedListSize();

  @Nullable
  Verse getVerseAtPosition(@IntRange(from = 0) int position);

  boolean isVerseSelected(@NonNull Verse verse);

  void removeSelectedVerse(@NonNull Verse verse);

  void addSelectedVerse(@NonNull Verse verse);

  void updateSelectionActionsVisibility();

  void handleNewChapterSelection(@IntRange(from = 1) int newChapter);

}
