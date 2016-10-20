package com.andrewchelladurai.simplebible.adapter;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.BookmarksFragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.interaction.BookmarksTabOperations;
import com.andrewchelladurai.simplebible.interaction.DBUtilityOperations;
import com.andrewchelladurai.simplebible.model.BookmarkList.BookmarkItem;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.DBUtility;

import java.util.List;

public class BookmarkListAdapter
        extends RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewHolder> {

    private final List<BookmarkItem>     mValues;
    private final BookmarksTabOperations mListener;

    public BookmarkListAdapter(List<BookmarkItem> items, BookmarksFragment listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.content_bookmark_entry, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookmarkViewHolder holder, int position) {
        holder.updateView(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class BookmarkViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private static final String TAG = "SB_BookmarkViewHolder";
        BookmarkItem mItem;
        final View              mView;
        final AppCompatTextView mReference;
        final AppCompatTextView mNote;

        BookmarkViewHolder(View view) {
            super(view);
            mView = view;
            mReference = (AppCompatTextView) view.findViewById(R.id.bookmark_item_reference);
            mNote = (AppCompatTextView) view.findViewById(R.id.bookmark_item_note);
            AppCompatButton button =
                    (AppCompatButton) view.findViewById(R.id.bookmark_item_button_delete);
            button.setOnClickListener(this);
            button = (AppCompatButton) view.findViewById(R.id.bookmark_item_button_share);
            button.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mReference.getText() + "'";
        }

        void updateView(BookmarkItem bookmarkItem) {
            mItem = bookmarkItem;

            String[] references =
                    mItem.getReferences().split(Constants.DELIMITER_BETWEEN_REFERENCE);
            final int count = references.length;
            if (count == 0) {
                Log.w(TAG, "updateView: Bookmark Reference is if Zero length");
            }

            // get the details of the first reference in the list
            final String template = mListener.getBookmarkReferenceText(count);
            final DBUtilityOperations dbu = DBUtility.getInstance();

            final String part[] = references[0].split(Constants.DELIMITER_IN_REFERENCE);
            final BooksList.BookItem bookItem = BooksList.getBookItem(Integer.parseInt(part[0]));
            final String bookName = (bookItem == null) ? "" : bookItem.getBookName();
            final String bookNumber = part[0];
            final String chapterNumber = part[1];
            final String verseNumber = part[2];
            final String verseText = dbu.getVerseForReference(Integer.parseInt(bookNumber),
                                                              Integer.parseInt(chapterNumber),
                                                              Integer.parseInt(verseNumber));
            String referenceText = "";
            Log.d(TAG, "updateView: template = " + template + " count = "
                       + count);
            if (count == 1) {
                referenceText = String.format(template,
                                              bookName, chapterNumber, verseNumber, verseText);
            } else if (count > 1) {
                referenceText = String.format(template,
                                              bookName, chapterNumber, verseNumber, count + "");
            } else {
                Log.wtf(TAG, "updateView: " + mListener.getResourceString(R.string.how_am_i_here));
            }
            mReference.setText(referenceText);

            // Populate Notes here
            String noteText = mItem.getNote();
            if (null == noteText || noteText.isEmpty()) {
                noteText = mListener.getResourceString(R.string.fragment_bookmark_empty_note);
            }
            if (noteText.length() > 173) {
                noteText = noteText.substring(0, 173) + " ...";
            }
            mNote.setText(noteText);

            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof AppCompatButton) {
                String label = ((AppCompatButton) view).getText().toString();
                if (label.equalsIgnoreCase(mListener.getDeleteButtonLabel())) {
                    mListener.deleteButtonClicked(mItem);
                } else if (label.equalsIgnoreCase(mListener.getShareButtonLabel())) {
                    mListener.shareButtonClicked(mItem);
                }
            } else {
                mListener.bookmarkClicked(mItem);
            }
        }
    }
}
