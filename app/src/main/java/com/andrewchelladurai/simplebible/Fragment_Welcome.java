/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 */

package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link Fragment_Welcome.OnFragmentInteractionListener} interface to handle interaction events. Use
 * the {@link Fragment_Welcome#getInstance} factory method to create an instance of this fragment.
 */
public class Fragment_Welcome
        extends Fragment {

    private static final String TAB_NUMBER = "1";

    public Fragment_Welcome() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static Fragment_Welcome getInstance(int position) {
        Fragment_Welcome fragmentWelcome = new Fragment_Welcome();
        Bundle args = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragmentWelcome.setArguments(args);
        return fragmentWelcome;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

}
