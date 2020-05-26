package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.model.Verse;

public interface SearchScreenOps {

  void updateSelectionActionsState();

  boolean isSelected(@NonNull String verseReference);

  void removeSelection(@NonNull String verseReference);

  void addSelection(@NonNull String verseReference, @NonNull Verse verse);

  @NonNull
  Verse getVerseAtPosition(@IntRange(from = 0) int position);

  @IntRange(from = 0)
  int getResultCount();

}
