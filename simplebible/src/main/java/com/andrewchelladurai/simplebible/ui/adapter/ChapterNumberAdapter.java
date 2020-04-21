package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class ChapterNumberAdapter
    extends RecyclerView.Adapter {

  private static final String TAG = "ChapterNumberAdapter";

  @NonNull
  private static final ArrayList<Integer> LIST = new ArrayList<>();

  @NonNull
  private final ChapterScreenOps ops;

  public ChapterNumberAdapter(@NonNull final ChapterScreenOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterNumberView(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter_number,
                                                         parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((ChapterNumberView) holder).updateView(LIST.get(position));
  }

  public void updateList(@IntRange(from = 1) final int chapterCount) {
    if (LIST.size() != chapterCount) {
      LIST.clear();
      for (int i = 0; i < chapterCount; i++) {
        LIST.add((i + 1));
      }
      Log.d(TAG, "updateList: updated [" + getItemCount() + "] records");
    } else {
      Log.d(TAG, "updateList: retained [" + getItemCount() + "] records");
    }
  }

  @Override
  public int getItemCount() {
    return LIST.size();
  }

  private class ChapterNumberView
      extends RecyclerView.ViewHolder {

    private final Chip textView;

    private int chapterNumber;

    ChapterNumberView(final View view) {
      super(view);
      textView = view.findViewById(R.id.item_chapter_number_text);
      textView.setOnClickListener(v -> {
        ops.handleNewChapterSelection(chapterNumber);
      });
    }

    private void updateView(final Integer chapterNumber) {
      this.chapterNumber = chapterNumber;
      textView.setText(String.valueOf(chapterNumber));
    }

  }

}
