package com.andrewchelladurai.simplebible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.SearchResult.Verse;

import java.util.List;

public class SearchViewAdapter
        extends RecyclerView.Adapter<SearchViewAdapter.SearchVerse> {

    private final List<SearchResult.Verse> mVerses;
    private final SearchFragment mListener;

    public SearchViewAdapter(List<Verse> items, SearchFragment listener) {
        mVerses = items;
        mListener = listener;
    }

    @Override
    public SearchVerse onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_search, parent, false);
        return new SearchVerse(view);
    }

    @Override
    public void onBindViewHolder(final SearchVerse holder, int position) {
        holder.mItem = mVerses.get(position);
        holder.mVerseId.setText(mVerses.get(position).getVerseReference());
        holder.mVerseText.setText(mVerses.get(position).getVerseText());

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
        return mVerses.size();
    }

    public class SearchVerse
            extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mVerseId;
        public final TextView mVerseText;
        public SearchResult.Verse mItem;

        public SearchVerse(View view) {
            super(view);
            mView = view;
            mVerseId = (TextView) view.findViewById(R.id.fragment_search_verse_Id);
            mVerseText = (TextView) view.findViewById(R.id.fragment_search_verse_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mVerseText.getText() + "'";
        }
    }
}
