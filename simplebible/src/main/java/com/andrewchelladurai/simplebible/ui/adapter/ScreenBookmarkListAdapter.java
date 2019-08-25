package com.andrewchelladurai.simplebible.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Bookmark;
import com.andrewchelladurai.simplebible.ui.ops.RecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookmarkListOps;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ScreenBookmarkListAdapter
    extends RecyclerView.Adapter
    implements RecyclerViewAdapterOps {

  private static final String TAG = "ScreenBookmarkListAdapt";

  private static final ArrayList<Bookmark> LIST = new ArrayList<>();
  private final ScreenBookmarkListOps ops;

  @NonNull
  private final String templateEmptyNoteContent;

  public ScreenBookmarkListAdapter(@NonNull final ScreenBookmarkListOps ops,
                                   @NonNull String templateEmptyNoteContent) {
    this.ops = ops;
    this.templateEmptyNoteContent = templateEmptyNoteContent;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookmarkEntry(LayoutInflater.from(parent.getContext())
                                           .inflate(R.layout.item_bookmark_list_entry, parent,
                                                    false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookmarkEntry) holder).updateView(LIST.get(position), position);
  }

  @Override
  public int getItemCount() {
    return LIST.size();
  }

  @Override
  public void updateList(@NonNull final List<?> list) {
    for (final Object o : list) {
      LIST.add((Bookmark) o);
    }
  }

  @Override
  public void filterList(@NonNull final String searchTerm) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearList() {
    LIST.clear();
  }

  private class BookmarkEntry
      extends RecyclerView.ViewHolder
      implements ItemHolderOps {

    private final View rootView;
    private Bookmark bookmark;
    private int position;

    BookmarkEntry(final View view) {
      super(view);
      rootView = view;
      rootView.setOnClickListener(view1 -> ops.handleBookmarkClick(bookmark));
    }

    @Override
    public void updateView(@NonNull final Object object, final int position) {
      bookmark = (Bookmark) object;
      this.position = position;

      final String noteText = (bookmark.getNote().isEmpty())
                              ? templateEmptyNoteContent : bookmark.getNote();

      final Chip noteField = rootView.findViewById(R.id.itemBookmarkEntryNote);
      noteField.setText(noteText);

      final TextView verseField = rootView.findViewById(R.id.itemBookmarkEntryVerse);
      ops.updateVerseList(verseField, bookmark.getReference());

    }

  }

}
