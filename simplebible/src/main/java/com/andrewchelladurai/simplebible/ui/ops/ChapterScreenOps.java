package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewchelladurai.simplebible.data.entities.EntityVerse;

public interface ChapterScreenOps {

  int getCachedListSize();

  @Nullable
  EntityVerse getVerseAtPosition(@IntRange(from = 0) int position);

  boolean isVerseSelected(@NonNull EntityVerse verse);

  void removeSelectedVerse(@NonNull EntityVerse verse);

  void addSelectedVerse(@NonNull EntityVerse verse);

  void updateSelectionActionsVisibility();

  void handleNewChapterSelection(@IntRange(from = 1) int newChapter);

}
