/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 */

package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        View v = inflater.inflate(R.layout.fragment_verse_viewer, container, false);
        SharedPreferences pref = getActivity()
              .getSharedPreferences("pref_general", Context.MODE_PRIVATE);
        Log.i("TEXT_STYLE", pref.getString("verse_text_style",
                                           "Default - Sans - Default"));
        return v;
    }
}
