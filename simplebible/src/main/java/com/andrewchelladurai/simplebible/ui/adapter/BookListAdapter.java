package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.ui.ops.BookListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvHolderOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter
    extends RecyclerView.Adapter
    implements SbRvAdapterOps {

  private static final String TAG = "BookListAdapter";

  private static ArrayList<Book> list = new ArrayList<>(BookUtils.EXPECTED_COUNT);

  private BookListScreenOps ops;

  public BookListAdapter(final BookListScreenOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookView(LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_book, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookView) holder).updateContent(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  @Override
  public void refreshList(@NonNull final List<?> list) {
    BookListAdapter.list.clear();
    for (final Object object : list) {
      BookListAdapter.list.add((Book) object);
    }
    Log.d(TAG, "refreshList: now has [" + getItemCount() + "] records");
  }

  @NonNull
  @Override
  public List<?> getList() {
    return list;
  }

  private class BookView
      extends RecyclerView.ViewHolder
      implements SbRvHolderOps {

    private Book book;

    private TextView name;

    private TextView description;

    private TextView details;

    private TextView testament;

    BookView(final View rootView) {
      super(rootView);
      rootView.setOnClickListener(v -> ops.handleClickBookSelection(book.getName()));

      name = rootView.findViewById(R.id.item_book_name);
      name.setOnClickListener(v -> ops.handleClickBookSelection(book.getName()));

      description = rootView.findViewById(R.id.item_book_desc);
      description.setOnClickListener(v -> ops.handleClickBookSelection(book.getName()));

      details = rootView.findViewById(R.id.item_book_details);
      details.setOnClickListener(v -> ops.handleClickBookSelection(book.getName()));

      testament = rootView.findViewById(R.id.item_book_testament);
      testament.setOnClickListener(v -> ops.handleClickBookSelection(book.getName()));
    }

    @Override
    public void updateContent(final Object object) {
      book = (Book) object;
      name.setText(book.getName());
      description.setText(book.getDescription());
      details.setText(ops.getContentString(book.getVerses(), book.getChapters()));
      testament.setText(book.getTestament());
    }

  }

}
