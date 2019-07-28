package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;

public interface ScreenChapterOps {

  void handleClickChapter(@IntRange(from = 1) int chapterNumber);

}
