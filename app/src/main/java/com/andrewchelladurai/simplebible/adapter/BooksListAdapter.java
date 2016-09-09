package com.andrewchelladurai.simplebible.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.BooksListFragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.adapter.BooksList.BookItem;

import java.util.List;

public class BooksListAdapter
        extends RecyclerView.Adapter<BooksListAdapter.BookItemViewHolder> {

    private final List<BookItem> mValues;
    private final BooksListFragment mListener;

    public BooksListAdapter(List<BookItem> items, BooksListFragment listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public BookItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_book, parent, false);
        return new BookItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookItemViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String value = String.valueOf(mValues.get(position).getBookNumber());
        holder.mIdView.setText(value);
        value = mValues.get(position).getBookName();
        holder.mContentView.setText(value);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.handleInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookItemViewHolder
            extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public BookItem mItem;

        public BookItemViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.content_book_name);
            mContentView = (TextView) view.findViewById(R.id.content_book_details);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
