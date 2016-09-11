/*
 *
 * This file 'BooksListFragmentPresenterTest.java' is part of SimpleBible :
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

import com.andrewchelladurai.simplebible.interaction.BooksTabOperations;
import com.andrewchelladurai.simplebible.presentation.BooksListPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by andrew on 9/10/16.
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 10-Sep-2016 @ 3:03 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class BooksListPresenterTest {

    @Mock
    private BooksTabOperations mInterface;
    private BooksListPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new BooksListPresenter(mInterface);
    }

    @After
    public void tearDown() throws Exception {
        mInterface = null;
        mPresenter = null;
    }

    @Test
    public void testInit() throws Exception {
    }
}
