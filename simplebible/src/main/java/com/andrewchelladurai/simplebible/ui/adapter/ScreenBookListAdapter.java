package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.ui.ops.SbRecyclerViewAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbViewHolderOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookListOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;
import java.util.List;

public class ScreenBookListAdapter
    extends RecyclerView.Adapter
    implements SbRecyclerViewAdapterOps {

  private static final String TAG = "ScreenBookListAdapter";
  private static ArrayList<Book> ALL_BOOKS = new ArrayList<>(BookUtils.EXPECTED_COUNT);
  private static ArrayList<Book> FILTERED_BOOK_LIST = new ArrayList<>(BookUtils.EXPECTED_COUNT);
  private final ScreenBookListOps viewOps;

  public ScreenBookListAdapter(final ScreenBookListOps ops) {
    viewOps = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_book, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((SbViewHolderOps) holder).updateView(FILTERED_BOOK_LIST.get(position), position);
  }

  @Override
  public int getItemCount() {
    return FILTERED_BOOK_LIST.size();
  }

  @Override
  public void updateList(@NonNull final List<?> list) {
    clearList();

    for (final Object o : list) {
      ALL_BOOKS.add((Book) o);
    }

    FILTERED_BOOK_LIST.addAll(ALL_BOOKS);
    notifyDataSetChanged();

    Log.d(TAG, "updateList: [" + getItemCount() + "] books now present");
  }

  @Override
  public void filterList(@NonNull final String searchTerm) {
    FILTERED_BOOK_LIST.clear();
    if (searchTerm.isEmpty()) {
      FILTERED_BOOK_LIST.addAll(ALL_BOOKS);
    } else {
      for (final Book book : ALL_BOOKS) {
        if (book.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
          FILTERED_BOOK_LIST.add(book);
        }
      }
    }
    notifyDataSetChanged();
  }

  @Override
  public void clearList() {
    ALL_BOOKS.clear();
    FILTERED_BOOK_LIST.clear();
  }

  private class BookViewHolder
      extends RecyclerView.ViewHolder
      implements SbViewHolderOps {

    private final View rootView;
    private Book book;

    BookViewHolder(final View view) {
      super(view);
      rootView = view;
    }

    @Override
    public void updateView(final Object object, int position) {
      book = (Book) object;

      ((TextView) rootView.findViewById(R.id.itemBookName))
          .setText(book.getName());
      rootView.findViewById(R.id.itemBookName).setOnClickListener(v -> {
        viewOps.handleBookClick(book);
      });

      ((TextView) rootView.findViewById(R.id.itemBookDesc))
          .setText(book.getDescription());
      rootView.findViewById(R.id.itemBookDesc).setOnClickListener(v -> {
        viewOps.handleBookClick(book);
      });

      ((Chip) rootView.findViewById(R.id.itemBookDetails))
          .setText(viewOps.getFormattedBookDetails(book.getChapters()));
      rootView.findViewById(R.id.itemBookDetails).setOnClickListener(v -> {
        viewOps.handleBookClick(book);
      });

    }

  }

}
