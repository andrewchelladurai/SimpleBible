package com.andrewchelladurai.simplebible.interaction;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:41 AM
 */
public interface HomeFragmentInterface
        extends BasicOperations {

    String getBookInput();

    String getChapterInput();

    void showError(String message);

    void inputValidated();

    void focusBookInputField();

    void focusChapterInputField();

    String getBookInputEmptyErrorMessage();

    String getChapterInputEmptyErrorMessage();
}
