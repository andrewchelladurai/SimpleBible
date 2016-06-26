/*
 * This file 'Utilities.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 14-May-2016 @ 9:14 PM
 */
public class Utilities {

    // constants for arguments used in Fragments and Activities.
    public static final String TODAY_VERSE_REFERENCE = "TODAY_VERSE_REFERENCE";
    public static final String BOOKS_COLUMN_COUNT = "BOOKS_COLUMN_COUNT";

    private static final String    TAG            = "SB_Utilities";
    private static       Utilities staticInstance = null;
    private final Resources mResources;

    private Utilities(final Resources pResources) {
        mResources = pResources;
    }

    public static Utilities getInstance(final Resources pResources) {
        if (staticInstance == null) {
            staticInstance = new Utilities(pResources);
            Log.i(TAG, "getInstance: staticInstance is Initiated");
        }
        return staticInstance;
    }

    public static Intent shareVerse(String textToShare) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");
        Log.i(TAG, "shareVerse: " + textToShare);
        return sendIntent;
    }
}
