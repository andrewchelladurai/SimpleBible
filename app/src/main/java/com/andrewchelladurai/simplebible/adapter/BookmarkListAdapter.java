package com.andrewchelladurai.simplebible.adapter;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.BookmarksFragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.BookmarksTabOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;

import java.util.List;

public class BookmarkListAdapter
        extends RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewHolder> {

    private final List<BookmarkItem>     mValues;
    private final BookmarksTabOperations mListener;

    public BookmarkListAdapter(List<BookmarkItem> items, BookmarksFragment listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_bookmark_entry, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookmarkViewHolder holder, int position) {
        holder.updateView(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class BookmarkViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final View              mView;
        public final AppCompatTextView mReference;
        public final AppCompatTextView mNote;
        public       BookmarkItem      mItem;

        public BookmarkViewHolder(View view) {
            super(view);
            mView = view;
            mReference = (AppCompatTextView) view.findViewById(R.id.bookmark_item_reference);
            mNote = (AppCompatTextView) view.findViewById(R.id.bookmark_item_note);
            AppCompatButton button =
                    (AppCompatButton) view.findViewById(R.id.bookmark_item_button_delete);
            button.setOnClickListener(this);
            button = (AppCompatButton) view.findViewById(R.id.bookmark_item_button_share);
            button.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mReference.getText() + "'";
        }

        public void updateView(BookmarkItem bookmarkItem) {
            mItem = bookmarkItem;
            mReference.setText(mItem.getNote());

            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof AppCompatButton) {
                String label = ((AppCompatButton) view).getText().toString();
                if (label.equalsIgnoreCase(mListener.getDeleteButtonLabel())) {
                    mListener.deleteButtonClicked(mItem);
                } else if (label.equalsIgnoreCase(mListener.getShareButtonLabel())) {
                    mListener.shareButtonClicked(mItem);
                }
            } else {
                mListener.bookmarkClicked(mItem);
            }
        }
    }
}
