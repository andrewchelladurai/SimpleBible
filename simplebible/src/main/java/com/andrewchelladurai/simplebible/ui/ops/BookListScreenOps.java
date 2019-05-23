package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public interface BookListScreenOps {

  void handleClickBookSelection(@NonNull final String bookName);

  String getChapterQuantityString(@IntRange(from = 1) int chapters);

}
