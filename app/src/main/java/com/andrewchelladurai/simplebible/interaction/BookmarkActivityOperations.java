/*
 *
 * This file 'BookmarkEntryActivityInterface.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.interaction;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 17-Sep-2016 @ 12:49 AM
 */
public interface BookmarkActivityOperations
        extends BasicOperations {

    String VIEW          = "VIEW";
    String CREATE        = "CREATE";
    String ARG_REFERENCE = "ARG_REFERENCE";
    String ARG_MODE      = "ARG_MODE";

    String getPassedReference();

    String getInputNote();
}
