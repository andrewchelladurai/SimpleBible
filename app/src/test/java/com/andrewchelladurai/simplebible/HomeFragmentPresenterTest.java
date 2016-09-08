package com.andrewchelladurai.simplebible;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;

import com.andrewchelladurai.simplebible.interaction.HomeFragmentInterface;
import com.andrewchelladurai.simplebible.presentation.HomeFragmentPresenter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

import static org.mockito.Mockito.when;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 07-Sep-2016 @ 2:37 AM
 */
@RunWith(JUnit4.class)
public class HomeFragmentPresenterTest {

    @Mock
    HomeFragmentInterface mFragment;
    private HomeFragmentPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        mPresenter = new HomeFragmentPresenter(mFragment);
    }

    @After
    public void tearDown() throws Exception {
        mFragment = null;
        mPresenter = null;
    }

    @Test
    public void go2_click_returns_false_when_book_input_is_empty() throws Exception {
        String result = mPresenter.validateBookInput("");
        Assert.assertTrue(result.equalsIgnoreCase("No Book Name Entered"));
    }
}
