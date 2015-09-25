/*
 *
 * This file is part of SimpleBible : A Holy Bible Application on the
 * Android Mobile platform with easy navigation and offline access.
 *
 * Copyright (c) 2015.
 * Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
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

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.Locale;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class Adapter_TabSections
        extends FragmentPagerAdapter {

    private final String CLASS_NAME = "Adapter_TabSections";
    private Context          context;
    private Fragment_Welcome fragmentWelcome;
    private Fragment_Books   fragment_books;
    private Fragment_Search  fragment_search;
    private Fragment_About   fragment_about;

    public Adapter_TabSections(FragmentManager fm, Context con) {
        super(fm);
        context = con;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(CLASS_NAME, "Entering getItem " + position);
        switch (position) {
            case 1:
                if (fragment_books == null) {
                    fragment_books = Fragment_Books.newInstance(position);
                }
                Log.i(CLASS_NAME, "Exiting getItem");
                return fragment_books;
            case 2:
                if (fragment_search == null) {
                    fragment_search = Fragment_Search.newInstance(position);
                }
                Log.i(CLASS_NAME, "Exiting getItem");
                return fragment_search;
            case 3:
                if (fragment_about == null) {
                    fragment_about = Fragment_About.newInstance(position);
                }
                Log.i(CLASS_NAME, "Exiting getItem");
                return fragment_about;
            default:
                Log.e(CLASS_NAME, "ERROR : Hit default switch in Tabs");
                if (fragmentWelcome == null) {
                    fragmentWelcome = Fragment_Welcome.getInstance(position);
                }
                Log.i(CLASS_NAME, "Exiting getItem");
                return fragmentWelcome;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.i(CLASS_NAME, "Entering getPageTitle");
        switch (position) {
            case 0:
                return context.getString(R.string.title_tab1).toUpperCase(Locale.getDefault());
            case 1:
                return context.getString(R.string.title_tab2).toUpperCase(Locale.getDefault());
            case 2:
                return context.getString(R.string.title_tab3).toUpperCase(Locale.getDefault());
            case 3:
                return context.getString(R.string.title_tab4).toUpperCase(Locale.getDefault());
        }
        Log.i(CLASS_NAME, "Exiting getPageTitle");
        return "NULL TITLE";
    }

    public void searchShowResults(View view) {
        Log.i(CLASS_NAME, "Entering searchShowResults");
        fragment_search.searchForResults(view);
        Log.i(CLASS_NAME, "Exiting searchShowResults");
    }
}
