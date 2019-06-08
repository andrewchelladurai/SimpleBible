package com.andrewchelladurai.simplebible.ui.ops;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;

public interface BookmarkListScreenOps {

  @NonNull
  String getStringValue(@StringRes int stringResId);

  void handleBookmarkActionEdit(@NonNull Bookmark bookmark);

  void handleBookmarkActionDelete(@NonNull Bookmark bookmark);

  void handleBookmarkActionShare(@NonNull Bookmark bookmark);

}
