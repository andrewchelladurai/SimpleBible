package com.andrewchelladurai.simplebible;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 14-May-2016 @ 9:14 PM
 */
public class Utilities {
    private static Utilities staticInstance = new Utilities();

    public static Utilities getInstance() {
        return staticInstance;
    }

    private Utilities() {
    }
}
