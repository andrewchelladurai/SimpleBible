package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 6:52 PM.
 */
public class BookmarkVerseAdapter
    extends RecyclerView.Adapter<BookmarkVerseAdapter.ViewHolder>
    implements AdapterOps {

    private static final String TAG = "BookmarkVerseAdapter";

    private final ArrayList<Verse> mCacheList           = new ArrayList<>();
    private final StringBuilder    mDisplayTextTemplate = new StringBuilder();
    private final BookmarkScreenOps mOps;

    private String mReferences;

    public BookmarkVerseAdapter(BookmarkScreenOps ops) {
        mOps = ops;
        if (mDisplayTextTemplate.toString().isEmpty()) {
            mDisplayTextTemplate.append(
                mOps.getSystemContent().getString(
                    R.string.content_bookmark_item_reference_template));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_verse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.updateView(mCacheList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCacheList.size();
    }

    @Override
    public void updateList(final List<?> list, final Object... objects) {
        final String references = (String) objects[0];
        if (references != null && references.equalsIgnoreCase(mReferences)) {
            Log.e(TAG, "already cached");
            return;
        }

        mCacheList.clear();
        mReferences = references;

        for (final Object object : list) {
            mCacheList.add((Verse) object);
        }
        Log.d(TAG, "updated cache with [" + getItemCount() + "] bookmarks");
    }

    class ViewHolder
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        private final View     view;
        private final TextView textView;
        private       Verse    bookmarkVerse;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            textView = view.findViewById(R.id.item_verse_text);
            this.view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return bookmarkVerse.getReference();
        }

        @Override
        public void updateView(@NonNull Object modelObject) {
            bookmarkVerse = (Verse) modelObject;
            textView.setText(String.format(
                mDisplayTextTemplate.toString(),
                Utilities.getInstance().getBookName(bookmarkVerse.getBook()),
                bookmarkVerse.getChapter(),
                bookmarkVerse.getVerse(),
                bookmarkVerse.getText()));
        }

        @Override
        public void onClick(View view) {
            mOps.handleInteractionClick(bookmarkVerse);
        }
    }
}
