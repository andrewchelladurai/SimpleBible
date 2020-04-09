package com.andrewchelladurai.simplebible.ui.adapter;

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
import com.andrewchelladurai.simplebible.ui.ops.BookListScreenOps;
import com.andrewchelladurai.simplebible.utils.Utils;

public class BookListAdapter
    extends RecyclerView.Adapter {

  @NonNull private final BookListScreenOps ops;

  @NonNull Utils utils = Utils.getInstance();

  public BookListAdapter(@NonNull final BookListScreenOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookListItemView(LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.scr_book_list_list_item,
                                                       parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookListItemView) holder).updateContent(utils.getCachedBook(position + 1));
  }

  @Override
  public int getItemCount() {
    return Utils.MAX_BOOKS;
  }

  private class BookListItemView
      extends RecyclerView.ViewHolder {

    private final TextView nameView;

    private final TextView detailsView;

    @Nullable
    private EntityBook book = null;

    BookListItemView(final View view) {
      super(view);
      view.setOnClickListener(v -> ops.handleBookSelection(book));

      nameView = view.findViewById(R.id.book_list_item_name);
      detailsView = view.findViewById(R.id.book_list_item_details);
    }

    private void updateContent(@Nullable final EntityBook book) {
      this.book = book;
      if (book == null) {
        nameView.setText(R.string.scr_book_list_null_book_name);
        detailsView.setText(R.string.scr_book_list_null_book_description);
        return;
      }

      nameView.setText(book.getName());
      detailsView.setText(HtmlCompat.fromHtml(nameView.getContext()
                                                      .getResources()
                                                      .getQuantityString(
                                                          R.plurals.book_list_item_details_template,
                                                          book.getChapters(),
                                                          book.getVerses(),
                                                          book.getChapters(),
                                                          book.getDescription()),
                                              HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

  }

}
