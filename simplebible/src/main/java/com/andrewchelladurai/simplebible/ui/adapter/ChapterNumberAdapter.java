package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ChapterNumberDialogOps;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ChapterNumberAdapter
    extends RecyclerView.Adapter {

  private static final String TAG = "ChapterNumberAdapter";

  @NonNull
  private static final ArrayList<Integer> CHAPTER_NUMBER_LIST = new ArrayList<>();

  @NonNull
  private final ChapterScreenOps screenOps;

  @NonNull
  private final ChapterNumberDialogOps dialogOps;

  public ChapterNumberAdapter(@NonNull final ChapterScreenOps ops,
                              @IntRange(from = 1) final int chapters,
                              @NonNull final ChapterNumberDialogOps dialogOps) {
    this.screenOps = ops;
    this.dialogOps = dialogOps;

    if (CHAPTER_NUMBER_LIST.size() == chapters) {
      Log.d(TAG, "ChapterNumberAdapter: same number of chapters, no need to refresh");
      return;
    }

    CHAPTER_NUMBER_LIST.clear();
    for (int i = 0; i < chapters; i++) {
      CHAPTER_NUMBER_LIST.add(i + 1);
    }

    Log.d(TAG, "ChapterNumberAdapter: list now has [" + getItemCount() + "] chapters");
  }

  @Override
  public int getItemCount() {
    return CHAPTER_NUMBER_LIST.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterNumberView(
        LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.chapter_screen_chapter_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                               final int position) {
    ((ChapterNumberView) holder).updateItem(position);
  }

  private class ChapterNumberView
      extends RecyclerView.ViewHolder {

    private final Chip chapterNumberView;

    ChapterNumberView(final View view) {
      super(view);
      chapterNumberView = view.findViewById(R.id.chapter_screen_chapter_item);
    }

    private void updateItem(final int position) {
      final int chapterNumber = CHAPTER_NUMBER_LIST.get(position);
      chapterNumberView.setText(String.valueOf(chapterNumber));
      chapterNumberView.setOnClickListener(v -> {
        dialogOps.dismissSelectionView();
        screenOps.handleNewChapterSelection(chapterNumber);
      });
    }

  }

}
