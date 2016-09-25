package com.andrewchelladurai.simplebible.interaction;

import android.content.Context;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:41 AM
 */
public interface HomeTabOperations
        extends BasicOperations {

    String getBookInput();

    String getChapterInput();

    void showError(String message);

    void inputValidated();

    void focusBookInputField();

    void focusChapterInputField();

    String getBookInputEmptyErrorMessage();

    String getChapterInputEmptyErrorMessage();

    Context getFragmentContext();

    String[] getDailyVerseArray();
}
