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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentNotes
        extends Fragment {

    private static final String TAG = "SB_FragmentNotes";
    private AdapterNoteList noteListAdapter;
    private RecyclerView    notesList;

    public FragmentNotes() {
    }

    public static FragmentNotes newInstance() {
        return new FragmentNotes();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        if (getArguments() != null) {
            // mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
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

    void handleVerseClick(ListNotes.Entry entry) {
        Utilities.log(TAG, "handleVerseClick() called");
        ArrayList<String> references = new ArrayList<>();
        Collections.addAll(references, entry.getReference());
        Utilities.log(TAG, "handleVerseClick: reference count = " + references.size());

        Bundle bundle = (getArguments() != null) ? getArguments() : new Bundle();
        bundle.putStringArrayList(Utilities.REFERENCES, references);

        Intent intent = new Intent(getContext(), ActivityBookmark.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void handleDeleteButtonClick(final ListNotes.Entry pEntry) {
        Utilities.log(TAG, "handleDeleteButtonClick() called");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                                                                    R.style.DawnTheme_AlertDialog);
        builder.setTitle(R.string.activity_bookmark_alert_delete_title);
        builder.setMessage(R.string.activity_bookmark_alert_delete_message);

        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        builder.setPositiveButton(
                R.string.activity_bookmark_alert_delete_positive_text,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface pDialogInterface, int pI) {
                        StringBuilder reference = getConvertedReference(pEntry.getReference());
                        DatabaseUtility dbu = DatabaseUtility.getInstance(getContext());
                        if (dbu.deleteBookmark(reference.toString())) {
                            Toast.makeText(getContext(),
                                           R.string.activity_bookmark_toast_delete_success,
                                           Toast.LENGTH_SHORT).show();
                            ((ActivitySimpleBible) getActivity()).onResume();
                        } else {
                            Toast.makeText(getContext(),
                                           R.string.activity_bookmark_toast_delete_failure,
                                           Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.setNegativeButton(R.string.activity_bookmark_alert_delete_negative_text,
                                  new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(
                                              DialogInterface pDialogInterface, int pI) {
                                          dialog.dismiss();
                                      }
                                  });
        builder.create();
        builder.show();
    }

    private StringBuilder getConvertedReference(String[] pReferences) {
        StringBuilder reference = new StringBuilder();
        String delimiter = Utilities.DELIMITER_BETWEEN_REFERENCE;
        for (String entry : pReferences) {
            reference.append(entry).append(delimiter);
        }
        // remove the last appended DELIMITER_BETWEEN_REFERENCE
        reference.delete(reference.lastIndexOf(delimiter), reference.length());
        return reference;
    }

    void handleShareButtonClick(ListNotes.Entry pEntry) {
        Utilities.log(TAG, "handleShareButtonClick() called");
        DatabaseUtility dbu = DatabaseUtility.getInstance(getContext());
        String[] reference = pEntry.getReference();
        StringBuilder shareText = new StringBuilder();
        for (String str : reference) {
            String part[] = str.split(Utilities.DELIMITER_IN_REFERENCE);
            String verseText = dbu.getSpecificVerse(Integer.parseInt(part[0]),
                                                    Integer.parseInt(part[1]),
                                                    Integer.parseInt(part[2]));
            shareText.append(Utilities.getFormattedBookmarkVerse(
                    part[0], part[1], part[2], verseText)).append('\n');
        }

        if (pEntry.getNotes().isEmpty()) {
            shareText.append(getString(R.string.activity_bookmark_empty_note)).append('\n');
        } else {
            shareText.append(getString(R.string.activity_bookmark_share_note_below_text))
                     .append('\n')
                     .append(pEntry.getNotes().trim()).append('\n');
        }
        shareText.append(getString(R.string.share_append_text));
        startActivity(Utilities.shareVerse(shareText.toString()));
    }
}
