package com.andrewchelladurai.simplebible.interaction;

import android.content.Context;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:41 AM
 */
public interface HomeFragmentInterface {

    String getBookInput();
    String getChapterInput();
    void showError(String message);
    Context getAppContext();
    void inputValidated();
    void focusBookInputField();
    void focusChapterInputField();
}
