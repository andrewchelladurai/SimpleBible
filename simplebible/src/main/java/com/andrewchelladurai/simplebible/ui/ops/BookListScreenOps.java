package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.List;

public interface BookListScreenOps {

  void handleClickBookSelection(@NonNull final String bookName);

  @NonNull
  String getContentString(@IntRange(from = 1) int verses, @IntRange(from = 1) int chapters);

  void refreshCachedList(final List<?> list);

  @NonNull
  List<?> getCachedList();

  @NonNull
  Object getCachedItemAt(int position);

  int getCachedListSize();

}
