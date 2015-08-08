/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 */

package com.andrewchelladurai.simplebible;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_About
      extends Fragment {

    private static final String TAB_NUMBER = "4";
    private OnFragmentInteractionListener mListener;

    public static Fragment_About newInstance(int position) {
        Fragment_About fragment = new Fragment_About();
        Bundle         args     = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_About() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

//// TODO: Rename method, update argument and hook method into UI event
//public void onButtonPressed(View view) {
//    if (mListener != null) {
//        mListener.onFragmentAboutInteraction(view.getBookNumber() + "");
//    }
//}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " +
                                         "OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentAboutInteraction(String id);
    }

}
