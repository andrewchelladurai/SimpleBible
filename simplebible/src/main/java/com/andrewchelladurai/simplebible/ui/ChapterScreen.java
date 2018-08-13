package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.presenter.ChapterScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.VerseListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterScreen
    extends AppCompatActivity
    implements ChapterScreenOps {

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

        RecyclerView recyclerView = findViewById(R.id.act_chapter_list);
        recyclerView.setAdapter(sAdapter);
    }

    @Override
    public void handleInteractionClickVerseItem(final Verse verse) {
        Log.d(TAG, "handleInteractionClickVerseItem: called with verse = [" + verse + "]");
    }
}
