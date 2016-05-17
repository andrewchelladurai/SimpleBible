package com.andrewchelladurai.simplebible;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

        String vText = mVerses.get(position).getVerseText();
        String vId[] = mVerses.get(position).getVerseReference().split(":");
        int bookNumber = Integer.parseInt(vId[0]);
        int chapterNumber = Integer.parseInt(vId[1]);
        int verseNumber = Integer.parseInt(vId[2]);

        Book.Details book = Book.getBookDetails(bookNumber);
        String bookName = book != null ? book.name : "";

        String template = getString(R.string.search_result_template);
        template = template.replace(getString(R.string.search_result_template_text), vText);
        template = template.replace(getString(R.string.search_result_template_book), bookName);
        template = template.replace(getString(R.string.search_result_template_chapter), vId[1]);
        template = template.replace(getString(R.string.search_result_template_verse), vId[2]);

        holder.mVerse.setText(Html.fromHtml(template));

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(final View v) {
                mListener.searchResultLongClicked(holder.mItem);
                return true;
            }
        });
    }

    private String getString(final int pStringId) {
        return (mListener != null) ? mListener.getString(pStringId) : "";
    }

    @Override
    public int getItemCount() {
        return mVerses.size();
    }

    public class SearchVerse
            extends RecyclerView.ViewHolder {

        SearchResult.Verse mItem;
        final TextView mVerse;
        final View mView;

        public SearchVerse(View view) {
            super(view);
            mView = view;
            mVerse = (TextView) view.findViewById(R.id.fragment_search_result_entry);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mVerse.getText();
        }
    }
}
