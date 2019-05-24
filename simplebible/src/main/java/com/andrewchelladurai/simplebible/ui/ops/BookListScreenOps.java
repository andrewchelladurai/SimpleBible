package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public interface BookListScreenOps {

  void handleClickBookSelection(@NonNull final String bookName);

  @NonNull
  String getContentString(@IntRange(from = 1) int verses, @IntRange(from = 1) int chapters);

}
