package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.andrewchelladurai.simplebible.interaction.HomeTabOperations;
import com.andrewchelladurai.simplebible.model.BooksList;
import com.andrewchelladurai.simplebible.presentation.HomeTabPresenter;
import com.andrewchelladurai.simplebible.utilities.Constants;
import com.andrewchelladurai.simplebible.utilities.Utilities;

public class HomeFragment
        extends Fragment
        implements View.OnClickListener, HomeTabOperations, View.OnFocusChangeListener,
                   View.OnLongClickListener, DialogInterface.OnClickListener {

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

    @Override public void onCreate(Bundle bundle) {
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
        mDailyVerse.setOnLongClickListener(this);
        mBookInput = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_home_input_book_name);
        mChapterInput = (AppCompatAutoCompleteTextView)
                view.findViewById(R.id.fragment_home_input_chapter_number);
        mMessageLabel = (AppCompatTextView) view.findViewById(R.id.fragment_home_message_label);
        mGotoButton = (AppCompatButton) view.findViewById(R.id.fragment_home_button_goto);
        mGotoButton.setOnClickListener(this);
        init();
        return view;
    }

    @Override public void init() {
        Log.d(TAG, "init() called");
        mPresenter.init();
        ArrayAdapter<String> bookNamesAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line,
                mPresenter.getBookNamesList());
        mBookInput.setAdapter(bookNamesAdapter);
        mBookInput.setOnFocusChangeListener(this);
        mChapterInput.setAdapter(null);
        mChapterInput.setOnFocusChangeListener(this);

        mDailyVerse.setText(mPresenter.getVerseContentForToday());
    }

    @Override public void onClick(View v) {
        if (v instanceof AppCompatButton & v.equals(mGotoButton)) {
            showError("");
            String returnValue = mPresenter.validateInput(getBookInput(), getChapterInput());
            if (returnValue.equalsIgnoreCase(Constants.SUCCESS)) {
                inputValidated();
            }
        } else {
            Log.e(TAG, "onClick: " + getString(R.string.how_am_i_here));
        }
    }

    private String getBookInput() {
        return mBookInput.getText().toString();
    }

    private String getChapterInput() {
        return mChapterInput.getText().toString();
    }

    private void showError(String message) {
        mMessageLabel.setText(message);
    }

    private void inputValidated() {

        // get parameters to pass on
        BooksList.BookItem item = mPresenter.getBookItemUsingName(getBookInput());
        int chapterNumber = Integer.parseInt(getChapterInput());
        boolean returnValue = mPresenter.loadChapterList(item, getString(R.string.chapter));
        if (!returnValue) {
            Log.e(TAG, "inputValidated: returning - presenter could not load Chapter List");
            return;
        }
        // load parameters on a bundle to pass on
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChapterFragment.ARG_BOOK_ITEM, item);
        bundle.putInt(ChapterFragment.ARG_CHAPTER_NUMBER, chapterNumber);

        // clear current input
        mBookInput.setText(null);
        mChapterInput.setText(null);
        showError("");
        focusBookInputField();

        // start new activity using parameters
        Intent intent = new Intent(getContext(), ChapterListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void focusBookInputField() {
        mBookInput.requestFocus();
    }

    private void focusChapterInputField() {
        mChapterInput.requestFocus();
    }

    @Override
    public Context getFragmentContext() {
        return getContext();
    }

    @Override public void updateChapterAdapter(ArrayAdapter<String> adapter) {
        mChapterInput.setAdapter(adapter);
        showError("");
    }

    @Override public void handleEmptyChapterNumberValidationFailure() {
        showError(getString(R.string.fragment_home_err_msg_empty_chapter));
        focusChapterInputField();
    }

    @Override public void handleIncorrectChapterNumberValidationFailure() {
        showError(getString(R.string.fragment_home_err_msg_invalid_chapter_number));
        focusChapterInputField();
    }

    @Override public String getDailyVerseTemplate() {
        return getString(R.string.fragment_home_daily_verse_template);
    }

    @Override public void handleEmptyBookNameValidationFailure() {
        showError(getString(R.string.fragment_home_err_msg_empty_book));
        focusBookInputField();
    }

    @Override public void handleIncorrectBookNameValidationFailure() {
        updateChapterAdapter(null);
        focusBookInputField();
        showError(getString(R.string.fragment_home_err_msg_invalid_book));
    }

    @Override public void refresh() {
        Log.d(TAG, "refresh() called");
    }

    @Override public void onFocusChange(View v, boolean hasFocus) {
        if (v.equals(mChapterInput) & hasFocus) {
            mPresenter.validateBookInput(getBookInput());
        } else if (mBookInput.hasFocus()) {
            // FIXME: 25/9/16 code if needed
        }
    }

    @Override public boolean onLongClick(View view) {
        if (!view.equals(mDailyVerse)) {
            return false;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("What would you wish to do with Today's Verse?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Share", this);
        dialog.setNegativeButton("Bookmark", this);
        dialog.setNeutralButton("Cancel", this);
        dialog.show();
        return true;
    }

    @Override public void onClick(DialogInterface dialogInterface, int i) {
        Intent intent = null;
        switch (i) {
            case AlertDialog.BUTTON_POSITIVE: // Share
                String stringToShare = mPresenter.getTextToShareDailyVerse();
                if (stringToShare != null && !stringToShare.isEmpty()) {
                    intent = Utilities.shareVerse(stringToShare);
                }
                break;
            case AlertDialog.BUTTON_NEGATIVE: // Bookmark
                intent = mPresenter.bookmarkVerseForToday();
                break;
            case AlertDialog.BUTTON_NEUTRAL: // Cancel
                intent = null;
                break;
            default: // ???
                Log.e(TAG, "onClick: " + getString(R.string.how_am_i_here));
                return;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
