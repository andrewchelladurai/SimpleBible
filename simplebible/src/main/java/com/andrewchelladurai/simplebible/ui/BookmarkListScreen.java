package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.repository.BookmarkRepository;
import com.andrewchelladurai.simplebible.presenter.BookmarkListScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkListScreenOps;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarkListScreen
    extends AppCompatActivity
    implements BookmarkListScreenOps {

    private static final String TAG = "BookmarkListScreen";

    private static BookmarkListScreenPresenter mPresenter  = null;
    private static BookmarkListAdapter         mAdapter    = null;
    private static BookmarkRepository          mRepository = null;
    private static String mHeaderTemplate;
    private static String mDetailTemplate;
    private static String mEmptyNote;
    private static String mVerseDisplayTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);

        if (mRepository == null
            || mAdapter == null
            || mPresenter == null
            || mHeaderTemplate == null
            || mDetailTemplate == null
            || mEmptyNote == null
            || mVerseDisplayTemplate == null) {
            mPresenter = new BookmarkListScreenPresenter(this);
            mAdapter = new BookmarkListAdapter(this);
            mRepository = ViewModelProviders.of(this).get(BookmarkRepository.class);

            mHeaderTemplate = getString(R.string.item_bookmark_header_template);
            mDetailTemplate = getString(R.string.item_bookmark_detail_template);
            mEmptyNote = getString(R.string.item_bookmark_empty_note);
            mVerseDisplayTemplate = getString(R.string.content_bookmark_item_reference_template);
        }

        mRepository.queryDatabase().observe(this, this);

        final RecyclerView listView = findViewById(R.id.act_blist_list);
        listView.setAdapter(mAdapter);
    }

    @Override
    public String getFormattedBookmarkHeader(@NonNull final Bookmark bookmark) {
        final int verseCount = mPresenter.getVerseCount(bookmark);

        final String firstVerse = mPresenter.getFirstVerse(bookmark, mVerseDisplayTemplate);

        return String.format(mHeaderTemplate, verseCount, firstVerse);
    }

    @Override
    public String getFormattedBookmarkDetails(@NonNull final Bookmark bookmark) {
        final String note = bookmark.getNote();
        if (note.isEmpty()) {
            return mEmptyNote;
        }

        return String.format(mDetailTemplate, note);
    }

    @Override
    public void handleActionBookmarkClick(@NonNull final Bookmark bookmark) {
        Log.d(TAG, "handleActionBookmarkClick() called with: bookmark = [" + bookmark + "]");
    }

    @Override
    public void handleActionDelete(@NonNull final Bookmark bookmark) {
        Log.d(TAG, "handleActionDelete() called with: bookmark = [" + bookmark + "]");
    }

    @Override
    public void handleActionShare(@NonNull final Bookmark bookmark) {
        Log.d(TAG, "handleActionShare() called with: bookmark = [" + bookmark + "]");
    }

    @Override
    public void onChanged(@NonNull final List<Bookmark> list) {
        updateList(list);
    }

    private void updateList(@NonNull final List<Bookmark> list) {
        Log.d(TAG, "updateList: [" + list.size() + "] records retrieved");
        mAdapter.updateList(list);
        mAdapter.notifyDataSetChanged();
    }

    private void showErrorCacheUpdateFailed() {
        Log.d(TAG, "showErrorCacheUpdateFailed() called");
    }
}
