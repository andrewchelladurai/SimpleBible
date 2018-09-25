/*
 *
 * This file 'BooksScreen.java' is part of SimpleBible :
 *
 * Copyright (c) 2018.
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

package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.ops.BooksScreenOps;
import com.andrewchelladurai.simplebible.ops.ViewHolderOps;
import com.andrewchelladurai.simplebible.presenter.BooksScreenPresenter;
import com.andrewchelladurai.simplebible.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class BooksScreen
    extends Fragment
    implements BooksScreenOps {

    private static final String TAG = "BooksScreen";

    private static final List<Book> mCache = new ArrayList<>();
    private static BooksScreenPresenter mPresenter;
    private AutoCompleteTextView mBook;
    private AutoCompleteTextView mChapter;
    private RecyclerView mListView;
    private BooksListAdapter mAdapter;
    private BookRepository mBookRepository;

    @SuppressWarnings("WeakerAccess")
    public BooksScreen() {
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.books_screen, container, false);
        mBookRepository = ViewModelProviders.of(this).get(BookRepository.class);
        mPresenter = new BooksScreenPresenter(this);

        mBook = view.findViewById(R.id.book_screen_book);
        mBook.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count,
                                          final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before,
                                      final int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                resetChapterField();
            }
        });

        final Context context = getContext();
        mBookRepository.getAllBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(final List<Book> books) {
                if (books == null || books.isEmpty()) {
                    Log.e(TAG, "onCreateView: Empty Books list returned from Repository");
                    return;
                }
                // create a array of all book names
                String bookNames[] = new String[books.size()];
                int i = -1;
                for (final Book book : books) {
                    bookNames[++i] = book.getBookName();
                }

                // use the name array for book autocomplete
                mBook.setAdapter(new ArrayAdapter<>(
                    context, android.R.layout.simple_list_item_1, bookNames));
                Log.d(TAG, "onCreate: setup autocomplete of " + bookNames.length + " BookNames");
            }
        });

        mChapter = view.findViewById(R.id.book_screen_chapter);
        mChapter.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                switch (v.getId()) {
                    case R.id.book_screen_book:
                        Log.d(TAG, "book_field hasFocus [" + hasFocus + "]");
                        break;
                    case R.id.book_screen_chapter:
                        if (hasFocus) { //chapter input field gained focus
                            handleInteractionChapterFieldGainFocus();
                        } else { // chapter input field is losing focus
                            resetChapterFieldHint();
                        }
                        break;
                    default:
                        Log.e(TAG, "onFocusChange: " + getString(R.string.msg_unexpected));
                }
            }
        });

        // create click listener for the Navigate button
        view.findViewById(R.id.book_screen_navigate)
            .setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    handleInteractionNavigate();
                }
            });

        mListView = view.findViewById(R.id.book_screen_books_list);

        BookRepository repository = ViewModelProviders.of(this).get(BookRepository.class);
        repository.getAllBooks().observe(this, new Observer<List<Book>>() {

            @Override
            public void onChanged(final List<Book> books) {
                if (books == null || books.isEmpty()) {
                    showErrorScreen();
                } else {
                    updateScreen(books);
                }
            }
        });

        return view;
    }

    private void updateScreen(final List<Book> books) {
        Log.d(TAG, "updateScreen:");

        if (mAdapter == null) {
            mAdapter = new BooksListAdapter();
        }

        mListView.setAdapter(mAdapter);

        if (!isCacheValid(getString(R.string.bible_first_book_name),
                          getString(R.string.bible_last_book_name))) {
            updateList(books);
        }
        mAdapter.notifyDataSetChanged();

    }

    private void handleInteractionNavigate() {
        Log.d(TAG, "handleInteractionNavigate: ");

        final String bookName = getBookInput();
        final Book[] book = new Book[1];
        mBookRepository.getBookUsingName(bookName).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(final List<Book> list) {
                if (list == null || list.isEmpty()) {
                    Log.e(TAG,
                          "handleInteractionNavigate: No Book exists with name [" + bookName + "]");
                    showErrorInvalidBookInput();
                    return;
                }
                book[0] = list.get(0);
                final int chapter = getChapterInput();
                if (mBookRepository.isChapterValid(chapter, book[0])) {
                    showChapterActivity(book[0].getBookNumber(), chapter);
                } else {
                    showErrorInvalidChapterInput();
                }
            }
        });
    }

    private void resetChapterField() {
        mChapter.setText(null);
        mChapter.setAdapter(null);
        mChapter.setError(null);
        resetChapterFieldHint();
    }

    private void resetChapterFieldHint() {
        mChapter.setHint(null);
    }

    private void showChapterActivity(final int book, final int chapter) {
    /*    resetChapterField();
        resetBookField();
        Intent intent = new Intent(this, ChapterScreen.class);
        intent.putExtra(ChapterScreen.BOOK_NUMBER, book);
        intent.putExtra(ChapterScreen.CHAPTER_NUMBER, chapter);
        startActivity(intent);
    */
    }

    private void resetBookField() {
        mBook.setText(null);
        mBook.setError(null);
    }

    private void showErrorInvalidChapterInput() {
        mChapter.setError(getString(R.string.book_screen_err_chapter_invalid));
        mChapter.requestFocus();
    }

    private void showErrorInvalidBookInput() {
        mBook.setError(getString(R.string.book_screen_err_book_invalid));
        mBook.requestFocus();
    }

    private void prepareChapterField(int chapters) {
        resetChapterField();
        String[] values = new String[chapters];
        for (int i = 1; i <= chapters; i++) {
            values[i - 1] = String.valueOf(i);
        }
        mChapter.setAdapter(new ArrayAdapter<>(
            getContext(), android.R.layout.simple_list_item_1, values));
        mChapter.setHint(
            String.format(getString(R.string.book_screen_hint_chapter), chapters));
    }

    private String getBookInput() {
        return mBook.getText().toString().trim();
    }

    private int getChapterInput() {
        try {
            return Integer.parseInt(mChapter.getText().toString().trim());
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "invalid chapter, returning default 1");
            return 1;
        }
    }

    private void handleInteractionChapterFieldGainFocus() {
        final String bookName = getBookInput();

        final Book[] book = new Book[1];
        mBookRepository.getBookUsingName(bookName).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(final List<Book> list) {
                if (list == null || list.isEmpty()) {
                    Log.e(TAG,
                          "handleInteractionChapterFieldGainFocus: No Book exists with name [" +
                          bookName + "]");
                    showErrorInvalidBookInput();
                    return;
                }
                book[0] = list.get(0);
                resetChapterField();
                prepareChapterField(book[0].getBookChapterCount());
            }
        });
    }

    private void showErrorScreen() {
        Log.d(TAG, "showErrorScreen:");
        // FIXME: 23/9/18 show an error message and hide everything else
    }

    @Override
    public void handleInteractionBook(final Book book) {
        Log.d(TAG, "handleInteractionBook: " + book.getBookName());
        showChapterActivity(book.getBookNumber(), 1);
    }

    private boolean isCacheValid(@NonNull final String first_book_name,
                                 @NonNull final String last_book_name) {
        return (!mCache.isEmpty()
                && mCache.size() == Book.EXPECTED_BOOK_COUNT
                && mCache.get(0).getBookName().equalsIgnoreCase(first_book_name)
                && mCache.get(mCache.size() - 1).getBookName().equalsIgnoreCase(
            last_book_name));
    }

    private void updateList(@NonNull final List<Book> books) {
        mCache.clear();
        mCache.addAll(books);
        Log.d(TAG, "updateList : populated [" + mCache.size() + "] books");
    }

    class BooksListAdapter
        extends RecyclerView.Adapter<BookView> {

        @Override
        public BookView onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.item_book, parent, false);
            return new BookView(view);
        }

        @Override
        public void onBindViewHolder(final BookView holder, int position) {
            holder.updateView(mCache.get(position));
        }

        @Override
        public int getItemCount() {
            return mCache.size();
        }

    }

    class BookView
        extends RecyclerView.ViewHolder
        implements ViewHolderOps {

        private final View mView;
        private final TextView mName;
        private final TextView mDetails;
        private Book mBook;

        BookView(View view) {
            super(view);
            mView = view;
            mName = view.findViewById(R.id.item_book_name);
            mDetails = view.findViewById(R.id.item_book_details);
        }

        @Override
        public void updateView(final Object object) {
            mBook = (Book) object;

            // set book title
            mName.setText(String.format(
                getString(R.string.item_book_name_template), mBook.getBookName()));

            // set book details : Chapter Count
            final int chapters = mBook.getBookChapterCount();
            mDetails.setText(getResources().getQuantityString(
                R.plurals.item_book_details_template, chapters, chapters));

            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    handleInteractionBook(mBook);
                }
            });
        }
    }

}
