package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.ViewHolderOps;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 1:54 PM.
 */
public class ChapterListAdapter
    extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder>
    implements AdapterOps {

    private static final String             TAG        = "ChapterListAdapter";
    private static final ArrayList<Integer> CACHE_LIST = new ArrayList<>();
    private final ChapterScreenOps mOps;

    public ChapterListAdapter(@NonNull ChapterScreenOps ops) {
        mOps = ops;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_chapter, parent, false));
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
        int newChapterCount = (int) objects[0];
        if (getItemCount() == newChapterCount) {
            Log.d(TAG, "already cached [" + newChapterCount + "] chapters");
            return;
        }

        CACHE_LIST.clear();
        for (final Object object : list) {
            CACHE_LIST.add((Integer) object);
        }
        Log.d(TAG, "updated cache with [" + getItemCount() + "] chapters");
    }

    class ViewHolder
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        private final View     rootView;
        private final TextView chapterNumberField;
        private       int      chapterNumber;

        private ViewHolder(View view) {
            super(view);
            rootView = view;
            chapterNumberField = rootView.findViewById(R.id.item_chapter_number);
        }

        @Override
        public String toString() {
            return chapterNumberField.getText().toString();
        }

        @Override
        public void updateView(@NonNull final Object object) {
            chapterNumber = (Integer) object;
            chapterNumberField.setText(String.valueOf(chapterNumber));
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            mOps.handleInteractionChapterClicked(chapterNumber);
        }
    }
}
