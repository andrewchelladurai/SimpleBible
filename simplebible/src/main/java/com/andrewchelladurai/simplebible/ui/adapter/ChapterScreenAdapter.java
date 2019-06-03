package com.andrewchelladurai.simplebible.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvHolderOps;
import java.util.List;

public class ChapterScreenAdapter
    extends RecyclerView.Adapter
    implements SbRvAdapterOps {

  private final ChapterScreenOps ops;
  private final String contentTemplate;

  public ChapterScreenAdapter(@NonNull final ChapterScreenOps ops,
                              @NonNull final String contentTemplate) {
    this.ops = ops;
    this.contentTemplate = contentTemplate;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterVerseItem(
        LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_chapter_verse, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((ChapterVerseItem) holder).updateContent(ops.getCachedItemAt(position));
  }

  @Override
  public int getItemCount() {
    return ops.getCacheSize();
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    ops.updateCache(newList);
  }

  @NonNull
  @Override
  public List<?> getList() {
    return ops.getCache();
  }

  private class ChapterVerseItem
      extends RecyclerView.ViewHolder
      implements SbRvHolderOps {

    //    private final View rootView;
    private final TextView contentView;
    private Verse verse;

    public ChapterVerseItem(final View view) {
      super(view);
/*
      rootView = view.findViewById(R.id.item_chapter_verse);
      rootView.setOnClickListener(v -> toggleVerseSelection());
*/

      contentView = view.findViewById(R.id.item_chapter_verse_content);
      contentView.setOnClickListener(v -> toggleVerseSelection());
    }

    @Override
    public void updateContent(@NonNull final Object object) {
      verse = (Verse) object;
      contentView.setText(HtmlCompat.fromHtml(
          String.format(contentTemplate, verse.getVerseNumber(), verse.getText()),
          HtmlCompat.FROM_HTML_MODE_LEGACY));
      contentView.setSelected(ops.isSelected(verse));
    }

    private void toggleVerseSelection() {
      if (ops.isSelected(verse)) {
        ops.removeSelection(verse);
        ops.removeSelection(String.valueOf(contentView.getText()));
      } else {
        ops.addSelection(verse);
        ops.addSelection(String.valueOf(contentView.getText()));
      }
      contentView.setSelected(ops.isSelected(verse));
      ops.toggleActionButtons();
    }

  }

}
