package com.andrewchelladurai.simplebible;

import android.util.Log;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:40 AM
 */
public class HomeFragmentPresenter {

    private static final String TAG = "SB_HF_Presenter";
    private HomeFragmentInterface mFragment;

    public HomeFragmentPresenter(HomeFragmentInterface fragment) {
        mFragment = fragment;
    }

    public void gotoLocationClicked() {
        Log.d(TAG, "gotoLocationClicked");
        String bookName = mFragment.getBookInput();
        String chapterNumber = mFragment.getChapterInput();
    }
}
