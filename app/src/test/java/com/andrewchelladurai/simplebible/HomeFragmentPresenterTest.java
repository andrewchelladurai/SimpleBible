package com.andrewchelladurai.simplebible;

import com.andrewchelladurai.simplebible.interaction.HomeFragmentInterface;
import com.andrewchelladurai.simplebible.presentation.HomeFragmentPresenter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        MockitoAnnotations.initMocks(this);
        mPresenter = new HomeFragmentPresenter(mFragment);
    }

    @After
    public void tearDown() throws Exception {
        mFragment = null;
        mPresenter = null;
    }

    @Test
    public void validateBookInput_returns_empty_message_when_input_is_empty() throws Exception {
        String expectedResult = "No Book Name Entered";
        when(mFragment.getBookInputEmptyErrorMessage()).thenReturn(expectedResult);
        String result = mPresenter.validateBookInput("");
        Assert.assertTrue(result.equalsIgnoreCase(expectedResult));
    }
    @Test
    public void validateBookInput_returns_empty_message_when_input_is_null() throws Exception {
        String expectedResult = "No Book Name Entered";
        when(mFragment.getBookInputEmptyErrorMessage()).thenReturn(expectedResult);
        String result = mPresenter.validateBookInput(null);
        Assert.assertTrue(result.equalsIgnoreCase(expectedResult));
    }

    @Test
    public void validateChapterInput_returns_empty_message_when_input_is_empty() throws Exception {
        String expectedResult = "No Chapter Entered";
        when(mFragment.getChapterInputEmptyErrorMessage()).thenReturn(expectedResult);
        String result = mPresenter.validateChapterInput("");
        Assert.assertTrue(result.equalsIgnoreCase(expectedResult));
    }
    @Test
    public void validateChapterInput_returns_empty_message_when_input_is_null() throws Exception {
        String expectedResult = "No Chapter Entered";
        when(mFragment.getChapterInputEmptyErrorMessage()).thenReturn(expectedResult);
        String result = mPresenter.validateChapterInput(null);
        Assert.assertTrue(result.equalsIgnoreCase(expectedResult));
    }
}
