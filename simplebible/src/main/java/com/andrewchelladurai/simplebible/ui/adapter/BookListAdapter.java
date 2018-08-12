package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.ui.ops.AdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.BooksScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookListAdapter
    extends RecyclerView.Adapter<BookListAdapter.ViewHolder>
    implements AdapterOps {

    private static final String     TAG   = "BookListAdapter";
    private final        List<Book> mList = new ArrayList<>();
    private final BooksScreenOps mOps;

    public BookListAdapter(@NonNull BooksScreenOps ops) {
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
        holder.updateView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void updateList(final List<?> list) {
        mList.clear();
        for (Object book : list) {
            mList.add((Book) book);
        }
        Log.d(TAG, "updateList: updated [" + mList.size() + "] records");
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
            mtvBookName.setText(mOps.getFormattedBookListHeader(mBook));
            mtvBookDetails.setText(mOps.getFormattedBookListDetails(mBook));
        }
    }
}
