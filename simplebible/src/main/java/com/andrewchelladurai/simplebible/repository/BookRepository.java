/*
 *
 * This file 'BookRepository.java' is part of SimpleBible :
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

package com.andrewchelladurai.simplebible.repository;

import android.app.Application;
import android.util.Log;

import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.data.BookDao;
import com.andrewchelladurai.simplebible.data.SbDatabase;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 22-Sep-2018 @ 1:22 AM
 */
public class BookRepository
    extends AndroidViewModel {

    private static final String TAG = "BookRepository";
    private static BookDao mBookDao;

    public BookRepository(final Application application) {
        super(application);
        mBookDao = SbDatabase.getInstance(getApplication()).getBookDao();
        Log.d(TAG, "BookRepository: init done [" + (mBookDao != null) + "]");
    }

    public LiveData<List<Book>> getBookUsingNumber(final int bookNumber) {
        return mBookDao.getBookUsingNumber(bookNumber);
    }

    public LiveData<List<Book>> getAllBooks() {
        return mBookDao.getAllBookDetails();
    }

    public LiveData<List<Book>> getBookUsingName(@NonNull final String bookName) {
        return mBookDao.getBookUsingName(bookName);
    }

    public boolean isChapterValid(@IntRange(from = 1) final int chapter,
                                  @NonNull final Book book) {
        return (chapter < 1 || chapter > book.getBookChapterCount());
    }
}
