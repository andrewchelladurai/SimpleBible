package com.andrewchelladurai.simplebible.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvHolderOps;

import java.util.List;

public class BookmarkedVerseListAdapter
    extends RecyclerView.Adapter
    implements SbRvAdapterOps {

  private BookmarkScreenOps ops;

  public BookmarkedVerseListAdapter(final BookmarkScreenOps ops) {
    this.ops = ops;
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    ops.refreshList(newList);
  }

  @NonNull
  @Override
  public List<?> getList() {
    return ops.getCachedList();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookmarkedVerse(LayoutInflater.from(parent.getContext()).inflate(
        R.layout.item_bookmark_verse, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookmarkedVerse) holder).updateContent(ops.getCachedItemAt(position));
  }

  @Override
  public int getItemCount() {
    return ops.getCachedListSize();
  }

  private class BookmarkedVerse
      extends RecyclerView.ViewHolder
      implements SbRvHolderOps {

    private final TextView verseView;
    private Verse verse;

    BookmarkedVerse(final View rootView) {
      super(rootView);
      verseView = rootView.findViewById(R.id.item_bookmark_verse_content);
    }

    @Override
    public void updateContent(final Object object) {
      verse = (Verse) object;
      ops.showContent(verseView, verse);
    }

  }

}
