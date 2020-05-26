package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.data.Verse;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;

import java.util.ArrayList;

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
                      .inflate(R.layout.item_bookmark_verse, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookmarkVerseView) holder).updateContent(ops.getVerseAtPosition(position));
  }

  @Override
  public int getItemCount() {
    return ops.getCachedVerseListSize();
  }

  @NonNull
  public ArrayList<String> getVerseTexts() {

    final ArrayList<String> arrayList = new ArrayList<>();
    Verse verse;
    final int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      verse = ops.getVerseAtPosition(i);

      if (verse == null) {
        continue;
      }

      final Book book = Book.getCachedBook(verse.getBookNumber());
      if (book == null) {
        continue;
      }

      arrayList.add(verse.getFormattedContentForBookmark(template).toString());
    }

    return arrayList;
  }

  private class BookmarkVerseView
      extends RecyclerView.ViewHolder {

    private final TextView textView;

    @Nullable
    private Verse verse;

    BookmarkVerseView(final View view) {
      super(view);
      textView = view.findViewById(R.id.item_bookmark_verse_text);
    }

    private void updateContent(@Nullable final Verse verse) {
      this.verse = verse;

      if (this.verse == null) {
        Log.e(TAG, "updateContent:", new IllegalArgumentException("null verse"));
        return;
      }

      final Book book = Book.getCachedBook(verse.getBookNumber());
      if (book == null) {
        Log.e(TAG, "updateContent:",
              new IllegalArgumentException("null book for verse reference["
                                           + this.verse.getReference() + "]"));
        return;
      }

      textView.setText(verse.getFormattedContentForBookmark(template));
    }

  }

}
