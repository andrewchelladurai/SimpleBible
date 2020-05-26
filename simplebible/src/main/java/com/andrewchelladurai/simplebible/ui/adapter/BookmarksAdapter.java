package com.andrewchelladurai.simplebible.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.ui.ops.BookmarksScreenOps;

public class BookmarksAdapter
    extends RecyclerView.Adapter {

  @NonNull
  private final BookmarksScreenOps ops;

  public BookmarksAdapter(@NonNull final BookmarksScreenOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookmarkView(LayoutInflater.from(parent.getContext())
                                          .inflate(R.layout.item_bookmark, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookmarkView) holder).updateContent(ops.getBookmarkAtPosition(position));
  }

  @Override
  public int getItemCount() {
    return ops.getBookmarkListSize();
  }

  private class BookmarkView
      extends RecyclerView.ViewHolder {

    private final TextView verseView;

    private final TextView noteView;

    private EntityBookmark bookmark;

    BookmarkView(final View view) {
      super(view);
      verseView = view.findViewById(R.id.item_bookmark_verse);
      noteView = view.findViewById(R.id.item_bookmark_note);

      verseView.setOnClickListener(v -> ops.handleActionSelect(bookmark));
      noteView.setOnClickListener(v -> ops.handleActionSelect(bookmark));
      view.setOnClickListener(v -> ops.handleActionSelect(bookmark));

    }

    private void updateContent(@NonNull final EntityBookmark bookmark) {
      this.bookmark = bookmark;

      ops.getFirstVerseOfBookmark(bookmark, verseView, noteView);

    }

  }

}
