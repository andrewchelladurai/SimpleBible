package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.utils.Utils;

public class BookmarkAdapter
    extends RecyclerView.Adapter {

  private static final String TAG = "BookmarkAdapter";

  @NonNull
  private final BookmarkScreenOps ops;

  @NonNull
  private String template;

  public BookmarkAdapter(@NonNull final BookmarkScreenOps ops, @NonNull String template) {
    this.ops = ops;
    this.template = template;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookmarkVerseView(
        LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.scr_bookmark_details_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookmarkVerseView) holder).updateContent(ops.getVerseAtPosition(position));
  }

  @Override
  public int getItemCount() {
    return ops.getCachedVerseListSize();
  }

  private class BookmarkVerseView
      extends RecyclerView.ViewHolder {

    private final TextView verseTextView;

    @Nullable
    private EntityVerse verse;

    BookmarkVerseView(final View view) {
      super(view);
      verseTextView = view.findViewById(R.id.item_list_scr_bookmark_detail);
    }

    private void updateContent(@Nullable final EntityVerse verse) {
      this.verse = verse;

      if (this.verse == null) {
        Log.e(TAG, "updateContent:", new IllegalArgumentException("null verse"));
        return;
      }

      final EntityBook book = Utils.getInstance().getCachedBook(verse.getBook());
      if (book == null) {
        Log.e(TAG, "updateContent:",
              new IllegalArgumentException("null book for verse reference["
                                           + this.verse.getReference() + "]"));
        return;
      }

      verseTextView.setText(HtmlCompat.fromHtml(String.format(
          template, book.getName(), verse.getChapter(), verse.getVerse(), verse.getText()),
                                                HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

  }

}
