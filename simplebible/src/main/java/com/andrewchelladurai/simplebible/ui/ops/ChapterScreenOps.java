package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;

import com.andrewchelladurai.simplebible.data.EntityVerse;

public interface ChapterScreenOps {

  int getCachedListSize();

  EntityVerse getVerseAtPosition(@IntRange(from = 0) int position);

}
