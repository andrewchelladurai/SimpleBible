package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.ui.ops.SbRecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbViewHolderOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenChapterOps;

import java.util.ArrayList;
import java.util.List;

public class ScrChapterVerseAdapter
    extends RecyclerView.Adapter
    implements SbRecyclerViewAdapterOps {

  private static final String TAG = "ScrChapterVerseAdapter";
  private static ArrayList<Verse> LIST = new ArrayList<>();
  private final ScreenChapterOps ops;
  private final String contentTemplate;

  public ScrChapterVerseAdapter(final ScreenChapterOps ops, final String template) {
    this.ops = ops;
    contentTemplate = template;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new ChapterVerseView(LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_chapter_verse, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((ChapterVerseView) holder).updateView(LIST.get(position), position);
  }

  @Override
  public int getItemCount() {
    return LIST.size();
  }

  @Override
  public void updateList(@NonNull final List<?> list) {
    clearList();
    for (final Object o : list) {
      LIST.add((Verse) o);
    }
    Log.d(TAG, "updateList: updated [" + getItemCount() + "] records");
  }

  @Override
  public void filterList(@NonNull final String searchTerm) {
    Log.e(TAG, "filterList: NOT IMPLEMENTED");
  }

  @Override
  public void clearList() {
    LIST.clear();
  }

  private class ChapterVerseView
      extends RecyclerView.ViewHolder
      implements SbViewHolderOps {

    private final TextView textView;
    private Verse verse;

    ChapterVerseView(@NonNull final View view) {
      super(view);
      textView = view.findViewById(R.id.itemChapterVerse);
      textView.setOnClickListener(view1 -> ops.handleClickVerse(textView, verse));
    }

    @Override
    public void updateView(final Object object, final int position) {
      verse = (Verse) object;
      final String text = String.format(contentTemplate, verse.getVerse(), verse.getText());
      textView.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

  }

}
