package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
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

  private static final String TAG = "SearchScreenAdapter";
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
    ((SearchResultView) holder).updateContent(ops.getAdapterItemAt(position));
  }

  @Override
  public int getItemCount() {
    return ops.getAdapterListSize();
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    ops.clearAdapterList();
    for (final Object object : newList) {
      ops.addToAdapterList(object);
    }
    Log.d(TAG, "refreshList: list now contains [" + getItemCount() + "] records");
  }

  @NonNull
  @Override
  public List<?> getList() {
    return ops.getAdapterList();
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
      ops.showFormattedResultContent(textView, verse);
      textView.setText(verse.getText());
      rootView.setSelected(ops.isResultSelected(verse));
    }

    private void toggleVerseSelection() {
      if (ops.isResultSelected(verse)) {
        ops.removeSelectedResult(verse);
        ops.removeSelectedText(String.valueOf(textView.getText()));
      } else {
        ops.addSelectedResult(verse);
        ops.addSelectedText(String.valueOf(textView.getText()));
      }
      rootView.setSelected(ops.isResultSelected(verse));
      ops.toggleActionButtons();
    }

  }

}
