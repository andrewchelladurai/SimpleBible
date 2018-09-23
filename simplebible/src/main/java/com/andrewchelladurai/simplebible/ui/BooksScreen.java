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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.ops.BooksScreenOps;
import com.andrewchelladurai.simplebible.ops.ViewHolderOps;
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

    private TextView mBook;
    private TextView mChapter;
    private RecyclerView mListView;
    private BooksListAdapter mAdapter;

    @SuppressWarnings("WeakerAccess")
    public BooksScreen() {
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.books_screen, container, false);

        mBook = view.findViewById(R.id.book_screen_book);
        mChapter = view.findViewById(R.id.book_screen_chapter);

        // create click listener for the Navigate button
        view.findViewById(R.id.book_screen_navigate).setOnClickListener(new OnClickListener() {

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

    private void showErrorScreen() {
        Log.d(TAG, "showErrorScreen:");
        // FIXME: 23/9/18 show an error message and hide everything else
    }

    @Override
    public void handleInteractionBook(final Book book) {
        Log.d(TAG, "handleInteractionBook: " + book.getBookName());
    }

    private void handleInteractionNavigate() {
        Log.d(TAG, "handleInteractionNavigate:");
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
            mName = view.findViewById(R.id.book_name);
            mDetails = view.findViewById(R.id.book_details);
        }

        @Override
        public void updateView(final Object object) {
            mBook = (Book) object;
            mName.setText(mBook.getBookName());
            mDetails.setText(String.valueOf(mBook.getBookChapterCount()));

            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    handleInteractionBook(mBook);
                }
            });
        }
    }

}
