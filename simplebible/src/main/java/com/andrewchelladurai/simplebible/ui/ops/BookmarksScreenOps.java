package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.EntityBookmark;
import com.andrewchelladurai.simplebible.data.EntityVerse;

public interface BookmarksScreenOps {

  @IntRange(from = 0)
  int getBookmarkListSize();

  @NonNull
  EntityBookmark getBookmarkAtPosition(@IntRange(from = 0) int position);

  void handleActionEdit(@NonNull EntityBookmark bookmark);

  void handleActionDelete(@NonNull EntityBookmark bookmark);

  void handleActionShare(@NonNull EntityBookmark bookmark);

  void handleActionSelect(@NonNull EntityBookmark bookmark);

  @NonNull
  EntityVerse getFirstVerseOfBookmark(@NonNull EntityBookmark bookmark);


}
