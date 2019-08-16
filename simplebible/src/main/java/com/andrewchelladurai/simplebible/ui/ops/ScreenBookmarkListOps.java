package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.Bookmark;

public interface ScreenBookmarkListOps {

  void handleActionClickDelete(@NonNull Bookmark bookmark);

  void handleActionClickEdit(@NonNull Bookmark bookmark);

  void handleActionClickShare(@NonNull Bookmark bookmark);

  void updateVerseList(final TextView verseField, @NonNull String bookmarkReference);

}
