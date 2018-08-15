package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.VerseRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterScreen
    extends AppCompatActivity
    implements ChapterScreenOps {

    public static final  String BOOK_NUMBER    = "BOOK_NUMBER";
    public static final  String CHAPTER_NUMBER = "CHAPTER_NUMBER";
    private static final String TAG            = "ChapterScreen";
    private static final Bundle ARGS           = new Bundle();
    private static VerseRepository        sRepository;
    private static ChapterScreenPresenter sPresenter;
    private static VerseListAdapter       sAdapter;
    private RecyclerView mRecyclerView = null;
    private BottomAppBar         mBottomAppBar;
    private FloatingActionButton mFab;

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

        if (sRepository == null) {
            sRepository = ViewModelProviders.of(this).get(VerseRepository.class);
        }

        if (sPresenter == null) {
            sPresenter = new ChapterScreenPresenter(this);
        }

        if (sAdapter == null) {
            sAdapter = new VerseListAdapter(this);
        }

        mBottomAppBar = findViewById(R.id.act_chap_appbar);
        mBottomAppBar.setOnMenuItemClickListener(this);
        mBottomAppBar.replaceMenu(R.menu.chapter_screen_appbar);

        mFab = findViewById(R.id.act_chap_fab);
        mFab.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.act_chap_list);
        mRecyclerView.setAdapter(sAdapter);

        showChapter(getBookToShow(), getChapterToShow());
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BOOK_NUMBER, ARGS.getInt(BOOK_NUMBER));
        outState.putInt(CHAPTER_NUMBER, ARGS.getInt(CHAPTER_NUMBER));
    }

    private void showChapter(final int book, final int chapter) {
        Log.d(TAG, "showChapter: book = [" + book + "], chapter = [" + chapter + "]");

        ARGS.putInt(BOOK_NUMBER, book);
        ARGS.putInt(CHAPTER_NUMBER, chapter);

        sRepository.queryDatabase(book, chapter).observe(this, this);
    }

    @Override
    public void onChanged(final List<Verse> verses) {
        updateVerseList(verses);
    }

    private void updateVerseList(@Nullable final List<Verse> verses) {
        if (null == verses) {
            Log.e(TAG, "updateVerseList: No verses returned");
            return;
        }

        if (sPresenter.updateRepositoryCache(verses)) {
            updateTitle();
            sAdapter.updateList(verses, getBookToShow(), getChapterToShow());
            sAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
        } else {
            Log.e(TAG, "updateVerseList: error updating UI");
        }
    }

    private void updateTitle() {
        final String title = getString(R.string.act_chap_title);
        final String bookName = Utilities.getInstance().getBookName(getBookToShow());
        TextView textView = findViewById(R.id.act_chap_titlebar);
        textView.setText(String.format(title, bookName, getChapterToShow()));
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
    public int getBookToShow() {
        return (ARGS.containsKey(BOOK_NUMBER) ? ARGS.getInt(BOOK_NUMBER) : 0);
    }

    @IntRange(from = 1)
    @Override
    public int getChapterToShow() {
        return (ARGS.containsKey(CHAPTER_NUMBER) ? ARGS.getInt(CHAPTER_NUMBER) : 0);
    }

    public void handleInteractionClickChapter(@NonNull final Integer chapterNumber) {
/*
        if (chapterListDialog != null) {
            chapterListDialog.dismiss();
            chapterListDialog = null;
        }
*/
        showChapter(getBookToShow(), chapterNumber);
    }

    public void showErrorFirstChapter() {
        Snackbar.make(findViewById(R.id.act_chap_list), R.string.act_chap_err_first_chapter,
                      Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorLastChapter() {
        Snackbar.make(findViewById(R.id.act_chap_list), R.string.act_chap_err_last_chapter,
                      Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorEmptySelectedList() {
        Snackbar.make(findViewById(R.id.act_chap_list), R.string.act_chap_err_empty_selection_list,
                      Snackbar.LENGTH_SHORT).show();
    }

    public void handleInteractionClickSettings() {
        throw new UnsupportedOperationException(getString(R.string.msg_unexpected));
    }

    private void showMessageDiscardSelectedVerses() {
        Snackbar.make(findViewById(R.id.act_chap_list),
                      R.string.act_chap_msg_discarded_selected_verses,
                      Snackbar.LENGTH_SHORT).show();
    }

    private void showErrorInvalidBookmarkReference() {
        Snackbar.make(findViewById(R.id.act_chap_list),
                      R.string.act_chap_err_invalid_bookmark_reference, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_chap_menu_prev:
                actionPrevClicked();
                break;
            case R.id.act_chap_menu_next:
                actionNextClicked();
                break;
            case R.id.act_chap_menu_share:
                actionShareClicked();
                break;
            case R.id.act_chap_menu_bookmark:
                actionBookmarkClicked();
                break;
            case R.id.act_chap_menu_clear:
                actionClearClicked();
                break;
            case R.id.act_chap_menu_settings:
                actionSettingsClicked();
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
                actionListClicked();
                break;
            default:
                Log.e(TAG, "onClick: Unhandled click event" + getString(R.string.msg_unexpected));
        }
    }

    private void actionSettingsClicked() {
        Log.d(TAG, "actionSettingsClicked:");
    }

    private void actionClearClicked() {
        Log.d(TAG, "actionClearClicked:");
        boolean anyVerseSelected = sAdapter.isAnyVerseSelected();
        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }
        anyVerseSelected = sAdapter.discardSelectedVerses();
        if (!anyVerseSelected) {
            showMessageDiscardSelectedVerses();
            sAdapter.notifyDataSetChanged();
        }
    }

    private void actionBookmarkClicked() {
        Log.d(TAG, "actionBookmarkClicked:");

        if (!sAdapter.isAnyVerseSelected()) {
            showErrorEmptySelectedList();
            return;
        }

        final String references = sPresenter.getSelectedVerseReferences();

        if (references == null || references.isEmpty()) {
            Log.e(TAG, "got Empty or Invalid bookmarkReference");
            showErrorInvalidBookmarkReference();
            return;
        }

/*
        Bundle bundle = new Bundle();
        bundle.putString(BookmarkScreen.REFERENCES, references);
        final Intent intent = new Intent(this, BookmarkScreen.class);
        intent.putExtras(bundle);
*/

        sAdapter.discardSelectedVerses();
        sAdapter.notifyDataSetChanged();

//        startActivity(intent);

    }

    private void actionNextClicked() {
        Log.d(TAG, "actionNextClicked:");
        int newChapter = getChapterToShow() + 1;
        Book book = Utilities.getInstance().getBookUsingNumber(getBookToShow());
        if (book != null && newChapter > book.getChapters()) {
            showErrorLastChapter();
            return;
        }
        showChapter(getBookToShow(), newChapter);
    }

    private void actionPrevClicked() {
        Log.d(TAG, "actionPrevClicked:");
        int newChapter = getChapterToShow() - 1;
        if (newChapter < 1) {
            showErrorFirstChapter();
            return;
        }
        showChapter(getBookToShow(), newChapter);
    }

    private void actionListClicked() {
        Log.d(TAG, "actionListClicked:");
        final Book book = Utilities.getInstance().getBookUsingNumber(getBookToShow());
        if (book == null) {
            Log.e(TAG, "handleInteractionClickList: invalid book returned");
            return;
        }
/*
        chapterListDialog = ChapterListDialog.newInstance(this, book.getChapters());
        chapterListDialog.show(getSupportFragmentManager(), "ChapterListDialog");
*/
    }

    private void actionShareClicked() {
        final boolean anyVerseSelected = sAdapter.isAnyVerseSelected();
        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }

        final String selectedVerses = sPresenter.getSelectedVersesTextToShare(
            getString(R.string.content_item_verse_text_template));

        final String textToShare = String.format(getString(R.string.act_chap_template_share),
                                                 selectedVerses,
                                                 getTitleDisplay());
        Log.d(TAG, "actionShareClicked: [selectedVerses=" + selectedVerses.length()
                   + " chars][textToShare=" + textToShare.length() + " chars]");

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");

        sAdapter.discardSelectedVerses();
        sAdapter.notifyDataSetChanged();

        startActivity(sendIntent);
    }

    @Override
    public void handleInteractionClickVerseItem(final Verse verse) {
        verse.setSelected(!verse.isSelected());
    }

}
