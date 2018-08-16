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

    private static final String           TAG                   = "BookmarkVerseAdapter";
    private static final ArrayList<Verse> CACHE_LIST            = new ArrayList<>();
    private static final StringBuilder    DISPLAY_TEXT_TEMPLATE = new StringBuilder();
    private static       String           sReferences           = null;
    private final BookmarkScreenOps mOps;

    public BookmarkVerseAdapter(BookmarkScreenOps ops) {
        mOps = ops;
        if (DISPLAY_TEXT_TEMPLATE.toString().isEmpty()) {
            DISPLAY_TEXT_TEMPLATE.append(
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
        holder.updateView(CACHE_LIST.get(position));
    }

    @Override
    public int getItemCount() {
        return CACHE_LIST.size();
    }

    @Override
    public void updateList(final List<?> list, final Object... objects) {
        final String references = (String) objects[0];
        if (references != null && references.equalsIgnoreCase(sReferences)) {
            Log.e(TAG, "updateList: already cached");
            return;
        }

        CACHE_LIST.clear();
        sReferences = references;
        for (final Object object : list) {
            CACHE_LIST.add((Verse) object);
        }
        Log.d(TAG, "updateList: updated [" + getItemCount() + "] bookmarks");
    }

    class ViewHolder
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        final View     view;
        final TextView textView;
        Verse bookmarkVerse;

        ViewHolder(View view) {
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
                DISPLAY_TEXT_TEMPLATE.toString(),
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
