package com.andrewchelladurai.simplebible.ui.ops;

import android.content.Context;
import android.view.View;

import com.andrewchelladurai.simplebible.data.entities.Verse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 09-Aug-2018 @ 8:44 PM.
 */
public interface SplashScreenOps
    extends View.OnClickListener {

    @NonNull
    Context getSystemContext();

    void showLoadingScreen();

    void initRepositories();

    void showLoadingSuccessScreen();

    void showLoadingFailureScreen();

    void stopLoadingScreen();

    void displayVerseForToday(@Nullable Verse verse);
}
