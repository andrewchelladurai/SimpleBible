package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

public interface SearchScreenOps {

  void showFormattedResultContent(@NonNull TextView textView, @NonNull Verse verse);

  @NonNull
  List<?> getAdapterList();

  @NonNull
  Object getAdapterItemAt(int position);

  void addToAdapterList(@NonNull Object object);

  int getAdapterListSize();

  void clearAdapterList();

  void addSelectedResult(@NonNull Verse verse);

  void removeSelectedText(@NonNull String text);

  boolean isResultSelected(@NonNull Verse verse);

  void addSelectedText(@NonNull String text);

  void removeSelectedResult(@NonNull Verse verse);

  void toggleActionButtons();

}
