/*
 *
 * This file 'HomeScreen.java' is part of SimpleBible :
 *
 * Copyright (c) 2018.
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
 *
 */

package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Verse;
import com.andrewchelladurai.simplebible.ops.HomeScreenOps;
import com.andrewchelladurai.simplebible.presenter.HomeScreenPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

public class HomeScreen
    extends Fragment
    implements HomeScreenOps {

    private static final String TAG = "HomeScreen";

    private static HomeScreenPresenter mPresenter;

    private TextView mVerseView;
    private TextView mMessage;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;

    @SuppressWarnings("WeakerAccess")
    public HomeScreen() {
        mPresenter = new HomeScreenPresenter(this);
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.home_screen, container, false);
        mVerseView = rootView.findViewById(R.id.daily_verse);
        mMessage = rootView.findViewById(R.id.message);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mFab = rootView.findViewById(R.id.share);
        mFab.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                shareDailyVerse();
            }
        });

        return rootView;
    }

    private void shareDailyVerse() {
        Log.d(TAG, "shareDailyVerse:");
    }

    @Override
    public void startLoadingScreen() {
        Log.d(TAG, "startLoadingScreen: ");
        mVerseView.setText(HtmlCompat.fromHtml(
            getString(R.string.home_screen_daily_verse_loading),
            HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public void stopLoadingScreen() {
        mVerseView.setText(HtmlCompat.fromHtml(
            getString(R.string.home_screen_daily_verse_default),
            HtmlCompat.FROM_HTML_MODE_LEGACY));

        mMessage.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFailedLoadingMessage() {
        Log.d(TAG, "showFailedLoadingMessage:");
        mVerseView.setText(HtmlCompat.fromHtml(
            getString(R.string.home_screen_daily_verse_failure),
            HtmlCompat.FROM_HTML_MODE_LEGACY));

        mMessage.setText(R.string.home_screen_message_loading_failure);

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDefaultDailyVerse() {
        Log.d(TAG, "showDefaultDailyVerse:");
        stopLoadingScreen();
    }

    @Override
    public void showDailyVerse(@NonNull final Verse verse) {
        Log.d(TAG, "showDailyVerse:");
    }

}
