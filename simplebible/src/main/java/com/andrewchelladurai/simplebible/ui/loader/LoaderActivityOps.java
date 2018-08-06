package com.andrewchelladurai.simplebible.ui.loader;

import android.support.v4.app.LoaderManager;

import com.andrewchelladurai.simplebible.common.BaseScreenOps;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 05-Aug-2018 @ 8:38 PM.
 */
interface LoaderActivityOps
    extends BaseScreenOps, LoaderManager.LoaderCallbacks<Boolean> {

    void showLoadingScreen();

    void showMainScreen();

    void showFailedScreen();
}
