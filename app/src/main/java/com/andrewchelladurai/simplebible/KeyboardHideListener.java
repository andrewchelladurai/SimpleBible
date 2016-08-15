/*
 *
 * This file 'KeyboardHideListener.java' is part of SimpleBible :
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
 *
 */

package com.andrewchelladurai.simplebible;

import android.support.v4.view.ViewPager;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 23-Apr-2016 @ 1:44 PM
 */
class KeyboardHideListener
        implements ViewPager.OnPageChangeListener {

    private final ActivitySimpleBible activity;

    public KeyboardHideListener(final ActivitySimpleBible pActivity) {
        activity = pActivity;
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int
            positionOffsetPixels) {
        Utilities.hideKeyboard(activity);
        StringBuilder pageTitle = new StringBuilder(activity.getString(R.string.app_name));
        switch (position) {
            case 0:
                pageTitle.append(" : ").append(activity.getString(R.string.tab_home));
                break;
            case 1:
                pageTitle.append(" : ").append(activity.getString(R.string.tab_books));
                break;
            case 2:
                pageTitle.append(" : ").append(activity.getString(R.string.tab_search));
                break;
            case 3:
                activity.refreshNotesScreen();
                pageTitle.append(" : ").append(activity.getString(R.string.tab_notes));
        }
        activity.setTitle(pageTitle);
    }

    @Override
    public void onPageSelected(final int position) {
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
    }
}
