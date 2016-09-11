package com.andrewchelladurai.simplebible.adapter;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.BookmarksFragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;

import java.util.List;

public class BookmarkListAdapter
        extends RecyclerView.Adapter<BookmarkListAdapter.ViewHolder> {

    private final List<BookmarkItem> mValues;
    private final BookmarksFragment  mListener;

    public BookmarkListAdapter(List<BookmarkItem> items, BookmarksFragment listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mReference.setText(mValues.get(position).getNote());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final View              mView;
        public final AppCompatTextView mReference;
        public final AppCompatTextView mNote;
        public       BookmarkItem      mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mReference = (AppCompatTextView) view.findViewById(R.id.bookmark_item_reference);
            mReference.setOnClickListener(this);
            mNote = (AppCompatTextView) view.findViewById(R.id.bookmark_item_note);
            mNote.setOnClickListener(this);
            AppCompatButton button = (AppCompatButton) view
                    .findViewById(R.id.bookmark_item_button_delete);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mListener.deleteButtonClicked(mItem);
                }
            });
            button = (AppCompatButton) view.findViewById(R.id.bookmark_item_button_share);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mListener.shareButtonClicked(mItem);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mReference.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            if (view instanceof AppCompatTextView) {
//                mListener.bookmarkClicked(mItem);
            }
        }
    }
}
