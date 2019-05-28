package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.List;

public interface BookmarkScreenOps {

  void refreshList(final @NonNull List<?> newList);

  @NonNull
  List<?> getCachedList();

  @NonNull
  Object getCachedItemAt(int position);

  int getCachedListSize();

  void showContent(@NonNull TextView textView, @NonNull Verse verse);

}
