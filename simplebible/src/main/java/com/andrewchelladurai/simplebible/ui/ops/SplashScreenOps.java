package com.andrewchelladurai.simplebible.ui.ops;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 09-Aug-2018 @ 8:44 PM.
 */
public interface SplashScreenOps
    extends LoaderManager.LoaderCallbacks<Boolean>, View.OnClickListener {

    @NonNull
    Context getSystemContext();

    void updateProgress(Integer value);

    void showLoadingScreen();

    void showNextScreen();

    void showLoadingFailureScreen();
}
