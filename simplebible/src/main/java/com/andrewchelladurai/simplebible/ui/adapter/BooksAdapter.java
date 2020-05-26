package com.andrewchelladurai.simplebible.ui.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.ui.ops.BookListScreenOps;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.ArrayList;
import java.util.Set;

public class BooksAdapter
    extends RecyclerView.Adapter {

  private static final String TAG = "BooksAdapter";

  @NonNull
  private static final ArrayList<Integer> BOOK_NUMBER_LIST = new ArrayList<>(Book.MAX_BOOKS);

  @NonNull
  private final BookListScreenOps ops;

  public BooksAdapter(@NonNull final BookListScreenOps ops) {
    this.ops = ops;
    filterList("");
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookListItemView(LayoutInflater.from(parent.getContext())
                                              .inflate(R.layout.item_book,
                                                       parent, false));
  }

  public void filterList(@NonNull final String text) {
    BOOK_NUMBER_LIST.clear();

    final Utils utils = Utils.getInstance();
    final boolean showAll = text.isEmpty();
    final Set<Integer> cachedBookList = Book.getCachedBookList();
    Book book;

    for (final Integer integer : cachedBookList) {
      book = Book.getCachedBook(integer);
      if (book == null) {
        continue;
      }
      if (showAll) {
        BOOK_NUMBER_LIST.add(integer);
      } else {
        if (book.getName().toLowerCase().contains(text.toLowerCase())) {
          BOOK_NUMBER_LIST.add(integer);
        }
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookListItemView) holder)
        .updateContent(Book.getCachedBook(BOOK_NUMBER_LIST.get(position)));
  }

  @Override
  public int getItemCount() {
    return BOOK_NUMBER_LIST.size();
  }

  private class BookListItemView
      extends RecyclerView.ViewHolder {

    private final TextView nameView;

    private final TextView detailsView;

    @Nullable
    private Book book = null;

    BookListItemView(final View view) {
      super(view);
      view.setOnClickListener(v -> ops.handleBookSelection(book));

      nameView = view.findViewById(R.id.item_book_name);
      detailsView = view.findViewById(R.id.item_book_details);
    }

    private void updateContent(@Nullable final Book book) {
      this.book = book;
      if (book == null) {
        nameView.setText(R.string.scr_books_empty_book_name);
        detailsView.setText(R.string.scr_books_empty_book_description);
        return;
      }

      final Resources resources = nameView.getContext().getResources();
      nameView.setText(book.getName());
      detailsView.setText(HtmlCompat.fromHtml(resources.getQuantityString(
          R.plurals.scr_books_list_item_template_details,
          book.getChapters(),
          book.getVerses(),
          book.getChapters(),
          book.getDescription()), HtmlCompat.FROM_HTML_MODE_COMPACT));
    }

  }

}
