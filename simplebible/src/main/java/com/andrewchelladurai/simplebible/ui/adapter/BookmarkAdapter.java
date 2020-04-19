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
                      .inflate(R.layout.bookmark_verse, parent, false));
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
    EntityVerse verse;
    final int itemCount = getItemCount();
    for (int i = 0; i < itemCount; i++) {
      verse = ops.getVerseAtPosition(i);

      if (verse == null) {
        continue;
      }

      final EntityBook book = Utils.getInstance().getCachedBook(verse.getBook());
      if (book == null) {
        continue;
      }

      arrayList.add(HtmlCompat.fromHtml(String.format(
          template, book.getName(), verse.getChapter(), verse.getVerse(), verse.getText()),
                                        HtmlCompat.FROM_HTML_MODE_COMPACT).toString());
    }

    return arrayList;
  }

  private class BookmarkVerseView
      extends RecyclerView.ViewHolder {

    private final TextView textView;

    @Nullable
    private EntityVerse verse;

    BookmarkVerseView(final View view) {
      super(view);
      textView = view.findViewById(R.id.bookmark_verse_content);
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

      textView.setText(HtmlCompat.fromHtml(String.format(
          template, book.getName(), verse.getChapter(), verse.getVerse(), verse.getText()),
                                           HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

  }

}
