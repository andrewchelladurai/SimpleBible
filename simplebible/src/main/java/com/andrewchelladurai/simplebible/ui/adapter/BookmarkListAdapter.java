package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;
import com.andrewchelladurai.simplebible.util.Utilities;

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

    private static String mHeaderTemplate;
    private static String mDetailTemplate;
    private static String mEmptyNote;
    private static String mVerseDisplayTemplate;

    public BookmarkListAdapter(@NonNull BookmarkListScreenOps ops) {
        mOps = ops;
        if (mHeaderTemplate == null || mHeaderTemplate.isEmpty()
            || mDetailTemplate == null || mDetailTemplate.isEmpty()
            || mEmptyNote == null || mEmptyNote.isEmpty()
            || mVerseDisplayTemplate == null || mVerseDisplayTemplate.isEmpty()) {
            mHeaderTemplate = mOps.getHeaderTemplate();
            mDetailTemplate = mOps.getDetailTemplate();
            mEmptyNote = mOps.getEmptyNoteText();
            mVerseDisplayTemplate = mOps.getVerseDisplayTemplate();
        }
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
        mCacheList.clear();
        Bookmark bookmark;
        for (final Object object : list) {
            bookmark = (Bookmark) object;
            mCacheList.add(bookmark);
        }
        Log.d(TAG, "updateList: updated list with [" + getItemCount() + "] records");
    }

    public class BookmarkListViewHolder
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

            mOps.updateBookmarkHeader(mBookmark, this);

            final String note = mBookmark.getNote();
            if (note.isEmpty()) {
                mDetails.setText(mEmptyNote);
            } else {
                mDetails.setText(String.format(mDetailTemplate, note));
            }

            mView.findViewById(R.id.item_bookmark_view).setOnClickListener(this);
            mView.findViewById(R.id.item_bookmark_action_delete).setOnClickListener(this);
            mView.findViewById(R.id.item_bookmark_action_share).setOnClickListener(this);
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

        public void updateBookmarkHeader(@NonNull final ArrayList<Verse> list) {
            Log.d(TAG, "updateBookmarkHeader() called with [" + list.size() + "] records");

            if (list.isEmpty()) {
                Log.e(TAG, "updateBookmarkHeader: empty or null verse list");
                mHeader.setText("Error getting verse list");
                return;
            }

            Verse verse = list.get(0);
            String verseText = String.format(
                mVerseDisplayTemplate,
                Utilities.getInstance().getBookName(verse.getBook()),
                verse.getChapter(),
                verse.getVerse(),
                verse.getText());

            mHeader.setText(String.format(mHeaderTemplate, list.size(), verseText));
        }
    }
}
