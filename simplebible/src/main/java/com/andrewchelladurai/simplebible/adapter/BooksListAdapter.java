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
        extends RecyclerView.Adapter<BooksListAdapter.BookViewHolder> {

    private final List<BookItem>     mValues;
    private final BooksTabOperations mListener;
    private final String             bookNameTemplate;
//    private final String             chapterCountTemplate;

    public BooksListAdapter(List<BookItem> items, BooksTabOperations listener) {
        mValues = items;
        mListener = listener;
        bookNameTemplate = mListener.getBookNameTemplateString();
//        chapterCountTemplate = mListener.chapterCountTemplateString();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_book_entry, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookViewHolder holder, int position) {
        holder.updateItem(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookViewHolder
            extends RecyclerView.ViewHolder {

        final View     mView;
        final TextView mIdView;
        final TextView mContentView;
        BookItem mItem;

        public BookViewHolder(View view) {
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
            int count = mItem.getChapterCount();
            String text = mListener.chapterCountTemplateString(count);
            mContentView.setText(String.format(text, count));

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.bookItemClicked(mItem);
                }
            });
        }
    }
}
