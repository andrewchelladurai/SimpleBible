package com.andrewchelladurai.simplebible.interaction;

import android.content.Context;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 05-Sep-2016 @ 4:55 PM
 */
public interface SimpleBibleActivityInterface
        extends BasicOperations {

    Context getThisApplicationContext();
    String getTabTitle(int position);
}
