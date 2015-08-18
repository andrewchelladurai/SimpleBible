/*
 * Copyright (c) 2015.
 * Andrew Chelladurai - - TheUnknownAndrew[at]GMail[dot]com
 *
 * This Application is available at location
 * https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 *
 */

package com.andrewchelladurai.simplebible;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
    private OnFragmentInteractionListener mListener;

    public Fragment_About() {
        // Required empty public constructor
    }

    public static Fragment_About newInstance(int position) {
        Fragment_About fragment = new Fragment_About();
        Bundle args = new Bundle();
        args.putInt(TAB_NUMBER, position);
        fragment.setArguments(args);
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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        WebView abtMe = (WebView) v.findViewById(R.id.about_meView);
//        abtMe.setBackgroundColor(Color.TRANSPARENT);
        try {
            abtMe.loadUrl("file:///android_asset/about_me.html");
        } catch (Exception e) {
            Log.e("ERROR", "about_me.html not loaded " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void sendEmail() {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.contactEmail)});
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contactSubject));
        email.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(email, "Select an Email app"));
    }

    public void openGitHubPage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.githubPage))));
    }

    public void openGPlusPage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gPlusPage))));
    }

    public interface OnFragmentInteractionListener {
        void onFragmentAboutInteraction(String id);
    }

}
