package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.presenter.BookmarkScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkVerseAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarkScreen
    extends AppCompatActivity
    implements BookmarkScreenOps, View.OnClickListener {

    public static final  String REFERENCES = "REFERENCES";
    private static final String TAG        = "BookmarkScreen";

    private static BookmarkScreenPresenter presenter = null;
    private static BookmarkVerseAdapter sAdapter;

    private String            mReferences;
    private RecyclerView      mRecyclerView;
    private TextInputEditText mNoteField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        if (sAdapter == null) {
            sAdapter = new BookmarkVerseAdapter(this);
        }

        if (presenter == null) {
            presenter = new BookmarkScreenPresenter(this);
        }

        mRecyclerView = findViewById(R.id.act_bmrk_list);
        mRecyclerView.setAdapter(sAdapter);

        mNoteField = findViewById(R.id.act_bmrk_note);

        BottomAppBar mBottomAppBar = findViewById(R.id.act_bmrk_appbar);
        mBottomAppBar.replaceMenu(R.menu.bookmark_screen_appbar);
        mBottomAppBar.setOnMenuItemClickListener(this);

        FloatingActionButton mFab = findViewById(R.id.act_bmrk_fab);
        mFab.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(REFERENCES)) {
            mReferences = extras.getString(REFERENCES);
            if (mReferences != null && !mReferences.isEmpty()) {
                Log.d(TAG, "onCreate: passed reference [" + mReferences + "]");
                if (!Utilities.getInstance().isValidBookmarkReference(mReferences)) {
                    Log.e(TAG, "onCreate: invalid bookmark reference");
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_bmrk_menu_save:
                handleClickButSave();
                return true;
            case R.id.act_bmrk_menu_edit:
                handleClickButEdit();
                return true;
            case R.id.act_bmrk_menu_delete:
                handleClickButDelete();
                return true;
            case R.id.act_bmrk_menu_share:
                handleClickButShare();
                return true;
            case R.id.act_bmrk_menu_clear:
                handleClickButReset();
                return true;
            case R.id.act_bmrk_menu_settings:
                handleClickButSettings();
                return true;
            default:
                Log.e(TAG, "onMenuItemClick: " + getString(R.string.msg_unexpected));
        }
        return false;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_bmrk_fab:
                // handle whichever action is required
                break;
            default:
                Log.e(TAG, "onClick: " + getString(R.string.msg_unexpected));
        }
    }

    @Override
    public void handleInteractionClick(@NonNull Verse verse) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Context getSystemContent() {
        return getApplicationContext();
    }

    @NonNull
    @Override
    public String getReferences() {
        return mReferences;
    }

    @Override
    public void handleClickButSave() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleClickButEdit() {
        mNoteField.setEnabled(true);
    }

    @Override
    public void handleClickButDelete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleClickButShare() {
        throw new UnsupportedOperationException();
    }

    @Override
    @NonNull
    public String getNote() {
        return mNoteField.getText().toString().trim();
    }

    @Override
    public void handleClickButReset() {
    }

    @Override
    public void handleClickButSettings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showMessageSaved() {
        Snackbar.make(mNoteField, getString(R.string.act_bmrk_msg_saved),
                      Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showErrorSaveFailed() {
        Snackbar.make(mNoteField, getString(R.string.act_bmrk_err_save_failed),
                      Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showErrorDeleteFailed() {
        Snackbar.make(mNoteField, getString(R.string.act_bmrk_err_delete_failed),
                      Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showMessageDeleted() {
        Snackbar.make(mNoteField, getString(R.string.act_bmrk_msg_deleted),
                      Snackbar.LENGTH_SHORT)
                .show();
    }

}
