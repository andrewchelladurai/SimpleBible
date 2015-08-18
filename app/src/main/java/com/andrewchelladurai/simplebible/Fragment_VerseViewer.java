/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 *
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment_VerseViewer
        extends Fragment {

    public Fragment_VerseViewer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_verse_viewer, container, false);
        return v;
    }
}
