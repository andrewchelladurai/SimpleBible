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
  private static ArrayList<Book> BOOK_LIST = new ArrayList<>(BookUtils.EXPECTED_COUNT);
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
    ((SbViewHolderOps) holder).updateView(BOOK_LIST.get(position));
  }

  @Override
  public int getItemCount() {
    return BOOK_LIST.size();
  }

  @Override
  public void updateList(final List<?> list) {
    BOOK_LIST.clear();
    for (final Object o : list) {
      BOOK_LIST.add((Book) o);
    }
    notifyDataSetChanged();
    Log.d(TAG, "updateList: added [" + getItemCount() + "] books");
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
    public void updateView(final Object object) {
      book = (Book) object;

      ((TextView) rootView.findViewById(R.id.itemBookName))
          .setText(book.getName());

      ((TextView) rootView.findViewById(R.id.itemBookDesc))
          .setText(book.getDescription());

      ((Chip) rootView.findViewById(R.id.itemBookDetails))
          .setText(viewOps.getFormattedBookDetails(book.getChapters()));
    }

  }

}
