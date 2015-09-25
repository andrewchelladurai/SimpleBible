/*
 * This file 'Fragment_About.java' is part of SimpleBible :  An Android Bible application
 * with offline access, simple features and easy to use navigation.
 *
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on Fri, 25 Sep 2015 23:55:09 IST
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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class Fragment_About
        extends Fragment {

    private static final String TAB_NUMBER = "4";
    private static final String CLASS_NAME = "Fragment_About";
    private OnFragmentInteractionListener mListener;

    public Fragment_About() {
        // Required empty public constructor
    }

    public static Fragment_About newInstance(int position) {
        Log.i(CLASS_NAME, "Entering newInstance");
        Fragment_About fragment = new Fragment_About();
        Bundle args = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragment.setArguments(args);
        Log.i(CLASS_NAME, "Exiting newInstance");
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                         + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(CLASS_NAME, "Entering onCreateView");
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        WebView abtMe = (WebView) v.findViewById(R.id.about_meView);
        try {
            abtMe.loadUrl("file:///android_asset/about_me.html");
        } catch (Exception e) {
            Log.e(CLASS_NAME, "ERROR : about_me.html not loaded " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        Log.i(CLASS_NAME, "Exiting onCreateView");
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentAboutInteraction(String id);
    }

}
