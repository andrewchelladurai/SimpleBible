/*
 *
 * This file 'DBUtilityOperations.java' is part of SimpleBible :
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

import java.util.ArrayList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 02-Oct-2016 @ 12:49 PM
 */
public interface DBUtilityOperations {

    String getVerseForReference(int bookNumber, int chapterNumber, int verseNumber);

    ArrayList<String> getAllVerseForChapter(int bookNumber, int chapterNumber);

    ArrayList<String[]> searchForInput(String input);

    ArrayList<String[]> getAllBookmarks();

    String getNoteForReference(String reference);

    boolean doesBookmarkReferenceExist(String reference);

    boolean createNewBookmark(String references, String note);

    boolean deleteBookMarkEntry(String references);

    boolean updateExistingBookmark(String references, String note);
}
