package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;

public interface ScreenBookListOps {

  String getFormattedBookDetails(@IntRange(from = 1) int chapterCount);

}
