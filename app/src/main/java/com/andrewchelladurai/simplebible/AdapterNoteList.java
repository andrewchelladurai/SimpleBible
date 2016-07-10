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

import android.support.v7.widget.AppCompatImageButton;
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
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.entry_note, parent, false);
        return new NoteView(view);
    }

    @Override
    public void onBindViewHolder(final NoteView noteView, int position) {
        noteView.update(mValues.get(position));
/*
        noteView.mEntry = mValues.get(position);
        noteView.mIdView.setText(mValues.get(position).id);
        noteView.mContentView.setText(mValues.get(position).content);

        noteView.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(noteView.mEntry);
                }
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class NoteView
            extends RecyclerView.ViewHolder {

        private final View              mView;
        private final AppCompatTextView mVerseText;
        private final AppCompatTextView mNotesText;
        private Entry                   mEntry;

        public NoteView(View view) {
            super(view);
            mView = view;
            mVerseText = (AppCompatTextView) view.findViewById(R.id.note_verse);
            mVerseText.setOnClickListener(mListener);
            mNotesText = (AppCompatTextView) view.findViewById(R.id.note_text);
            bindButton(R.id.note_but_edit);
            bindButton(R.id.note_but_delete);
            bindButton(R.id.note_but_share);
        }

        private void bindButton(int buttonId) {
            AppCompatImageButton button = (AppCompatImageButton) mView.findViewById(buttonId);
            button.setOnClickListener(mListener);
        }

        @Override
        public String toString() {
            return mNotesText.getText().toString();
        }

        public void update(Entry entry) {
            mEntry = entry;
        }
    }
}
