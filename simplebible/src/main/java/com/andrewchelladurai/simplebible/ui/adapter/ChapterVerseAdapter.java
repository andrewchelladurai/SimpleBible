package com.andrewchelladurai.simplebible.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;

public class ChapterVerseAdapter
    extends RecyclerView.Adapter {

  @NonNull private final ChapterScreenOps ops;

  @NonNull private final String verseTemplate;

  public ChapterVerseAdapter(@NonNull final ChapterScreenOps ops, @NonNull final String template) {
    this.ops = ops;
    this.verseTemplate = template;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    // TODO: 5/4/20
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    // TODO: 5/4/20
  }

  @Override
  public int getItemCount() {
    // TODO: 5/4/20
    return 0;
  }

}
