package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class BookmarkActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    public static final String REFERENCES = "REFERENCES";
    public static final String VERSE_TEXT = "VERSE_TEXT";
    public static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    private static final String TAG = "BookmarkActivity";
    private MODE currentMode;
    private OPERATION currentOperation;
    private String verseReferences = "";

    private TextInputEditText mNotesField;
    private ArrayAdapter<String> mVerseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bookmark_activity_toolbar);
        setSupportActionBar(toolbar);

        currentOperation = OPERATION.INSERT;
        currentMode = MODE.VIEW;
        if (getIntent().getStringExtra(ACTIVITY_MODE).equalsIgnoreCase(MODE.EDIT.toString())) {
            currentMode = MODE.EDIT;
        } else {
            currentMode = MODE.VIEW;
        }

        verseReferences = getIntent().getStringExtra(REFERENCES);
        Log.d(TAG,
                "onCreate: REFERENCES = " +
                        verseReferences);

        ListViewCompat verseList = (ListViewCompat) findViewById(R.id.bookmark_activity_verse_list);
        if (null != verseList) {
            ArrayList<String> listItems = new ArrayList<>(0);
            mVerseListAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, listItems);
            verseList.setAdapter(mVerseListAdapter);
        }

        mNotesField = (TextInputEditText) findViewById(R.id.bookmark_activity_notes);

        bindButton(R.id.bookmark_activity_button_save);
        bindButton(R.id.bookmark_activity_button_delete);
        bindButton(R.id.bookmark_activity_button_share);

        final DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        populateReferences(dbu);

        if (currentMode == MODE.VIEW) {
            mNotesField.setFocusable(false);
        }
        setTitle(R.string.title_activity_bookmark);
    }

    private void bindButton(int resourceID) {
        AppCompatButton button = (AppCompatButton) findViewById(resourceID);
        if (button == null) {
            return;
        }

        switch (resourceID) {
            case R.id.bookmark_activity_button_save:
                if (currentMode == MODE.VIEW) {
                    button.setVisibility(View.GONE);
                    return;
                }
                break;
            case R.id.bookmark_activity_button_delete:
                if (currentMode == MODE.EDIT) {
                    button.setVisibility(View.GONE);
                    return;
                }
                break;
        }
        button.setOnClickListener(this);
    }

    private void populateReferences(final DatabaseUtility dbu) {
        Log.i(TAG, "populateReferences: Top");
        String dbReferences[] = dbu.isReferencePresent(
                verseReferences);

        if (dbReferences != null) {
            verseReferences = dbReferences[0];
            currentOperation = OPERATION.UPDATE;
        }

        mNotesField.setText((dbReferences != null) ? dbReferences[1] : "");

        String individualReference[] = verseReferences.split("~");
        String verseText = "";
        StringBuilder enterText = new StringBuilder(0);
        Book.Details book;

        mVerseListAdapter.clear();
        for (String reference : individualReference) {
            String[] parts = reference.split(":");
            verseText = dbu.getSpecificVerse(
                    Integer.parseInt(parts[0]),  // Book
                    Integer.parseInt(parts[1]),  // Chapter
                    Integer.parseInt(parts[2])); // Verse

            book = Book.getBookDetails(Integer.parseInt(parts[0]));
            enterText.append(book.name)
                    .append(" (").append(
                    parts[1]).append(":").append(parts[2]).append(") - ").append(verseText);
            mVerseListAdapter.add(enterText.toString());
            enterText.delete(0, enterText.length());
        }
        mVerseListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        String label = ((AppCompatButton) v).getText().toString();
        if (label.equalsIgnoreCase(
                getString(R.string.button_save))) {
            handleButtonSaveClicked();
        } else if (label.equalsIgnoreCase(getString(R.string.button_delete))) {
            handleButtonDeleteClicked();
        } else if (label.equalsIgnoreCase(getString(R.string.button_share))) {
            handleButtonShareClicked();
        } else {
            Log.i(TAG, "onClick: " + getString(R.string.how_am_i_here));
        }
    }

    private void handleButtonSaveClicked() {
        String notesText = mNotesField.getText().toString();

        Log.i(TAG, "handleButtonSaveClicked: [" + verseReferences + "] [" + notesText + " ] [" +
                currentOperation.toString() + "]");

        if (verseReferences.isEmpty()) {
            Snackbar.make(mNotesField, "No Verse Present to Bookmark", Snackbar.LENGTH_SHORT).show();
            return;
        }

        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        notesText = (notesText.isEmpty()) ? getString(R.string.empty_bookmark_notes) : notesText;

        String result = "Verse Notes could not be saved";
        switch (currentOperation) {
            case INSERT:
                result = (dbu.createNewBookmark(verseReferences, notesText))
                        ? "Verse Notes saved" : result;
                Snackbar.make(mNotesField, result, Snackbar.LENGTH_SHORT).show();
                mNotesField.setFocusable(false);
                return;
            case UPDATE:
                result = (dbu.updateExistingBookmark(verseReferences,notesText))
                        ?"Verse Notes saved" : result;
                Snackbar.make(mNotesField, result, Snackbar.LENGTH_SHORT).show();
                mNotesField.setFocusable(false);
                return;
            default:
                Log.d(TAG, "handleButtonSaveClicked: " + getString(R.string.how_am_i_here));
        }
        Snackbar.make(mNotesField, result, Snackbar.LENGTH_SHORT).show();
    }

    private void handleButtonDeleteClicked() {
        // FIXME: 11/6/16
        Log.i(TAG, "handleButtonDeleteClicked: ");
    }

    private void handleButtonShareClicked() {
        // FIXME: 11/6/16
        Log.i(TAG, "handleButtonShareClicked: ");
    }

    enum MODE {EDIT, VIEW}

    enum OPERATION {UPDATE, INSERT}
}
