/*
 *
 * This file 'BooksListTest.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.adapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by andrew on 9/10/16.
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 10-Sep-2016 @ 3:15 PM
 */
public class BooksListTest {

    @Before
    public void setUp() throws Exception {
        BooksList.clearList();
    }

    @After
    public void tearDown() throws Exception {
        BooksList.clearList();
    }

    @Test
    public void populateBooksList_with_null_returns_false() throws Exception {
        String array[] = null;
        boolean value = BooksList.populateBooksList(array);
        Assert.assertFalse(value);
    }

    @Test
    public void populateBooksList_returns_false_when_null_is_passed() throws Exception {
        String array[] = new String[0];
        boolean value = BooksList.populateBooksList(array);
        Assert.assertFalse(value);
    }

    @Test
    public void populateBooksList_returns_false_when_array_of_length_0_is_passed() throws Exception {
        String array[] = new String[0];
        boolean value = BooksList.populateBooksList(array);
        Assert.assertFalse(value);
    }

    @Test
    public void populateBooksList_returns_false_when_array_of_length_67_is_passed() throws Exception {
        String array[] = new String[67];
        boolean value = BooksList.populateBooksList(array);
        Assert.assertFalse(value);
    }

    @Test
    public void populateBooksList_returns_true_when_array_of_length_66_is_passed() throws Exception {
        String array[] = new String[66];
        for (int i = 0; i < 66; i++) {
            array[i] = "Book:" + i;
        }
        boolean value = BooksList.populateBooksList(array);
        Assert.assertTrue(value);
    }

    @Test
    public void populateBooksList_returns_false_when_array_of_length_66_is_passed_twice() throws Exception {
        String array[] = new String[66];
        for (int i = 0; i < 66; i++) {
            array[i] = "Book:" + i;
        }
        boolean value = BooksList.populateBooksList(array);
        Assert.assertTrue(value);
        value = BooksList.populateBooksList(array);
        Assert.assertFalse(value);
    }

    @Test
    public void getListItems_returns_zero_items_after_clearing_items() throws Exception {
        BooksList.clearList();
        List<BooksList.BookItem> items = BooksList.getListItems();
        Assert.assertTrue(items.size() == 0);
    }

    @Test
    public void getListItems_returns_items_when_populated_correctly() throws Exception {
        String array[] = new String[66];
        for (int i = 0; i < 66; i++) {
            array[i] = "Book:" + i;
        }

        boolean value = BooksList.populateBooksList(array);
        Assert.assertTrue(value);

        List<BooksList.BookItem> items = BooksList.getListItems();
        Assert.assertTrue(items.size() == 66);
    }
}
