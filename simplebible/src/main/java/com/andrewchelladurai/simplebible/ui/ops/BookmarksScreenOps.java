package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.EntityBookmark;

public interface BookmarksScreenOps {

  @IntRange(from = 0)
  int getBookmarkListSize();

  @NonNull
  EntityBookmark getBookmarkAtPosition(@IntRange(from = 0) int position);

  void handleActionEdit(@NonNull EntityBookmark bookmark);

  void handleActionDelete(@NonNull EntityBookmark bookmark);

  void handleActionShare(@NonNull EntityBookmark bookmark);

  void handleActionSelect(@NonNull EntityBookmark bookmark);

  void getFirstVerseOfBookmark(@NonNull EntityBookmark bookmark,
                               @NonNull final TextView verseView,
                               @NonNull final TextView noteView);


}
