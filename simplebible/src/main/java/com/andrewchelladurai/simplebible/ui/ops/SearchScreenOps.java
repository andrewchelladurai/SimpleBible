package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.EntityVerse;

public interface SearchScreenOps {

  void updateSelectionActionsState();

  boolean isSelected(@NonNull String verseReference);

  void removeSelection(@NonNull String verseReference);

  void addSelection(@NonNull String verseReference, @NonNull EntityVerse verse);

  @NonNull
  EntityVerse getVerseAtPosition(@IntRange(from = 0) int position);

  @IntRange(from = 0)
  int getResultCount();

}
