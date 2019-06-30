package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
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
  private static ArraySet<Integer> SELECTED_LIST = new ArraySet<>();

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
    ((SbViewHolderOps) holder).updateView(LIST.get(position), position);
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

  public void clearSelection() {
    SELECTED_LIST.clear();
    notifyDataSetChanged();
  }

  @NonNull
  public ArraySet<Integer> getSelection() {
    return SELECTED_LIST;
  }

  private class SearchResultViewHolder
      extends RecyclerView.ViewHolder
      implements SbViewHolderOps {

    private final View rootView;
    private final TextView contentView;
    private Verse verse;
    private int position;

    SearchResultViewHolder(@NonNull final View itemView) {
      super(itemView);
      rootView = itemView;
      contentView = itemView.findViewById(R.id.itemSearchResultContent);
      rootView.setOnClickListener(v -> {
        if (SELECTED_LIST.contains(position)) {
          SELECTED_LIST.remove(position);
        } else {
          SELECTED_LIST.add(position);
        }
        rootView.setSelected(SELECTED_LIST.contains(position));
        ops.updateSelectionActionState();
      });
    }

    @Override
    public void updateView(final Object object, int position) {
      verse = (Verse) object;
      this.position = position;

      ops.updateSearchResultView(verse, contentView);
      rootView.setSelected(SELECTED_LIST.contains(position));
    }

  }

}
