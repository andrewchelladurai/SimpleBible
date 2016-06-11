package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class BookmarkActivity
        extends AppCompatActivity implements View.OnClickListener {

    public static final String REFERENCES = "REFERENCES";
    public static final String VERSE_TEXT = "VERSE_TEXT";
    public static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    private static final String TAG = "BookmarkActivity";
    private MODE currentMode;
    private String verseReferences = "";

    ;
    private ListViewCompat verseList;
    private AppCompatEditText notesText;

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

        verseList = (ListViewCompat) findViewById(R.id.bookmark_activity_verse_list);
        notesText = (AppCompatEditText) findViewById(R.id.bookmark_activity_notes);

        bindButton(R.id.bookmark_activity_button_save);
        bindButton(R.id.bookmark_activity_button_delete);
        bindButton(R.id.bookmark_activity_button_cancel);

        final DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        populateReferences(dbu);
        populateNotes(dbu);
    }

    private void populateReferences(final DatabaseUtility dbu) {
        // FIXME: 11/6/16
        Log.i(TAG, "populateReferences: ");
    }

    private void populateNotes(final DatabaseUtility dbu) {
        // FIXME: 11/6/16
        Log.i(TAG, "populateNotes: ");
    }

    private void bindButton(int resourceID) {
        AppCompatButton button = (AppCompatButton) findViewById(resourceID);
        if (button != null) {
            if (currentMode == MODE.VIEW) {
                button.setVisibility(View.GONE);
                return;
            }
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        String label = ((AppCompatButton) v).getText().toString();
        if (label.equalsIgnoreCase(getString(R.string.button_save))) {
            handleButtonSaveClicked();
        } else if (label.equalsIgnoreCase(getString(R.string.button_delete))) {
            handleButtonDeleteClicked();
        } else if (label.equalsIgnoreCase(getString(R.string.button_cancel))) {
            handleButtonCancelClicked();
        } else {
            Log.i(TAG, "onClick: " + getString(R.string.how_am_i_here));
        }
    }

    private void handleButtonCancelClicked() {
        // FIXME: 11/6/16
        Log.i(TAG, "handleButtonCancelClicked: ");
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
