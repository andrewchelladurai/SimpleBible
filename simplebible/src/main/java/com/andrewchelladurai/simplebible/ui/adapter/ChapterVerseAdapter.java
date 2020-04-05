package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;

public class ChapterVerseAdapter
    extends RecyclerView.Adapter {

  private static final String TAG = "ChapterVerseAdapter";

  @NonNull private final ChapterScreenOps ops;

  @NonNull private final String template;

  public ChapterVerseAdapter(@NonNull final ChapterScreenOps ops, @NonNull final String template) {
    this.ops = ops;
    this.template = template;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterVerseView(LayoutInflater
                                    .from(parent.getContext())
                                    .inflate(R.layout.chapter_screen_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((ChapterVerseView) holder).updateContent(position);
  }

  @Override
  public int getItemCount() {
    return ops.getCachedListSize();
  }

  private class ChapterVerseView
      extends RecyclerView.ViewHolder {

    private final TextView textView;

    private EntityVerse verse;

    ChapterVerseView(final View view) {
      super(view);
      textView = view.findViewById(R.id.scr_chapter_list_item);
    }

    private void updateContent(final int position) {
      verse = ops.getVerseAtPosition(position + 1);
      if (verse == null) {
        Log.e(TAG, "updateContent: null verse at position[" + position + 1 + "]");
        return;
      }

      textView.setText(String.format(template, verse.getVerse(), verse.getText()));
    }

  }

}
