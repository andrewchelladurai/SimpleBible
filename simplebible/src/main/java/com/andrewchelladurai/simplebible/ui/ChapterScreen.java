package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.presenter.ChapterScreenPresenter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;

import androidx.appcompat.app.AppCompatActivity;

public class ChapterScreen
    extends AppCompatActivity
    implements ChapterScreenOps {

    public static final  String BOOK_NUMBER    = "BOOK_NUMBER";
    public static final  String CHAPTER_NUMBER = "CHAPTER_NUMBER";
    private static final String TAG            = "ChapterScreen";
    private static ChapterScreenPresenter sPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        if (sPresenter == null) {
            sPresenter = new ChapterScreenPresenter(this);
        }

    }
}
