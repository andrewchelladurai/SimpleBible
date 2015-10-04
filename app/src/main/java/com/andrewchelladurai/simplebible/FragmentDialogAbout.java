/*
 * This file 'FragmentDialogAbout.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Copyright (c) Andrew Chelladurai - 2015.
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
 */

package com.andrewchelladurai.simplebible;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDialogAbout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDialogAbout
        extends DialogFragment {

    private static final String TAG = "FragmentDialogAbout";

    public FragmentDialogAbout() {
        // Required empty public constructor
    }

    public static FragmentDialogAbout newInstance() {
        Log.d(TAG, "newInstance() Entered");
        FragmentDialogAbout fragment = new FragmentDialogAbout();
        Log.d(TAG, "newInstance() Exited");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() Entered");
        View v = inflater.inflate(R.layout.fragment_dialog_about, container);
        WebView abtMe = (WebView) v.findViewById(R.id.about_dialog_meView);
        try {
            abtMe.loadUrl("file:///android_asset/about_me.html");
            Log.d(TAG, "onCreateView() Loaded WebView");
        } catch (Exception e) {
            Log.e(TAG, "ERROR : about_me.html not loaded " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        Log.d(TAG, "onCreateView() Exited");
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (null != d) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
