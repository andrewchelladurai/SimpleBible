/*
 * This file 'FragmentWelcome.java' is part of SimpleBible :  An Android Bible application
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * Use the {@link FragmentWelcome#getInstance} factory method to create an instance of this
 * fragment.
 */
public class FragmentWelcome
        extends Fragment {

    private static final String TAB_NUMBER = "1";
    private static final String CLASS_NAME = "FragmentWelcome";

    public FragmentWelcome() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static FragmentWelcome getInstance(int position) {
        Log.i(CLASS_NAME, "Entering getInstance");
        FragmentWelcome fragmentWelcome = new FragmentWelcome();
        Bundle args = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragmentWelcome.setArguments(args);
        Log.i(CLASS_NAME, "Entering getInstance");
        return fragmentWelcome;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

}