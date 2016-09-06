package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.interaction.HomeFragmentInterface;
import com.andrewchelladurai.simplebible.presentation.HomeFragmentPresenter;

import static com.andrewchelladurai.simplebible.R.*;

public class HomeFragment
        extends Fragment
        implements View.OnClickListener, HomeFragmentInterface {

    private static final String TAG = "SB_HomeFragment";
    private AppCompatAutoCompleteTextView mBookInput;
    private AppCompatAutoCompleteTextView mChapterInput;
    private AppCompatTextView mDailyVerse;
    private AppCompatTextView mMessageLabel;
    private AppCompatButton mGotoButton;
    private HomeFragmentPresenter mPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPresenter = new HomeFragmentPresenter(this);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(layout.fragment_home, container, false);
        mDailyVerse = (AppCompatTextView) view.findViewById(id.fragment_home_daily_verse);
        mBookInput = (AppCompatAutoCompleteTextView)
                view.findViewById(id.fragment_home_input_book_name);
        mChapterInput = (AppCompatAutoCompleteTextView)
                view.findViewById(id.fragment_home_input_chapter_number);
        mMessageLabel = (AppCompatTextView) view.findViewById(id.fragment_home_message_label);
        mGotoButton = (AppCompatButton) view.findViewById(id.fragment_home_button_goto);
        mGotoButton.setOnClickListener(this);
        return view;
    }

    @Override public void onClick(View v) {
        if (v instanceof AppCompatButton & v.equals(mGotoButton)) {
            if (mPresenter.gotoLocationClicked()) {
                inputValidated();
            }
        } else {
            Log.d(TAG, "onClick: " + getString(string.how_am_i_here));
        }
    }

    @Override public String getBookInput() {
        return mBookInput.getText().toString();
    }

    @Override public String getChapterInput() {
        return mChapterInput.getText().toString();
    }

    @Override public void showError(String message) {
        mMessageLabel.setText(message);
    }

    @Override public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override public void inputValidated() {
        mBookInput.setText(null);
        mChapterInput.setText(null);
        mMessageLabel.setText(null);
        mBookInput.requestFocus();
    }

    @Override public void focusBookInputField() {
        mBookInput.requestFocus();
    }

    @Override public void focusChapterInputField() {
        mChapterInput.requestFocus();
    }
}
