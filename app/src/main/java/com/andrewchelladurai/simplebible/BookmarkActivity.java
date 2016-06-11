package com.andrewchelladurai.simplebible;

import android.os.Bundle;
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
        extends AppCompatActivity implements View.OnClickListener {

    public static final String REFERENCES = "REFERENCES";
    public static final String VERSE_TEXT = "VERSE_TEXT";
    public static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    private static final String TAG = "BookmarkActivity";
    private MODE currentMode;
    private String verseReferences = "";

    private TextInputEditText notesText;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bookmark_activity_toolbar);
        setSupportActionBar(toolbar);

        currentMode = MODE.VIEW;
        if (getIntent().getStringExtra(ACTIVITY_MODE).equalsIgnoreCase(MODE.EDIT.toString())) {
            currentMode = MODE.EDIT;
        } else {
            currentMode = MODE.VIEW;
        }

        verseReferences = getIntent().getStringExtra(REFERENCES);
        Log.d(TAG, "onCreate: REFERENCES = " + verseReferences);

        ListViewCompat verseList = (ListViewCompat) findViewById(R.id.bookmark_activity_verse_list);
        if (null != verseList) {
            ArrayList<String> listItems = new ArrayList<>(0);
            listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
            verseList.setAdapter(listAdapter);
        }

        notesText = (TextInputEditText) findViewById(R.id.bookmark_activity_notes);

        bindButton(R.id.bookmark_activity_button_save);
        bindButton(R.id.bookmark_activity_button_delete);
        bindButton(R.id.bookmark_activity_button_share);

        final DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        populateReferences(dbu);
        populateNotes(dbu);

        if (currentMode == MODE.VIEW){
            notesText.setFocusable(false);
        }
    }

    private void populateReferences(final DatabaseUtility dbu) {
        // FIXME: 11/6/16
        Log.i(TAG, "populateReferences: ");
        String dbReferences = dbu.isReferencePresent(verseReferences);
        if (null != dbReferences) {
            verseReferences = dbReferences;
            Log.i(TAG, "populateReferences: DB_References = " + dbReferences);
        }
        String individualReference[] = verseReferences.split("~");
        String verseText = "";
        StringBuilder enterText = new StringBuilder(0);
        Book.Details book;

        listAdapter.clear();
        for (String reference : individualReference) {
            String[] parts = reference.split(":");
            verseText = dbu.getSpecificVerse(
                    Integer.parseInt(parts[0]),  // Book
                    Integer.parseInt(parts[1]),  // Chapter
                    Integer.parseInt(parts[2])); // Verse

            book = Book.getBookDetails(Integer.parseInt(parts[0]));
            enterText.append(book.name).append(" (").append(parts[1]).append(":").append(parts[2])
                    .append(") - ").append(verseText);
            listAdapter.add(enterText.toString());
        }
        listAdapter.notifyDataSetChanged();
    }

    private void populateNotes(final DatabaseUtility dbu) {
        // FIXME: 11/6/16
        Log.i(TAG, "populateNotes: ");
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

    @Override
    public void onClick(View v) {
        String label = ((AppCompatButton) v).getText().toString();
        if (label.equalsIgnoreCase(getString(R.string.button_save))) {
            handleButtonSaveClicked();
        } else if (label.equalsIgnoreCase(getString(R.string.button_delete))) {
            handleButtonDeleteClicked();
        } else if (label.equalsIgnoreCase(getString(R.string.button_share))) {
            handleButtonShareClicked();
        } else {
            Log.i(TAG, "onClick: " + getString(R.string.how_am_i_here));
        }
    }

    private void handleButtonShareClicked() {
        // FIXME: 11/6/16
        Log.i(TAG, "handleButtonShareClicked: ");
    }

    private void handleButtonDeleteClicked() {
        // FIXME: 11/6/16
        Log.i(TAG, "handleButtonDeleteClicked: ");
    }

    private void handleButtonSaveClicked() {
        // FIXME: 11/6/16
        Log.i(TAG, "handleButtonSaveClicked: ");

    }

    enum MODE {EDIT, VIEW}
}
