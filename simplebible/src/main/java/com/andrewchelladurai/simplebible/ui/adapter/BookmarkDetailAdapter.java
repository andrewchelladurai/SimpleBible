package com.andrewchelladurai.simplebible.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.ui.ops.RecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookmarkDetailOps;

import java.util.List;

public class BookmarkDetailAdapter
    extends RecyclerView.Adapter
    implements RecyclerViewAdapterOps {

  private ScreenBookmarkDetailOps ops;

  public BookmarkDetailAdapter(final ScreenBookmarkDetailOps ops) {
    this.ops = ops;
  }

  @Override
  public void updateList(@NonNull final List<?> list) {
    // TODO: 11/8/19
  }

  @Override
  public void filterList(@NonNull final String searchTerm) {
    // TODO: 11/8/19
  }

  @Override
  public void clearList() {
    // TODO: 11/8/19
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    // TODO: 11/8/19
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    // TODO: 11/8/19
  }

  @Override
  public int getItemCount() {
    // TODO: 11/8/19
    return 0;
  }

}
