package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.ui.ops.AdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VerseListAdapter
    extends RecyclerView.Adapter<VerseListAdapter.ViewHolder>
    implements AdapterOps {

    private static final String      TAG   = "VerseListAdapter";
    private final        List<Verse> mList = new ArrayList<>();
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
        holder.updateView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void updateList(final List<?> list) {
        mList.clear();
        for (final Object object : list) {
            mList.add((Verse) object);
        }
        Log.d(TAG, "updateList: updated [" + getItemCount() + "] records");
    }

    public boolean isAnyVerseSelected() {
        for (Verse verse : mList) {
            if (verse.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public boolean discardSelectedVerses() {
        for (Verse verse : mList) {
            if (verse.isSelected()) {
                verse.setSelected(false);
            }
        }
        return isAnyVerseSelected();
    }

    class ViewHolder
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
            mTextView.setText(
                String.format(verseContentTemplate, mVerse.getVerse(), mVerse.getText()));
            mView.setSelected(mVerse.isSelected());
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            mOps.handleInteractionClickVerseItem(mVerse);
            view.setSelected(mVerse.isSelected());
        }
    }
}
