package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.Bookmark;

public interface ScreenBookmarkListOps {

  void updateVerseList(final TextView verseField, @NonNull String bookmarkReference);

  void handleBookmarkClick(@NonNull Bookmark bookmark);

}
