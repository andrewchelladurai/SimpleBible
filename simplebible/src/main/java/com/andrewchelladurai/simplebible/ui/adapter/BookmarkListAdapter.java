package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarkListAdapter
    extends RecyclerView.Adapter<BookmarkListAdapter.BookmarkListViewHolder>
    implements AdapterOps {

    private static final String TAG = "BookmarkListAdapter";

    private static List<Bookmark> mCacheList = new ArrayList<>();
    private final BookmarkListScreenOps mOps;

    public BookmarkListAdapter(@NonNull BookmarkListScreenOps ops) {
        mOps = ops;
    }

    @NonNull
    @Override
    public BookmarkListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_bookmark, parent, false);
        return new BookmarkListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkListViewHolder holder, int position) {
        holder.updateView(mCacheList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCacheList.size();
    }

    @Override
    public void updateList(final List<?> list, final Object... objects) {
        Bookmark bookmark;
        for (final Object object : list) {
            bookmark = (Bookmark) object;
            mCacheList.add(bookmark);
        }
        Log.d(TAG, "updateList: updated list with [" + getItemCount() + "] records");
    }

    class BookmarkListViewHolder
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        final View     mView;
        final TextView mHeader;
        final TextView mDetails;
        Bookmark mBookmark;

        BookmarkListViewHolder(View view) {
            super(view);
            mView = view;
            mHeader = view.findViewById(R.id.item_bookmark_header);
            mDetails = view.findViewById(R.id.item_bookmark_details);
        }

        @Override
        public String toString() {
            return mBookmark.toString();
        }

        @Override
        public void updateView(final Object item) {
            mBookmark = (Bookmark) item;

            mHeader.setText(mOps.getFormattedBookmarkHeader(mBookmark));
            mDetails.setText(mOps.getFormattedBookmarkDetails(mBookmark));

            mView.findViewById(R.id.item_bookmark_view).setOnClickListener(this);
            mView.findViewById(R.id.item_bookmark_action_delete).setOnClickListener(this);
            mView.findViewById(R.id.item_bookmark_action_delete).setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            switch (view.getId()) {

                case R.id.item_bookmark_view:
                    mOps.handleActionBookmarkClick(mBookmark);
                    break;
                case R.id.item_bookmark_action_delete:
                    mOps.handleActionDelete(mBookmark);
                    break;
                case R.id.item_bookmark_action_share:
                    mOps.handleActionShare(mBookmark);
                    break;
                default:
                    Log.e(TAG, "onClick: Unknown click event");
            }
        }
    }
}
