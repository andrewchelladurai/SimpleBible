package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
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
    private AppCompatButton mButton;
    private int mBookNumber, mChapterNumber, mMaxChapterCount;

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

        mButton = (AppCompatButton) view.findViewById(R.id.fragment_home_button);
        mButton.setOnClickListener(eventHandler);
        mButton.setEnabled(false);

        mBookName = (AppCompatAutoCompleteTextView) view.findViewById(R.id.fragment_home_book_name);
        mBookName.setAdapter(new ArrayAdapter<>(view.getContext(),
                                                android.R.layout.simple_dropdown_item_1line,
                                                Book.getAllBookNamed()));
        mBookName.setValidator(eventHandler);
        mBookName.setOnItemClickListener(eventHandler);

        mChapter = (AppCompatAutoCompleteTextView) view.findViewById(R.id.fragment_home_chapter);
        mDailyVerse = (AppCompatTextView) view.findViewById(R.id.fragment_home_daily_verse);
        setDailyVerse();

        return view;
    }

    private void resetValues() {
        mBookName.setText("");
        mBookName.setError(null);

        mChapter.setText("");
        mChapter.setAdapter(null);
        mChapter.setError(null);
        mChapter.setHint(R.string.hint_chapter_number);
        mBookNumber = mChapterNumber = mMaxChapterCount = 0;
        mButton.setEnabled(false);
    }

    public void setDailyVerse() {
        final DatabaseUtility dbu = DatabaseUtility.getInstance(null);
        final String[] parts = dbu.getVerseForToday().split(":");
        int book = Integer.parseInt(parts[0]);
        int chapter = Integer.parseInt(parts[1]);
        int verse = Integer.parseInt(parts[2]);

        String verseContent = getString(R.string.daily_verse_template);
        verseContent = verseContent.replace(getString(R.string.daily_verse_template_text),
                                            dbu.getSpecificVerse(book, chapter, verse));

        Book.Details bookDetails = Book.getBookDetails(book);
        if (bookDetails != null) {
            String verseRef = getString(R.string.daily_verse_ref_template);
            verseRef = verseRef.replace(
                    getString(R.string.daily_verse_ref_template_book_name), bookDetails.name);
            verseRef = verseRef.replace(
                    getString(R.string.daily_verse_ref_template_chapter_number), chapter + "");
            verseRef = verseRef.replace(
                    getString(R.string.daily_verse_ref_template_verse_number), verse + "");
            verseContent = verseContent.replace(
                    getString(R.string.daily_verse_template_ref), verseRef);

            Log.d(TAG, "setDailyVerse: " + verseContent);
            // FIXME: 16/5/16 Center Align Text on the display
            mDailyVerse.setText(Html.fromHtml(verseContent));
        }
    }

    class EventHandler
            implements View.OnClickListener, AutoCompleteTextView.Validator,
                       AdapterView.OnItemClickListener {

        @Override
        public void onClick(final View v) {
            String chapter = mChapter.getText().toString().trim();
            try {
                mChapterNumber = (chapter.isEmpty()) ? 1 : Integer.parseInt(chapter);
            } catch (NumberFormatException pNFE) {
                mChapterNumber = 1;
                pNFE.printStackTrace();
            }
            if (mChapterNumber < 1 | mChapterNumber > mMaxChapterCount) {
                mChapter.setError("Incorrect Chapter Number");
                return;
            }
            Log.i(TAG, "EventHandler.onClick: Button Goto Location Clicked"
                       + " [" + mBookNumber + "][" + mChapterNumber + "]");
            resetValues();
        }

        @Override
        public boolean isValid(final CharSequence text) {
            String bookName = (null != text) ? text.toString() : "";
            if (bookName.isEmpty()) {
                fixText(text);
                return false;
            }
            final Book.Details bookDetails = Book.getBookDetails(bookName);
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
            mBookName.setError(null);
            mBookNumber = bookDetails.number;
            mMaxChapterCount = bookDetails.chapterCount;
            mButton.setEnabled(true);
            return true;
        }

        @Override
        public CharSequence fixText(final CharSequence invalidText) {
            mBookName.setError("Incorrect Book Name");
            mChapter.setHint(R.string.hint_chapter_number);
            mChapter.setAdapter(null);
            mBookNumber = mChapterNumber = mMaxChapterCount = 0;
            mButton.setEnabled(false);
            return invalidText;
        }

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position,
                                final long id) {
            mButton.setEnabled(true);
            mChapter.requestFocus();
        }
    }
}
