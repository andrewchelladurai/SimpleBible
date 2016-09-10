/*
 *
 * This file 'SearchFragmentPresenterTest.java' is part of SimpleBible :
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

import com.andrewchelladurai.simplebible.interaction.SearchFragmentInterface;
import com.andrewchelladurai.simplebible.presentation.SearchFragmentPresenter;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * Created by andrew on 11/9/16. Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 11-Sep-2016 @ 1:15 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchFragmentPresenterTest {

    @Mock
    private SearchFragmentInterface mInterface;
    private SearchFragmentPresenter mPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mInterface = new SearchFragment();
        mPresenter = new SearchFragmentPresenter(mInterface);
    }

    @After
    public void tearDown() {
        mInterface = null;
        mPresenter = null;
    }

    @Test
    public void testInit() {
        mPresenter.init();
    }

    @Test
    public void searchButtonClicked_returns_empty_err_msg_when_null_is_passed() {
        when(mInterface.getEmptyInputErrorMessage()).thenReturn("Empty Input");
        String returnValue = mPresenter.searchButtonClicked(null);
        Assert.assertTrue(returnValue.equalsIgnoreCase("Empty Input"));
    }

    @Test
    public void testResetButtonClicked() {
//        mPresenter.resetButtonClicked();
    }
}
