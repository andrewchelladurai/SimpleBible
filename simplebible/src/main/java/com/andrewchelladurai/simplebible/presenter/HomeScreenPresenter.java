/*
 *
 * This file 'HomeScreenPresenter.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.ops.HomeScreenOps;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 16-Sep-2018 @ 6:42 PM
 */
public class HomeScreenPresenter {

    private final HomeScreenOps mOps;

    public HomeScreenPresenter(final HomeScreenOps ops) {
        mOps = ops;
    }
}
