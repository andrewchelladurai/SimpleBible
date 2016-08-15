/*
 *
 * This file 'NotesFragment.java' is part of SimpleBible :
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentNotes
        extends Fragment
        implements View.OnClickListener {

    private static final String TAG = "SB_FragmentNotes";
    private AdapterNoteList noteListAdapter;
    private RecyclerView notesList;

    public FragmentNotes() {
    }

    public static FragmentNotes newInstance() {
        return new FragmentNotes();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        ListNotes.populate();
        if (ListNotes.getCount() > 0) {
            Utilities.log(TAG, "onCreateView: ListNotes.getCount() > 0 == true");
            View view = inflater.inflate(R.layout.fragment_note, container, false);
            notesList = (RecyclerView) view.findViewById(R.id.frag_notes_list);

            noteListAdapter = new AdapterNoteList(ListNotes.getEntries(), this);
            notesList.setAdapter(noteListAdapter);
            return view;
        } else {
            Utilities.log(TAG, "onCreateView: ListNotes.getCount() > 0 == false");
            return inflater.inflate(R.layout.fragment_note_empty, container, false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof AppCompatTextView) {
            handleVerseClick();
        } else if (view instanceof AppCompatImageButton) {
            AppCompatImageButton button = (AppCompatImageButton) view;
            switch (button.getId()) {
                case R.id.note_but_edit:
                    handleEditButtonClick();
                    break;
                case R.id.note_but_delete:
                    handleDeleteButtonClick();
                    break;
                case R.id.note_but_share:
                    handleShareButtonClick();
                    break;
                default:
                    Utilities.throwError(TAG + "onClick : unknown buttonID = " + button.getId());
            }
        }
    }

    private void handleVerseClick() {
        Utilities.log(TAG, "handleVerseClick() called");
    }

    private void handleEditButtonClick() {
        Utilities.log(TAG, "handleEditButtonClick() called");
    }

    private void handleDeleteButtonClick() {
        Utilities.log(TAG, "handleDeleteButtonClick() called");
    }

    private void handleShareButtonClick() {
        Utilities.log(TAG, "handleShareButtonClick() called");
    }

    public void refreshData() {
        Utilities.log(TAG, "refreshData() called");
        ListNotes.populate();
        if (notesList == null || noteListAdapter == null) {
            getFragmentManager().beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        } else {
            noteListAdapter.notifyDataSetChanged();
        }
    }
}
