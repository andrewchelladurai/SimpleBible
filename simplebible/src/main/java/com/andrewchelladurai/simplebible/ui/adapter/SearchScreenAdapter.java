package com.andrewchelladurai.simplebible.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.SbRvAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvHolderOps;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;

import java.util.List;

public class SearchScreenAdapter
    extends RecyclerView.Adapter
    implements SbRvAdapterOps {

  private SearchScreenOps ops;

  public SearchScreenAdapter(@NonNull final SearchScreenOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new SearchResultView(
        LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_search_result, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((SearchResultView) holder).updateContent(ops.getCachedItemAt(position));
  }

  @Override
  public int getItemCount() {
    return ops.getCachedListSize();
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    ops.refreshCachedList(newList);
    ops.toggleActionButtons();
  }

  @NonNull
  @Override
  public List<?> getList() {
    return ops.getCachedList();
  }

  class SearchResultView
      extends RecyclerView.ViewHolder
      implements SbRvHolderOps {

    private final View rootView;
    private Verse verse;
    private TextView textView;

    SearchResultView(@NonNull final View rootView) {
      super(rootView);
      textView = rootView.findViewById(R.id.item_search_result_content);
      textView.setOnClickListener(v -> toggleVerseSelection());
      this.rootView = itemView.findViewById(R.id.item_search_result_root);
      this.rootView.setOnClickListener(v -> toggleVerseSelection());
    }

    @Override
    public void updateContent(final Object object) {
      verse = (Verse) object;
      ops.showContent(textView, verse);
      textView.setText(verse.getText());
      rootView.setSelected(ops.isSelected(verse));
    }

    private void toggleVerseSelection() {
      if (ops.isSelected(verse)) {
        ops.removeSelection(verse);
        ops.removeSelection(String.valueOf(textView.getText()));
      } else {
        ops.addSelection(verse);
        ops.addSelection(String.valueOf(textView.getText()));
      }
      rootView.setSelected(ops.isSelected(verse));
      ops.toggleActionButtons();
    }

  }

}
