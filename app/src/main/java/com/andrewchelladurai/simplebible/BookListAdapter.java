package com.andrewchelladurai.simplebible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.Book.Details;

import java.util.List;

public class BookListAdapter
        extends RecyclerView.Adapter<BookListAdapter.BookEntryView> {

    private final List<Details> mValues;
    private final BookListFragment mListener;

    public BookListAdapter(List<Details> items, BookListFragment listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public BookEntryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_book, parent, false);
        return new BookEntryView(view);
    }

    @Override
    public void onBindViewHolder(final BookEntryView holder, int position) {
        holder.mDetails = mValues.get(position);
        String bookName = mValues.get(position).name;
        String chapters = mValues.get(position).chapterCount +
                          mListener.getString(R.string.book_details_append_chapters);
        holder.mName.setText(bookName);
        holder.mChapterCount.setText(chapters);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.bookItemClicked(holder.mDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookEntryView
            extends RecyclerView.ViewHolder {

        final View mView;
        final TextView mName;
        final TextView mChapterCount;
        Details mDetails;

        public BookEntryView(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.fragment_book_name);
            mChapterCount = (TextView) view.findViewById(R.id.fragment_book_chapter_count);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mChapterCount.getText() + "'";
        }
    }
}
