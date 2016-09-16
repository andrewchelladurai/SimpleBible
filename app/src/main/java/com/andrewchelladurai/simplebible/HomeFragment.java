package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.interaction.HomeTabOperations;
import com.andrewchelladurai.simplebible.presentation.HomeTabPresenter;
import com.andrewchelladurai.simplebible.utilities.Constants;

public class HomeFragment
        extends Fragment
        implements View.OnClickListener, HomeTabOperations {

    private static final String TAG = "SB_HomeFragment";
    private AppCompatAutoCompleteTextView mBookInput;
    private AppCompatAutoCompleteTextView mChapterInput;
    private AppCompatTextView             mDailyVerse;
    private AppCompatTextView             mMessageLabel;
    private AppCompatButton               mGotoButton;
    private HomeTabPresenter              mPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPresenter = new HomeTabPresenter(this);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mDailyVerse = (AppCompatTextView) view.findViewById(R.id.fragment_home_daily_verse);
        mBookInput = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_home_input_book_name);
        mChapterInput = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_home_input_chapter_number);
        mMessageLabel = (AppCompatTextView) view.findViewById(R.id.fragment_home_message_label);
        mGotoButton = (AppCompatButton) view.findViewById(R.id.fragment_home_button_goto);
        mGotoButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof AppCompatButton & v.equals(mGotoButton)) {
            // Check if Book Input is valid, return otherwise
            String returnValue = mPresenter.validateBookInput(getBookInput());
            if (!returnValue.equalsIgnoreCase(Constants.SUCCESS_RETURN_VALUE)) {
                showError(returnValue);
                focusBookInputField();
                return;
            }
            // Check if Chapter Input is valid, return otherwise
            returnValue = mPresenter.validateChapterInput(getChapterInput());
            if (!returnValue.equalsIgnoreCase(Constants.SUCCESS_RETURN_VALUE)) {
                showError(returnValue);
                focusChapterInputField();
                return;
            }
            inputValidated();
        } else {
            Log.d(TAG, "onClick: " + getString(R.string.how_am_i_here));
        }
    }

    @Override
    public String getBookInput() {
        return mBookInput.getText().toString();
    }

    @Override
    public String getChapterInput() {
        return mChapterInput.getText().toString();
    }

    @Override
    public void showError(String message) {
        mMessageLabel.setText(message);
    }

    @Override
    public void inputValidated() {
        mBookInput.setText(null);
        mChapterInput.setText(null);
        mMessageLabel.setText(null);
        mBookInput.requestFocus();
    }

    @Override
    public void focusBookInputField() {
        mBookInput.requestFocus();
    }

    @Override
    public void focusChapterInputField() {
        mChapterInput.requestFocus();
    }

    @Override
    public String getBookInputEmptyErrorMessage() {
        return getString(R.string.fragment_home_err_msg_empty_book);
    }

    @Override
    public String getChapterInputEmptyErrorMessage() {
        return getString(R.string.fragment_home_err_msg_empty_chapter);
    }

    @Override
    public void init() {
        Log.d(TAG, "init() called");
        mPresenter.init();
    }

    @Override
    public void refresh() {
        Log.d(TAG, "refresh() called");
    }
}
