package com.andrewchelladurai.simplebible.interaction;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 1:41 AM
 */
public interface HomeTabOperations
        extends BasicOperations {

    void handleEmptyBookNameValidationFailure();

    void handleIncorrectBookNameValidationFailure();

    Context getFragmentContext();

    void updateChapterAdapter(ArrayAdapter<String> adapter);

    void handleEmptyChapterNumberValidationFailure();

    void handleIncorrectChapterNumberValidationFailure();

    String getDailyVerseTemplate();

}
