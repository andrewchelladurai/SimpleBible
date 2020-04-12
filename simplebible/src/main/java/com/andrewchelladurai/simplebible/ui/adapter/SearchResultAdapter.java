package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchResultAdapter
    extends RecyclerView.Adapter {

  private static final String TAG = "SearchResultAdapter";

  @NonNull
  private static final ArrayList<EntityVerse> BASE_LIST = new ArrayList<>();

  @NonNull
  private static final ArrayMap<String, EntityVerse> SELECTED_LIST = new ArrayMap<>();

  @NonNull
  private static String SEARCH_TEXT = "";

  @NonNull
  private final SearchScreenOps ops;

  @NonNull
  private final String template;

  public SearchResultAdapter(@NonNull final SearchScreenOps ops,
                             @NonNull String template) {
    this.ops = ops;
    this.template = template;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new SearchResultView(
        LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_list_view_scr_search, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((SearchResultView) holder).updateContent(BASE_LIST.get(position));
  }

  public void updateContent(@NonNull final List<EntityVerse> verseList, @NonNull String text) {
    final int count = getItemCount();
    if (SEARCH_TEXT.equalsIgnoreCase(text) && count == verseList.size()) {
      Log.d(TAG, "updateContent: already cached [" + count + "] results for [" + text + "]");
    }

    resetContent();
    BASE_LIST.addAll(verseList);

    Log.d(TAG, "updateContent: updated [" + getItemCount() + " records]");
  }

  @Override
  public int getItemCount() {
    return BASE_LIST.size();
  }

  public void resetContent() {
    BASE_LIST.clear();
    SELECTED_LIST.clear();
    SEARCH_TEXT = "";
    notifyDataSetChanged();
    ops.updateSelectionActionsState();
  }

  public void clearSelectedList() {
    SELECTED_LIST.clear();
    notifyDataSetChanged();
    ops.updateSelectionActionsState();
  }

  public Collection<EntityVerse> getSelectedList() {
    return SELECTED_LIST.values();
  }

  private class SearchResultView
      extends RecyclerView.ViewHolder {

    private final TextView textView;

    private final View selectedView;

    private EntityVerse verse;

    SearchResultView(final View view) {
      super(view);
      textView = view.findViewById(R.id.item_list_view_scr_search);
      selectedView = view.findViewById(R.id.selected_item_list_view_scr_search);

      textView.setOnClickListener(v -> {
        if (SELECTED_LIST.containsKey(verse.getReference())) {
          SELECTED_LIST.remove(verse.getReference());
          selectedView.setVisibility(View.GONE);
        } else {
          SELECTED_LIST.put(verse.getReference(), verse);
          selectedView.setVisibility(View.VISIBLE);
        }
        ops.updateSelectionActionsState();
      });

    }

    private void updateContent(@NonNull final EntityVerse entityVerse) {
      verse = entityVerse;

      final EntityBook book = Utils.getInstance().getCachedBook(verse.getBook());
      if (book == null) {
        Log.e(TAG, "updateContent: null book returned");
        return;
      }

      selectedView.setVisibility(
          SELECTED_LIST.containsKey(verse.getReference()) ? View.VISIBLE : View.GONE);

      textView.setText(String.format(
          template, book.getName(), verse.getChapter(), verse.getVerse(), verse.getText()));
    }

  }

}
