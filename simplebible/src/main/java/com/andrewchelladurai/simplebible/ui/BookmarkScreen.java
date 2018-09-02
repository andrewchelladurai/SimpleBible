package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.BookmarkRepository;
import com.andrewchelladurai.simplebible.data.repository.BookmarkVerseRepository;
import com.andrewchelladurai.simplebible.presenter.BookmarkScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkVerseAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BookmarkScreen
    extends AppCompatActivity
    implements BookmarkScreenOps {

    private static final String TAG        = "BookmarkScreen";
    public static final  String REFERENCES = "REFERENCES";

    private BookmarkScreenPresenter mPresenter;
    private BookmarkVerseAdapter    mAdapter;
    private String                  mReferences;
    private TextInputEditText       mNoteField;
    private TextView                mTitleBar;
    private FloatingActionButton    mFab;
    private int                     mFabAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        BookmarkRepository bookmarkRepository =
            ViewModelProviders.of(this).get(BookmarkRepository.class);

        BookmarkVerseRepository bookmarkVerseRepository =
            ViewModelProviders.of(this).get(BookmarkVerseRepository.class);

        mAdapter = new BookmarkVerseAdapter(this);
        mPresenter = new BookmarkScreenPresenter(this, bookmarkRepository, bookmarkVerseRepository);

        mTitleBar = findViewById(R.id.act_bmrk_titlebar);

        RecyclerView recyclerView = findViewById(R.id.act_bmrk_list);
        recyclerView.setAdapter(mAdapter);

        mNoteField = findViewById(R.id.act_bmrk_note);

        BottomAppBar bottomAppBar = findViewById(R.id.act_bmrk_appbar);
        bottomAppBar.replaceMenu(R.menu.bookmark_screen_appbar);
        bottomAppBar.setOnMenuItemClickListener(this);

        mFab = findViewById(R.id.act_bmrk_fab);
        mFab.setOnClickListener(this);

        final Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(REFERENCES)) {
            // FIXME: 16/8/18 show an error to contact dev
            // FIXME: 16/8/18 hopefully no one sees it
            throw new UnsupportedOperationException(
                "onCreate: no extras or REFERENCES value passed in intent bundle");
        }

        mReferences = extras.getString(REFERENCES);

        if (mReferences == null || mReferences.isEmpty()) {
            // FIXME: 16/8/18 show an error to contact dev
            // FIXME: 16/8/18 hopefully no one sees it
            throw new UnsupportedOperationException(
                "onCreate: null or empty REFERENCES passed in intent bundle");
        }

        if (!Utilities.getInstance().isValidBookmarkReference(mReferences)) {
            // FIXME: 16/8/18 show an error to contact dev
            // FIXME: 16/8/18 hopefully no one sees it
            throw new UnsupportedOperationException(
                "onCreate: invalid references[" + mReferences + "] passed");
        }

        bookmarkVerseRepository.queryBookmarkVerses(mReferences)
                               .observe(this, new Observer<List<Verse>>() {
                                   @Override
                                   public void onChanged(final List<Verse> list) {
                                       updateVerseList(list);
                                   }
                               });

        bookmarkRepository.queryBookmarkUsingReference(mReferences)
                          .observe(this, new Observer<List<Bookmark>>() {
                              @Override
                              public void onChanged(final List<Bookmark> list) {
                                  updateNoteField(list);
                              }
                          });
        showCorrectActions();
    }

    private void updateNoteField(final List<Bookmark> list) {
        if (list == null || list.isEmpty()) {
            Log.e(TAG, "updateNoteField: empty bookmark list");
            return;
        }
        mNoteField.setText(list.get(0).getNote());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mPresenter.clearRepositoryCache()) {
            Log.e(TAG, "onDestroy: failed to clear repository cache");
        }
    }

    private void updateVerseList(final List<Verse> verses) {
        if (verses == null || verses.isEmpty()) {
            // FIXME: 16/8/18 show an error to user on screen that hopefully no one sees ever
            throw new UnsupportedOperationException(
                "updateVerseList: empty list returned from live data");
        }

        if (!mPresenter.populateCache(verses, mReferences)) {
            // FIXME: 16/8/18 show an error to user on screen that hopefully no one sees ever
            throw new UnsupportedOperationException(
                "updateVerseList: could nto populate cache");
        }

        mAdapter.updateList(verses, mReferences);
    }

    private void showCorrectActions() {
        // update actions and title depending on passed reference being present in DB
        mPresenter.doesBookmarkExist(mReferences).observe(this, new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(final List<Bookmark> list) {
                if (list == null || list.isEmpty()) {
                    findViewById(R.id.act_bmrk_menu_save).setVisibility(GONE);
                    findViewById(R.id.act_bmrk_menu_edit).setVisibility(GONE);
                    findViewById(R.id.act_bmrk_menu_delete).setVisibility(GONE);
                    // findViewById(R.id.act_bmrk_menu_share).setVisibility(VISIBLE);
                    // findViewById(R.id.act_bmrk_menu_settings).setVisibility(VISIBLE);
                    mFabAction = R.id.act_bmrk_menu_save;
                    mFab.setImageResource(R.drawable.ic_save);
                    mTitleBar.setText(R.string.act_bmrk_titlebar_create);
                } else {
                    findViewById(R.id.act_bmrk_menu_save).setVisibility(GONE);
                    findViewById(R.id.act_bmrk_menu_edit).setVisibility(GONE);
                    findViewById(R.id.act_bmrk_menu_delete).setVisibility(VISIBLE);
                    // findViewById(R.id.act_bmrk_menu_share).setVisibility(VISIBLE);
                    // findViewById(R.id.act_bmrk_menu_settings).setVisibility(VISIBLE);
                    mFabAction = R.id.act_bmrk_menu_edit;
                    mFab.setImageResource(R.drawable.ic_edit);
                    mTitleBar.setText(R.string.act_bmrk_titlebar_edit);
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_bmrk_menu_save:
                handleInteractionSave();
                return true;
            case R.id.act_bmrk_menu_edit:
                handleInteractionEdit();
                return true;
            case R.id.act_bmrk_menu_delete:
                handleInteractionDelete();
                return true;
            case R.id.act_bmrk_menu_share:
                handleInteractionShare();
                return true;
            case R.id.act_bmrk_menu_settings:
                handleInteractionSettings();
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
                if (mFabAction == R.id.act_bmrk_menu_save) {
                    handleInteractionSave();
                } else if (mFabAction == R.id.act_bmrk_menu_edit) {
                    handleInteractionEdit();
                } else {
                    Log.e(TAG, "onClick: unknown actions set to Fab"
                               + getString(R.string.msg_unexpected));
                }
                break;
            default:
                Log.e(TAG, "onClick: " + getString(R.string.msg_unexpected));
        }
    }

    @Override
    public void handleInteractionClick(@NonNull Verse verse) {
        Log.d(TAG, "handleInteractionClick: not implemented");
        //        throw new UnsupportedOperationException();
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
    public void handleInteractionSave() {
        Utilities.getInstance().hideKeyboard(this, mNoteField);
        mPresenter.createBookmark(getReferences(), getNote());
    }

    @Override
    public void handleInteractionEdit() {
        mNoteField.setEnabled(true);
    }

    @Override
    public void handleInteractionDelete() {
        mPresenter.deleteBookmark(getReferences(), getNote());
    }

    @Override
    public void handleInteractionShare() {
        String textToShare = mPresenter.formatBookmarkToShare(
            getNote(),
            getString(R.string.content_bookmark_item_reference_template),
            getString(R.string.act_bmrk_template_share));

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");
    }

    @Override
    @NonNull
    public String getNote() {
        return mNoteField.getText().toString().trim();
    }

    @Override
    public void handleInteractionSettings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showMessageSaved() {
        Utilities utilities = Utilities.getInstance();
        utilities.hideKeyboard(getApplicationContext(), mNoteField);
        utilities.createSnackBar(
            findViewById(R.id.act_bmrk_list),
            R.string.act_bmrk_msg_saved,
            Snackbar.LENGTH_LONG,
            getResources().getColor(R.color.act_bmrk_snackbar_text),
            getResources().getColor(R.color.act_bmrk_snackbar)).show();
    }

    @Override
    public void showErrorSaveFailed() {
        Utilities utilities = Utilities.getInstance();
        utilities.hideKeyboard(getApplicationContext(), mNoteField);
        utilities.createSnackBar(
            findViewById(R.id.act_bmrk_list),
            R.string.act_bmrk_err_save_failed,
            Snackbar.LENGTH_LONG,
            getResources().getColor(R.color.act_bmrk_snackbar_text),
            getResources().getColor(R.color.act_bmrk_snackbar)).show();
    }

    @Override
    public void showErrorDeleteFailed() {
        Utilities utilities = Utilities.getInstance();
        utilities.hideKeyboard(getApplicationContext(), mNoteField);
        utilities.createSnackBar(
            findViewById(R.id.act_bmrk_list),
            R.string.act_bmrk_err_delete_failed,
            Snackbar.LENGTH_LONG,
            getResources().getColor(R.color.act_bmrk_snackbar_text),
            getResources().getColor(R.color.act_bmrk_snackbar)).show();
    }

    @Override
    public void showMessageDeleted() {
        Utilities utilities = Utilities.getInstance();
        utilities.hideKeyboard(getApplicationContext(), mNoteField);
        utilities.createSnackBar(
            findViewById(R.id.act_bmrk_list),
            R.string.act_bmrk_msg_deleted,
            Snackbar.LENGTH_LONG,
            getResources().getColor(R.color.act_bmrk_snackbar_text),
            getResources().getColor(R.color.act_bmrk_snackbar)).show();
    }

}
