package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.ui.ops.SbRecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbViewHolderOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSearchOps;
import java.util.ArrayList;
import java.util.List;

public class ScreenSearchAdapter
    extends RecyclerView.Adapter
    implements SbRecyclerViewAdapterOps {

  private static final String TAG = "ScreenSearchAdapter";

  private static ArrayList<Verse> LIST = new ArrayList<>();
  private static ArrayList<Verse> SELECTED_LIST = new ArrayList<>();

  private final ScreenSearchOps ops;

  public ScreenSearchAdapter(final ScreenSearchOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new SearchResultViewHolder(
        LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_search_result, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((SbViewHolderOps) holder).updateView(LIST.get(position));
  }

  @Override
  public int getItemCount() {
    return LIST.size();
  }

  public int getSelectedItemCount() {
    return SELECTED_LIST.size();
  }

  @Override
  public void updateList(@NonNull final List<?> list) {
    clearList();

    for (final Object obj : list) {
      LIST.add((Verse) obj);
    }

    notifyDataSetChanged();
    Log.d(TAG, "updateList: cached [" + getItemCount() + "] verses");
  }

  @Override
  public void filterList(@NonNull final String searchTerm) {
    // mot implemented since not needed
  }

  @Override
  public void clearList() {
    LIST.clear();
    SELECTED_LIST.clear();
    notifyDataSetChanged();
  }

  private class SearchResultViewHolder
      extends RecyclerView.ViewHolder
      implements SbViewHolderOps {

    private final View rootView;
    private final TextView contentView;
    private Verse verse;

    public SearchResultViewHolder(@NonNull final View itemView) {
      super(itemView);
      rootView = itemView;
      contentView = itemView.findViewById(R.id.itemSearchResultContent);
    }

    @Override
    public void updateView(final Object object) {
      verse = (Verse) object;
      ops.updateSearchResultView(verse, contentView);
    }

  }

}
