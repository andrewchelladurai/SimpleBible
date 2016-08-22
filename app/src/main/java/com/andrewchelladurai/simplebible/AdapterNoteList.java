/*
 *
 * This file 'AdapterNoteList.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.ListNotes.Entry;

import java.util.List;

public class AdapterNoteList
        extends RecyclerView.Adapter<AdapterNoteList.NoteView> {

    private final List<Entry>   mValues;
    private final FragmentNotes mListener;

    public AdapterNoteList(List<Entry> items, FragmentNotes listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public NoteView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_note, parent,
                                                                     false);
        return new NoteView(view);
    }

    @Override
    public void onBindViewHolder(final NoteView noteView, int position) {
        noteView.update(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class NoteView
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private static final String TAG = "SB_NoteView";
        private final View              mView;
        private final AppCompatTextView mVerseText;
        private final AppCompatTextView mNotesText;
        private       Entry             mEntry;

        public NoteView(View view) {
            super(view);
            mView = view;
            mVerseText = (AppCompatTextView) view.findViewById(R.id.entry_note_verse);
            // mVerseText.setOnClickListener(this);
            mNotesText = (AppCompatTextView) view.findViewById(R.id.entry_note_text);
            mView.findViewById(R.id.entry_note_but_delete).setOnClickListener(this);
            mView.findViewById(R.id.entry_note_but_share).setOnClickListener(this);
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return mNotesText.getText().toString();
        }

        void update(Entry entry) {
            mEntry = entry;

            String text;
            String[] references = mEntry.getReference();
            if (references == null) {
                Utilities.throwError(TAG + " references == null");
            }

            //Populate Verse Text
            if (references.length > 1) {
                String part[] = references[0].split(Utilities.DELIMITER_IN_REFERENCE);
                text = mListener.getString(R.string.fragment_note_entry_multiple_bookmark);
                text = String.format(text, ListBooks.getItem(part[0]).getName(),
                                     part[1], part[2], references.length - 1);

            } else {
                DatabaseUtility dbu = DatabaseUtility.getInstance(
                        mListener.getActivity().getApplicationContext());

                String part[] = references[0].split(Utilities.DELIMITER_IN_REFERENCE);
                text = dbu.getSpecificVerse(Integer.parseInt(part[0]), Integer.parseInt(part[1]),
                                            Integer.parseInt(part[2]));
                text = Utilities.getFormattedBookmarkVerse(part[0], part[1], part[2], text);
            }
            mVerseText.setText(text);

            // Populate Notes here
            text = mEntry.getNotes();
            if (null == text || text.isEmpty()) {
                text = mListener.getString(R.string.activity_bookmark_empty_note);
            }
            if (text.length() > 173) {
                text = text.substring(0, 173) + " ...";
            }
            mNotesText.setText(text);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof AppCompatButton) {
                AppCompatButton button = (AppCompatButton) view;
                switch (button.getId()) {
                    case R.id.entry_note_but_delete:
                        mListener.handleDeleteButtonClick(mEntry);
                        break;
                    case R.id.entry_note_but_share:
                        mListener.handleShareButtonClick(mEntry);
                        break;
                    default:
                        Utilities.throwError(
                                TAG + "onClick : unknown buttonID = " + button.getId());
                }
            } else {
                mListener.handleVerseClick(mEntry);
            }
        }
    }
}
