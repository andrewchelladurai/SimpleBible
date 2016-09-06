package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment
        extends Fragment
        implements View.OnClickListener, HomeFragmentInterface {

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

    @Override public void onClick(View v) {
        if (v instanceof AppCompatButton & v.equals(mGotoButton)) {
            mPresenter.gotoLocationClicked();
        }
    }

    @Override public String getBookInput() {
        return mBookInput.getText().toString();
    }

    @Override public String getChapterInput() {
        return mChapterInput.getText().toString();
    }
}
