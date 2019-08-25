package com.andrewchelladurai.simplebible.ui.ops;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.google.android.material.chip.Chip;

public interface ScreenBookmarkListOps {

  void updateVerseList(@NonNull final Chip verseCountChip,
                       @NonNull final TextView verseField,
                       @NonNull String bookmarkReference);

  void handleBookmarkClick(@NonNull Bookmark bookmark);

}
