package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.RecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenChapterOps;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ChapterNumberAdapter
    extends RecyclerView.Adapter
    implements RecyclerViewAdapterOps {

  private static final String TAG = "ScreenChapterNumberAdap";
  private static final ArrayList<Integer> LIST = new ArrayList<>();
  private final ScreenChapterOps ops;

  public ChapterNumberAdapter(final ScreenChapterOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterNumberView(LayoutInflater.from(parent.getContext())
                                               .inflate(R.layout.item_chapter_number, parent,
                                                        false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((ChapterNumberView) holder).updateView(LIST.get(position), position);
  }

  @Override
  public int getItemCount() {
    return LIST.size();
  }

  @Override
  public void updateList(@NonNull final List<?> list) {
    if (LIST.size() != list.size()) {
      clearList();
      for (final Object o : list) {
        LIST.add((Integer) o);
      }
      Log.d(TAG, "updateList: updated [" + getItemCount() + "] records");
    } else {
      Log.d(TAG, "updateList: retained [" + getItemCount() + "] records");
    }
  }

  @Override
  public void filterList(@NonNull final String searchTerm) {
    Log.e(TAG, "filterList: NOT IMPLEMENTED since I believe, it's not required");
  }

  @Override
  public void clearList() {
    LIST.clear();
  }

  private class ChapterNumberView
      extends RecyclerView.ViewHolder
      implements ItemHolderOps {

    private final Chip textView;
    private int chapterNumber;

    ChapterNumberView(final View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.itm_chapter_number_content);
      textView.setOnClickListener(view -> ops.handleClickChapter(chapterNumber));
    }

    @Override
    public void updateView(@NonNull final Object object, final int position) {
      chapterNumber = (Integer) object;
      textView.setText(String.valueOf(chapterNumber));
    }

  }

}
