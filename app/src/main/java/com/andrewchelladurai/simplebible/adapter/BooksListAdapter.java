package com.andrewchelladurai.simplebible.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.BooksTabOperations;
import com.andrewchelladurai.simplebible.model.BooksList.BookItem;

import java.util.List;

public class BooksListAdapter
        extends RecyclerView.Adapter<BooksListAdapter.BookItemViewHolder> {

    private final List<BookItem>     mValues;
    private final BooksTabOperations mListener;
    private final String             bookNameTemplate;
    private final String             chapterCountTemplate;

    public BooksListAdapter(List<BookItem> items, BooksTabOperations listener) {
        mValues = items;
        mListener = listener;
        bookNameTemplate = mListener.getBookNameTemplateString();
        chapterCountTemplate = mListener.chapterCountTemplateString();
    }

    @Override
    public BookItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_book, parent, false);
        return new BookItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookItemViewHolder holder, int position) {
        holder.updateItem(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookItemViewHolder
            extends RecyclerView.ViewHolder {

        final View     mView;
        final TextView mIdView;
        final TextView mContentView;
        BookItem mItem;

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

        public void updateItem(BookItem bookItem) {
            mItem = bookItem;
            mIdView.setText(String.format(bookNameTemplate, mItem.getBookName()));
            mContentView.setText(String.format(chapterCountTemplate, mItem.getChapterCount()));

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.bookItemClicked(mItem);
                    }
                }
            });
        }
    }
}
