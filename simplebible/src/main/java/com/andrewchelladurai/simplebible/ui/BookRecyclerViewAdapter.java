package com.andrewchelladurai.simplebible.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.ui.ops.AdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.BookFragmentOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookRecyclerViewAdapter
    extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>
    implements AdapterOps {

    private final List<Book>      mValues;
    private final BookFragmentOps mOps;

    BookRecyclerViewAdapter(@NonNull BookFragmentOps ops) {
        mValues = BookRepository.getInstance().getList();
        mOps = ops;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.updateView(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void updateList() {
        mValues.clear();
        mValues.addAll(BookRepository.getInstance().getList());
    }

    public class ViewHolder
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        final   View     mView;
        final   TextView mtvBookName;
        final   TextView mtvBookDetails;
        private Book     mBook;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mView.setOnClickListener(this);
            mtvBookName = view.findViewById(R.id.item_book_name);
            mtvBookDetails = view.findViewById(R.id.item_book_details);
        }

        @Override
        public void onClick(final View view) {
            mOps.onListFragmentInteraction(mBook);
        }

        @Override
        public void updateView(final Object item) {
            mBook = (Book) item;
            mtvBookName.setText(mBook.getName());
            mtvBookDetails.setText(String.valueOf(mBook.getChapters()));
        }
    }
}
