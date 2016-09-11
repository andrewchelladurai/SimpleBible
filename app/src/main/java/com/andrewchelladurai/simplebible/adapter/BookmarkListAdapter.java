package com.andrewchelladurai.simplebible.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.mIdView.setText(mValues.get(position).getReferences());
        holder.mContentView.setText(mValues.get(position).getNote());

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
            extends RecyclerView.ViewHolder {

        public final View         mView;
        public final TextView     mIdView;
        public final TextView     mContentView;
        public       BookmarkItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content_bookmark_note);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
