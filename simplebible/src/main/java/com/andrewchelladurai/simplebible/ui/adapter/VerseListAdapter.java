package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class VerseListAdapter
    extends RecyclerView.Adapter<VerseListAdapter.ViewHolder>
    implements AdapterOps {

    private static final String      TAG        = "VerseListAdapter";
    private static final List<Verse> CACHE_LIST = new ArrayList<>();

    private static int CACHE_BOOK_NUMBER;
    private static int CACHE_CHAPTER_NUMBER;

    private final ChapterScreenOps mOps;
    private final String           verseContentTemplate;

    public VerseListAdapter(ChapterScreenOps ops) {
        mOps = ops;
        verseContentTemplate = mOps.getVerseTemplateString();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_verse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.updateView(CACHE_LIST.get(position));
    }

    @Override
    public int getItemCount() {
        return CACHE_LIST.size();
    }

    @Override
    public void updateList(@NonNull final List<?> list,
                           @NonNull final Object... objects) {

        int book = (int) objects[0];
        int chapter = (int) objects[1];

        if (book == CACHE_BOOK_NUMBER
            && chapter == CACHE_CHAPTER_NUMBER
            && getItemCount() == list.size()) {
            Log.d(TAG, "already cached [book=" + book + "][chapter=" + chapter + "]");
            return;
        }

        CACHE_BOOK_NUMBER = book;
        CACHE_CHAPTER_NUMBER = chapter;

        CACHE_LIST.clear();
        for (final Object object : list) {
            CACHE_LIST.add((Verse) object);
        }
        Log.d(TAG, "updated cache with [" + getItemCount() + "] verses");
    }

    public boolean isAnyVerseSelected() {
        for (Verse verse : CACHE_LIST) {
            if (verse.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public boolean discardSelectedVerses() {
        for (Verse verse : CACHE_LIST) {
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

        for (Verse verse : CACHE_LIST) {
            if (verse.isSelected()) {
                selectedList.add(verse);
            }
        }
        return selectedList;
    }

    class ViewHolder
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        private final View     mView;
        private final TextView mTextView;
        private       Verse    mVerse;

        private ViewHolder(View view) {
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
            mTextView.setText(
                String.format(verseContentTemplate, mVerse.getVerse(), mVerse.getText()));
            mView.setSelected(mVerse.isSelected());
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            mOps.handleInteractionVerseClicked(mVerse);
            view.setSelected(mVerse.isSelected());
        }
    }
}
