/*
 * This file 'FragmentHome.java' is part of SimpleBible :
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
 */

package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHome#getInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome
        extends Fragment {

    private static final String TAG = "FragmentHome";
    private static final String ARG_VERSE_OF_THE_DAY = "ARG_VERSE_OF_THE_DAY";
    private static FragmentHome staticInstance;
    private        String              verseOfTheDay;
    private        InteractionListener mListener;

    public FragmentHome() {
        // Required empty public constructor
    }

    public static FragmentHome getInstance(String verseID) {
        if (staticInstance == null) {
            staticInstance = new FragmentHome();
            Bundle args = new Bundle();
            args.putString(ARG_VERSE_OF_THE_DAY, verseID);
            staticInstance.setArguments(args);
        }
        return staticInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            verseOfTheDay = getArguments().getString(ARG_VERSE_OF_THE_DAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getVerseOfTheDay() {
        return verseOfTheDay;
    }

    public void setVerseOfTheDay(String verseOfTheDay) {
        this.verseOfTheDay = verseOfTheDay;
    }

    public interface InteractionListener {

        void onHomeFragmentInteraction(View view);
    }
}
