/*
 *
 * This file 'VerseEventHandler.java' is part of SimpleBible :
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

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 21-May-2016 @ 8:55 PM
 */
public class VerseEventHandler {

    private final HANDLE mHandler;

    enum HANDLE {CHAPTER, SEARCH}

    private final ChapterActivity mVerseFragment;

    public VerseEventHandler(final ChapterActivity pVerseFragment,
                             final HANDLE pHandler) {
        mVerseFragment = pVerseFragment;
        mHandler = pHandler;
    }

    public void handleLongClick(final ChapterContent.VerseEntry pItem) {
        switch (mHandler) {
            case CHAPTER:
                mVerseFragment.handleLongClick(pItem);
                break;
            case SEARCH:
                break;
            default:// FIXME: handle this
        }
    }
}
