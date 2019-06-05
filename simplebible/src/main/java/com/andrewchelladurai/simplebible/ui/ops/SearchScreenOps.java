package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import java.util.List;

public interface SearchScreenOps {

  void toggleActionButtons();

  void showContent(@NonNull TextView textView, @NonNull Verse verse);

  void refreshCachedList(@NonNull List<?> list);

  @NonNull
  List<?> getCachedList();

  @NonNull
  Object getCachedItemAt(int position);

  int getCachedListSize();

  void addSelection(@NonNull Verse verse);

  void addSelection(@NonNull String text);

  void removeSelection(@NonNull Verse verse);

  void removeSelection(@NonNull String text);

  boolean isSelected(@NonNull Verse verse);

  void clearSelection();

}
