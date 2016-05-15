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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class HomeFragment
        extends Fragment {

    private static final String TAG = "HomeFragment";
    private AppCompatAutoCompleteTextView mBookName;
    private AppCompatAutoCompleteTextView mChapter;
    private AppCompatTextView mDailyVerse;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        EventHandler eventHandler = new EventHandler();

        AppCompatButton button = (AppCompatButton) view.findViewById(R.id.fragment_home_button);
        button.setOnClickListener(eventHandler);

        mBookName = (AppCompatAutoCompleteTextView) view.findViewById(R.id.fragment_home_book_name);
        mBookName.setAdapter(new ArrayAdapter<>(view.getContext(),
                                                android.R.layout.simple_dropdown_item_1line,
                                                Book.getAllBookNamed()));
//        mBookName.setOnItemClickListener(eventHandler);
        mBookName.setValidator(eventHandler);

        mChapter = (AppCompatAutoCompleteTextView) view.findViewById(R.id.fragment_home_chapter);
        mDailyVerse = (AppCompatTextView) view.findViewById(R.id.fragment_home_daily_verse);

        return view;
    }

    private void resetValues() {
        mBookName.setText("");
        mBookName.setError(null);

        mChapter.setText("");
        mChapter.setAdapter(null);
        mChapter.setError(null);
        mChapter.setHint(R.string.hint_chapter_number);
    }

    class EventHandler
            implements View.OnClickListener, AdapterView.OnItemClickListener,
                       AutoCompleteTextView.Validator {

        @Override public void onClick(final View v) {
            Log.i(TAG, "EventHandler.onClick: Button Goto Location Clicked");
            resetValues();
        }

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                                final long id) {
            Log.i(TAG, "EventHandler.onItemClick: Book Name Dropdown selected");
            String name = mBookName.getText().toString();
            Book.Details bookDetails = Book.getBookDetails(name);
            if (bookDetails != null) {
                String[] chapters = new String[bookDetails.chapterCount];
                for (int i = 0; i < chapters.length; i++) {
                    chapters[i] = "" + (i + 1);
                }
                mChapter.setAdapter(new ArrayAdapter<>(mChapter.getContext(),
                                                       android.R.layout.simple_dropdown_item_1line,
                                                       chapters));
                mBookName.setError(null);
                mChapter.requestFocus();
            } else {
                mChapter.setAdapter(null);
            }
        }

        @Override public boolean isValid(final CharSequence text) {
            String bookName = text.toString();
            if (bookName.isEmpty()) {
                mBookName.setError("Empty Book Name");
                return false;
            }
            Book.Details bookDetails = Book.getBookDetails(bookName);
            if (bookDetails == null) {
                fixText(text);
                return false;
            }
            String[] chapters = new String[bookDetails.chapterCount];
            for (int i = 0; i < chapters.length; i++) {
                chapters[i] = "" + (i + 1);
            }
            mChapter.setAdapter(new ArrayAdapter<>(mChapter.getContext(),
                                                   android.R.layout.simple_dropdown_item_1line,
                                                   chapters));
            mChapter.setHint("between 1 and " + chapters.length);
            mChapter.requestFocus();
            return true;
        }

        @Override public CharSequence fixText(final CharSequence invalidText) {
            mBookName.setError("Incorrect Book Name");
            mChapter.setAdapter(null);
            mChapter.setHint(R.string.hint_chapter_number);
            return invalidText;
        }
    }
}
