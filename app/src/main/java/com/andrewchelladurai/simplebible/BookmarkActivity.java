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

    public static final  String REFERENCES    = "REFERENCES";
    public static final  String VERSE_TEXT    = "VERSE_TEXT";
    public static final  String ACTIVITY_MODE = "ACTIVITY_MODE";
    private static final String TAG           = "BookmarkActivity";
    private OPERATION mOperation;
    private String mReferences = "";
    private TextInputEditText    mNotesField;
    private ArrayAdapter<String> mVerseListAdapter;
    private AppCompatButton      mButtonSave;
    private AppCompatButton      mButtonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bookmark_activity_toolbar);
        setSupportActionBar(toolbar);

        mOperation = OPERATION.INSERT;
        MODE currentMode;
        if (getIntent().getStringExtra(ACTIVITY_MODE).equalsIgnoreCase(MODE.EDIT.toString())) {
            currentMode = MODE.EDIT;
        } else {
            currentMode = MODE.VIEW;
        }

        mReferences = getIntent().getStringExtra(REFERENCES);
        Log.d(TAG,
              "onCreate: REFERENCES = " +
              mReferences);

        ListViewCompat verseList = (ListViewCompat) findViewById(R.id.bookmark_activity_verse_list);
        if (null != verseList) {
            ArrayList<String> listItems = new ArrayList<>(0);
            mVerseListAdapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
            verseList.setAdapter(mVerseListAdapter);
        }

        mNotesField = (TextInputEditText) findViewById(R.id.bookmark_activity_notes);
        mButtonSave = bindButton(R.id.bookmark_activity_button_save);
        AppCompatButton buttonDelete = bindButton(R.id.bookmark_activity_button_delete);
        mButtonShare = bindButton(R.id.bookmark_activity_button_share);

        populateReferences();

        switch (currentMode) {
            case VIEW:
                mNotesField.setFocusable(false);
                mButtonSave.setVisibility(View.GONE);
                break;
            case EDIT:
                buttonDelete.setVisibility(View.GONE);
        }

        mButtonShare.setVisibility(View.GONE);

        setTitle(R.string.title_activity_bookmark);
    }

    private AppCompatButton bindButton(int pButtonID) {
        AppCompatButton button = (AppCompatButton) findViewById(pButtonID);
        assert button != null : TAG + ".bindButton() : Invalid ID " + pButtonID;
        button.setOnClickListener(this);
        return button;
    }

    private void populateReferences() {
        Log.d(TAG, "populateReferences()");
        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());

        String[] dbResult = dbu.isReferencePresent(mReferences);

        if (dbResult != null) {
            mReferences = dbResult[0];
            mOperation = OPERATION.UPDATE;
        }

        mNotesField.setText((dbResult != null) ? dbResult[1] : "");

        String[] references = mReferences.split("~");
        String verseText;
        StringBuilder enterText = new StringBuilder(0);
        Book.Details book;

        mVerseListAdapter.clear();
        for (String reference : references) {
            String[] parts = reference.split(":");
            verseText = dbu.getSpecificVerse(
                    Integer.parseInt(parts[0]),  // Book
                    Integer.parseInt(parts[1]),  // Chapter
                    Integer.parseInt(parts[2])); // Verse

            book = Book.getBookDetails(Integer.parseInt(parts[0]));
            enterText.append(book != null ? book.name : "Unknown Book").append(" (")
                     .append(parts[1]).append(":").append(parts[2]).append(") - ")
                     .append(verseText);

            mVerseListAdapter.add(enterText.toString());
            enterText.delete(0, enterText.length());
        }
        mVerseListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        String label = ((AppCompatButton) v).getText().toString();
        if (label.equalsIgnoreCase(getString(R.string.button_save))) {
            if (handleButtonSaveClicked()) {
                mNotesField.setFocusable(false);
                mButtonSave.setVisibility(View.GONE);
                mButtonShare.setVisibility(View.VISIBLE);
            }
        } else if (label.equalsIgnoreCase(getString(R.string.button_delete))) {
            if (handleButtonDeleteClicked()) {
                finish();
            }
        } else if (label.equalsIgnoreCase(getString(R.string.button_share))) {
            handleButtonShareClicked();
        } else {
            Log.i(TAG, "onClick: " + getString(R.string.how_am_i_here));
        }
    }

    private boolean handleButtonSaveClicked() {
        boolean successful = false;
        String notesText = mNotesField.getText().toString();

        Log.i(TAG, "handleButtonSaveClicked: [" + mReferences + "] [" + notesText + " ] [" +
                   mOperation.toString() + "]");

        if (mReferences.isEmpty()) {
            Snackbar.make(mNotesField, "No Verse Present to Bookmark",
                          Snackbar.LENGTH_SHORT).show();
            return false;
        }

        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        notesText = (notesText.isEmpty()) ? getString(R.string.empty_bookmark_notes) : notesText;

        String result = "Verse Notes could not be saved";
        switch (mOperation) {
            case INSERT:
                successful = dbu.createNewBookmark(mReferences, notesText);
                break;
            case UPDATE:
                successful = dbu.updateExistingBookmark(mReferences, notesText);
                break;
            default:
                Log.d(TAG, "handleButtonSaveClicked: " + getString(R.string.how_am_i_here));
        }
        result = (successful) ? "Verse Notes saved" : result;
        Snackbar.make(mNotesField, result, Snackbar.LENGTH_SHORT).show();
        return successful;
    }

    private boolean handleButtonDeleteClicked() {
        // FIXME: 11/6/16
//        boolean successful = false;
        Log.i(TAG, "handleButtonDeleteClicked: ");
//        DatabaseUtility dbu = DatabaseUtility.getInstance(getApplicationContext());
        return false;
    }

    private void handleButtonShareClicked() {
        // FIXME: 11/6/16
        Log.i(TAG, "handleButtonShareClicked: ");
    }

    enum MODE {EDIT, VIEW}

    enum OPERATION {UPDATE, INSERT}
}
