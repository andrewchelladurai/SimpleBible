package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.VerseRepository;
import com.andrewchelladurai.simplebible.presenter.ChapterScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.VerseListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterScreen
    extends AppCompatActivity
    implements ChapterScreenOps {

    public static final String BOOK_NUMBER    = "BOOK_NUMBER";
    public static final String CHAPTER_NUMBER = "CHAPTER_NUMBER";

    private static final String TAG  = "ChapterScreen";
    private static final Bundle ARGS = new Bundle();

    private ChapterScreenPresenter mPresenter;
    private VerseListAdapter       mAdapter;
    private RecyclerView           mRecyclerView;
    private BottomAppBar           mBottomAppBar;
    private FloatingActionButton   mFab;
    private ChapterListDialog      mChapterDialog;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_chapter);

        if (savedState != null && savedState.containsKey(BOOK_NUMBER) && savedState.containsKey(
            CHAPTER_NUMBER)) {
            ARGS.putInt(BOOK_NUMBER, savedState.getInt(BOOK_NUMBER));
            ARGS.putInt(CHAPTER_NUMBER, savedState.getInt(CHAPTER_NUMBER));
        } else {
            ARGS.putInt(BOOK_NUMBER, getIntent().getIntExtra(BOOK_NUMBER, 0));
            ARGS.putInt(CHAPTER_NUMBER, getIntent().getIntExtra(CHAPTER_NUMBER, 0));
        }

        VerseRepository mRepository = ViewModelProviders.of(this).get(VerseRepository.class);
        mPresenter = new ChapterScreenPresenter(this, mRepository);
        mAdapter = new VerseListAdapter(this);

        mBottomAppBar = findViewById(R.id.act_chap_appbar);
        mBottomAppBar.setOnMenuItemClickListener(this);
        mBottomAppBar.replaceMenu(R.menu.chapter_screen_appbar);

        mFab = findViewById(R.id.act_chap_fab);
        mFab.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.act_chap_list);
        mRecyclerView.setAdapter(mAdapter);

        showChapter(getBook(), getChapter());
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        if (mChapterDialog != null) {
            mChapterDialog.dismiss();
            mChapterDialog = null;
        }
        super.onSaveInstanceState(outState);
        outState.putInt(BOOK_NUMBER, ARGS.getInt(BOOK_NUMBER));
        outState.putInt(CHAPTER_NUMBER, ARGS.getInt(CHAPTER_NUMBER));
    }

/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroyCache();
    }
*/

    private void showChapter(final int book, final int chapter) {
        Log.d(TAG, "showChapter: book = [" + book + "], chapter = [" + chapter + "]");

        ARGS.putInt(BOOK_NUMBER, book);
        ARGS.putInt(CHAPTER_NUMBER, chapter);

        VerseRepository.getInstance().queryDatabase(book, chapter)
                       .observe(this, new Observer<List<Verse>>() {
                           @Override
                           public void onChanged(final List<Verse> list) {
                               updateVerseList(list);
                           }
                       });
    }

    private void updateVerseList(@Nullable final List<Verse> verses) {
        if (null == verses || verses.isEmpty()) {
            Log.e(TAG, "updateVerseList: No verses returned");
            // FIXME: 16/8/18 This should show an error on screen asking to inform dev
            // FIXME: 16/8/18 hopefully it never shows to anyone
            return;
        }

        final int book = getBook();
        final int chapter = getChapter();

        if (mPresenter.populateCache(verses, book, chapter)) {
            updateTitle();
            mAdapter.updateList(verses, book, chapter);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
        } else {
            // FIXME: 16/8/18 This should show an error on screen asking to inform dev
            // FIXME: 16/8/18 hopefully it never shows to anyone
            Log.e(TAG, "updateVerseList: error updating UI");
        }
    }

    private void updateTitle() {
        final String title = getString(R.string.act_chap_title);
        final String bookName = Utilities.getInstance().getBookName(getBook());
        TextView textView = findViewById(R.id.act_chap_titlebar);
        textView.setText(String.format(title, bookName, getChapter()));
    }

    private String getTitleDisplay() {
        final TextView textView = findViewById(R.id.act_chap_titlebar);
        return textView.getText().toString();
    }

    @NonNull
    @Override
    public String getVerseTemplateString() {
        return getString(R.string.content_item_verse_text_template);
    }

    @NonNull
    @Override
    public Context getSystemContext() {
        return getApplicationContext();
    }

    @IntRange(from = 1, to = 66)
    @Override
    public int getBook() {
        return (ARGS.containsKey(BOOK_NUMBER) ? ARGS.getInt(BOOK_NUMBER) : 0);
    }

    @IntRange(from = 1)
    @Override
    public int getChapter() {
        return (ARGS.containsKey(CHAPTER_NUMBER) ? ARGS.getInt(CHAPTER_NUMBER) : 0);
    }

    @Override
    public void handleInteractionChapterClicked(
        @IntRange(from = 1, to = 66) final int chapterNumber) {
        if (mChapterDialog != null) {
            mChapterDialog.dismiss();
            mChapterDialog = null;
        }
        showChapter(getBook(), chapterNumber);
    }

    public void showErrorFirstChapter() {
        Utilities.getInstance().createSnackBar(
            findViewById(R.id.act_chap_list),
            R.string.act_chap_err_first_chapter,
            Snackbar.LENGTH_SHORT,
            getResources().getColor(R.color.act_chap_snackbar_text),
            getResources().getColor(R.color.act_chap_snackbar)).show();
    }

    public void showErrorLastChapter() {
        Utilities.getInstance().createSnackBar(
            findViewById(R.id.act_chap_list),
            R.string.act_chap_err_last_chapter,
            Snackbar.LENGTH_SHORT,
            getResources().getColor(R.color.act_chap_snackbar_text),
            getResources().getColor(R.color.act_chap_snackbar)).show();
    }

    public void showErrorEmptySelectedList() {
        Utilities.getInstance().createSnackBar(
            findViewById(R.id.act_chap_list),
            R.string.act_chap_err_empty_selection_list,
            Snackbar.LENGTH_SHORT,
            getResources().getColor(R.color.act_chap_snackbar_text),
            getResources().getColor(R.color.act_chap_snackbar)).show();
    }

    private void showMessageDiscardSelectedVerses() {
        Utilities.getInstance().createSnackBar(
            findViewById(R.id.act_chap_list),
            R.string.act_chap_msg_discarded_selected_verses,
            Snackbar.LENGTH_SHORT,
            getResources().getColor(R.color.act_chap_snackbar_text),
            getResources().getColor(R.color.act_chap_snackbar)).show();
    }

    private void showErrorInvalidBookmarkReference() {
        Utilities.getInstance().createSnackBar(
            findViewById(R.id.act_chap_list),
            R.string.act_chap_err_invalid_bookmark_reference,
            Snackbar.LENGTH_SHORT,
            getResources().getColor(R.color.act_chap_snackbar_text),
            getResources().getColor(R.color.act_chap_snackbar)).show();
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_chap_menu_prev:
                handleInteractionPrev();
                break;
            case R.id.act_chap_menu_next:
                handleInteractionNext();
                break;
            case R.id.act_chap_menu_share:
                handleInteractionShare();
                break;
            case R.id.act_chap_menu_bookmark:
                handleInteractionBookmark();
                break;
            case R.id.act_chap_menu_clear:
                handleInteractionClear();
                break;
            case R.id.act_chap_menu_settings:
                handleInteractionSettings();
                break;
            default:
                Log.e(TAG, "onClick: Unhandled click event" + getString(R.string.msg_unexpected));
        }
        return true;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_chap_fab:
                handleInteractionList();
                break;
            default:
                Log.e(TAG, "onClick: Unhandled click event" + getString(R.string.msg_unexpected));
        }
    }

    private void handleInteractionSettings() {
        Log.d(TAG, "handleInteractionSettings:");
    }

    private void handleInteractionClear() {
        boolean anyVerseSelected = mAdapter.isAnyVerseSelected();

        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }

        anyVerseSelected = mAdapter.discardSelectedVerses();
        if (!anyVerseSelected) {
            showMessageDiscardSelectedVerses();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void handleInteractionBookmark() {
        if (!mAdapter.isAnyVerseSelected()) {
            showErrorEmptySelectedList();
            return;
        }

        final String references =
            mPresenter.getSelectedVerseReferences(mAdapter.getSelectedVerses());

        if (references == null || references.isEmpty()) {
            Log.e(TAG, "got Empty or Invalid bookmarkReference");
            showErrorInvalidBookmarkReference();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(BookmarkScreen.REFERENCES, references);
        final Intent intent = new Intent(this, BookmarkScreen.class);
        intent.putExtras(bundle);

        mAdapter.discardSelectedVerses();
        mAdapter.notifyDataSetChanged();

        startActivity(intent);
    }

    private void handleInteractionNext() {
        int newChapter = getChapter() + 1;
        Book book = Utilities.getInstance().getBookUsingNumber(getBook());
        if (book != null && newChapter > book.getBookChapterCount()) {
            showErrorLastChapter();
            return;
        }
        showChapter(getBook(), newChapter);
    }

    private void handleInteractionPrev() {
        int newChapter = getChapter() - 1;
        if (newChapter < 1) {
            showErrorFirstChapter();
            return;
        }
        showChapter(getBook(), newChapter);
    }

    private void handleInteractionList() {
        if (mChapterDialog != null) {
            mChapterDialog.dismiss();
            mChapterDialog = null;
        }

        final Book book = mPresenter.getBook(getBook());
        if (book == null) {
            Log.e(TAG, "handleInteractionClickList: invalid book returned");
            return;
        }

        mChapterDialog = ChapterListDialog.newInstance(this, book.getBookChapterCount());
        mChapterDialog.show(getSupportFragmentManager(), "ChapterListDialog");
    }

    private void handleInteractionShare() {
        final boolean anyVerseSelected = mAdapter.isAnyVerseSelected();
        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }

        final String selectedVerses =
            mPresenter.getSelectedVersesTextToShare(mAdapter.getSelectedVerses(), getString(
                R.string.content_item_verse_text_template));

        final String textToShare = String.format(getString(R.string.act_chap_template_share),
                                                 selectedVerses,
                                                 getTitleDisplay());

        Log.d(TAG, "handleInteractionShare: " + textToShare);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");

        mAdapter.discardSelectedVerses();
        mAdapter.notifyDataSetChanged();

        startActivity(sendIntent);
    }

    @Override
    public void handleInteractionVerseClicked(final Verse verse) {
        verse.setSelected(!verse.isSelected());
    }

}
