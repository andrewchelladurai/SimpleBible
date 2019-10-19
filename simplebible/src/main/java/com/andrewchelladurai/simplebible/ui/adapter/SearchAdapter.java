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
import com.andrewchelladurai.simplebible.ui.ops.RecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSearchOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchAdapter
    extends RecyclerView.Adapter
    implements RecyclerViewAdapterOps {

  private static final String TAG = "SearchAdapter";

  private static ArrayList<Verse> LIST = new ArrayList<>();
  private static HashMap<Verse, String> SELECTED_LIST = new HashMap<>();

  private final ScreenSearchOps ops;

  public SearchAdapter(final ScreenSearchOps ops) {
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
    ((ItemHolderOps) holder).updateView(LIST.get(position), position);
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
  public HashMap<Verse, String> getSelectedVerses() {
    return SELECTED_LIST;
  }

  private class SearchResultViewHolder
      extends RecyclerView.ViewHolder
      implements ItemHolderOps {

    private final View rootView;
    private final TextView contentView;
    private Verse verse;
    private int position;

    SearchResultViewHolder(@NonNull final View itemView) {
      super(itemView);
      rootView = itemView;
      contentView = itemView.findViewById(R.id.itemSearchResultContent);
      rootView.setOnClickListener(v -> {
        if (SELECTED_LIST.containsKey(verse)) {
          SELECTED_LIST.remove(verse);
        } else {
          SELECTED_LIST.put(verse, contentView.getText().toString());
        }
        rootView.setSelected(SELECTED_LIST.containsKey(verse));
        ops.toggleVerseSelectionActionsState();
      });
    }

    @Override
    public void updateView(final Object object, int position) {
      verse = (Verse) object;
      this.position = position;

      ops.updateSearchResultView(verse, contentView);
      rootView.setSelected(SELECTED_LIST.containsKey(verse));
    }

  }

}
