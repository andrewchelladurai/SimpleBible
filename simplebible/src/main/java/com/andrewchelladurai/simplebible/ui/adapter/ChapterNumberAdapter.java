package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvHolderOps;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ChapterNumberAdapter
    extends RecyclerView.Adapter
    implements SbRvAdapterOps {

  private static final String TAG = "ChapterNumberAdapter";
  private static ArrayList<String> list = new ArrayList<>();
  private ChapterScreenOps ops;

  public ChapterNumberAdapter(final ChapterScreenOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterNumberView(
        LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_chapter_number, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((ChapterNumberView) holder).updateContent(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    if (list.isEmpty() || list.size() != newList.size()) {
      list.clear();
      for (final Object o : newList) {
        list.add((String) o);
      }
      Log.d(TAG, "refreshList: cached [" + getItemCount() + "] records");
    } else {
      Log.d(TAG, "refreshList: using cached records");
    }
  }

  @NonNull
  @Override
  public List<?> getList() {
    return list;
  }

  private class ChapterNumberView
      extends RecyclerView.ViewHolder
      implements SbRvHolderOps {

    private Chip button;
    private String chapterNumber;

    public ChapterNumberView(final View rootView) {
      super(rootView);
      button = rootView.findViewById(R.id.item_chapter_number);
      button.setOnClickListener(v -> {
        ops.handleClickChapterNumber(Integer.parseInt(chapterNumber));
      });
    }

    @Override
    public void updateContent(final Object object) {
      chapterNumber = (String) object;
      button.setText(chapterNumber);
    }

  }

}
