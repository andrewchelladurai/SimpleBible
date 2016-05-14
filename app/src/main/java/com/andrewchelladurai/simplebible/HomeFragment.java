package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface to handle interaction events. Use
 * the {@link HomeFragment#newInstance} factory method to create an instance of this fragment.
 */
public class HomeFragment
        extends Fragment {

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}
