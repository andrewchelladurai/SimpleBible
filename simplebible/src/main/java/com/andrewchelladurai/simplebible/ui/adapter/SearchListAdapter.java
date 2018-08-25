package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;
import com.andrewchelladurai.simplebible.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SearchListAdapter
    extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>
    implements AdapterOps {

    private static final String TAG = "SearchListAdapter";

    private static List<Verse> mCacheList = new ArrayList<>();
    private static String mCacheText;

    private final SearchScreenOps mOps;
    private final String          verseContentTemplate;

    public SearchListAdapter(SearchScreenOps ops) {
        mOps = ops;
        verseContentTemplate = mOps.getSearchVerseTemplateString();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_verse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.updateView(mCacheList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCacheList.size();
    }

    @Override
    public void updateList(@NonNull final List<?> list,
                           @NonNull final Object... cacheParams) {
        final String searchText = (String) cacheParams[0];

        if (mCacheText != null
            && !mCacheText.isEmpty()
            && searchText.equalsIgnoreCase(mCacheText)
            && getItemCount() == list.size()) {
            Log.d(TAG, "already cached [searchText=" + searchText + "]");
            return;
        }

        mCacheText = searchText;
        mCacheList.clear();
        for (final Object object : list) {
            mCacheList.add((Verse) object);
        }
        Log.d(TAG, "updated cache with [" + getItemCount() + "] verses");
    }

    public boolean isAnyVerseSelected() {
        for (Verse verse : mCacheList) {
            if (verse.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public boolean discardSelectedVerses() {
        for (Verse verse : mCacheList) {
            if (verse.isSelected()) {
                verse.setSelected(false);
            }
        }
        return isAnyVerseSelected();
    }

    @Nullable
    public ArrayList<Verse> getSelectedVerses() {
        if (!isAnyVerseSelected()) {
            Log.e(TAG, "getSelectedVerses: no verse is selected");
            return null;
        }

        final ArrayList<Verse> selectedList = new ArrayList<>();

        for (Verse verse : mCacheList) {
            if (verse.isSelected()) {
                selectedList.add(verse);
            }
        }
        return selectedList;
    }

    public void clearList() {
        mCacheText = "";
        mCacheList.clear();
    }

    public class ViewHolder
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        private final View     mView;
        private final TextView mTextView;
        private       Verse    mVerse;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTextView = view.findViewById(R.id.item_verse_text);
        }

        @Override
        public String toString() {
            return mVerse.toString();
        }

        @Override
        public void updateView(final Object item) {
            mVerse = (Verse) item;
            mTextView.setText(mVerse.getText());
            mView.setOnClickListener(this);

            final String bookName = Utilities.getInstance().getBookName(mVerse.getBook());
            mTextView.setText(
                String.format(verseContentTemplate, bookName,
                              mVerse.getChapter(), mVerse.getVerse(), mVerse.getText()));
            mView.setSelected(mVerse.isSelected());
        }

        @Override
        public void onClick(final View view) {
            mOps.actionVerseClicked(mVerse);
            mView.setSelected(mVerse.isSelected());
        }
    }
}
