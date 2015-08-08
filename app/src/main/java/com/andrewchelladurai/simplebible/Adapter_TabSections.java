/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 */

package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.Locale;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class Adapter_TabSections
        extends FragmentPagerAdapter {

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
    switch (position) {
        case 1:
            if (fragment_books == null) {
                fragment_books = Fragment_Books.newInstance(position);
            }
            return fragment_books;
        case 2:
            if (fragment_search == null) {
                fragment_search = Fragment_Search.newInstance(position);
            }
            return fragment_search;
        case 3:
            if (fragment_about == null) {
                fragment_about = Fragment_About.newInstance(position);
            }
            return fragment_about;
        default:
            Log.e("ERROR","Hit default switch in Adapter_TabSections.getItem("+
                          position+")");
            if (fragmentWelcome == null) {
                fragmentWelcome = Fragment_Welcome.getInstance(position);
            }
            return fragmentWelcome;
    }
}

@Override
public int getCount() {
    return 4;
}

@Override
public CharSequence getPageTitle(int position) {
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
    return null;
}

    public void aboutSendEmail() {
        fragment_about.sendEmail();
    }

    public void aboutOpenGitHub() {
        fragment_about.openGitHubPage();
    }

    public void aboutOpenGPlus() {
        fragment_about.openGPlusPage();
    }

    public void searchShowResults() {
        fragment_search.searchForResults();
    }
}
