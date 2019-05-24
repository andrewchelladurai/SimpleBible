package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.NonNull;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

public interface ChapterScreenOps {

  void toggleActionButtons();

  void updateCache(@NonNull List<?> newList);

  @NonNull
  List<?> getCache();

  @NonNull
  Object getCachedItemAt(int position);

  int getCacheSize();

  boolean isSelected(@NonNull Verse verse);

  void addSelection(@NonNull Verse verse);

  void addSelection(@NonNull String text);

  void removeSelection(@NonNull Verse verse);

  void removeSelection(@NonNull String text);

}
