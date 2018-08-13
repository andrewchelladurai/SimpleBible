package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.presenter.ChapterScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.VerseListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterScreen
    extends AppCompatActivity
    implements ChapterScreenOps, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    public static final  String BOOK_NUMBER    = "BOOK_NUMBER";
    public static final  String CHAPTER_NUMBER = "CHAPTER_NUMBER";
    private static final String TAG            = "ChapterScreen";
    private static ChapterScreenPresenter sPresenter;
    private static VerseListAdapter       sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        if (sPresenter == null) {
            sPresenter = new ChapterScreenPresenter(this);
        }

        if (sAdapter == null) {
            sAdapter = new VerseListAdapter(this);
        }

        BottomAppBar bar = findViewById(R.id.act_chapter_appbar);
        bar.setOnMenuItemClickListener(this);
        bar.replaceMenu(R.menu.chapter_screen_appbar);
        findViewById(R.id.act_chapter_fab).setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.act_chapter_list);
        recyclerView.setAdapter(sAdapter);
    }

    @Override
    public void handleInteractionClickVerseItem(final Verse verse) {
        Log.d(TAG, "handleInteractionClickVerseItem: called with verse = [" + verse + "]");
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        return false;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_chapter_fab:
                handleInteractionClickFab();
                break;
            default:
                Log.e(TAG, "onClick: Unhandled click event" + getString(R.string.msg_unexpected));
        }
    }

    private void handleInteractionClickFab() {
        Log.d(TAG, "handleInteractionClickFab: called");
    }
}
