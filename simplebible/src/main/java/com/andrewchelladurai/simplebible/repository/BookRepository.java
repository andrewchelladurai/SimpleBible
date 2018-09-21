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

import com.andrewchelladurai.simplebible.data.Book;
import com.andrewchelladurai.simplebible.data.SbDatabase;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 22-Sep-2018 @ 1:22 AM
 */
public class BookRepository
    extends AndroidViewModel {

    public BookRepository(final Application application) {
        super(application);
    }

    public LiveData<List<Book>> getBookName(final int bookNumber) {
        return SbDatabase.getInstance(getApplication()).getBookDao().getBookUsingNumber(bookNumber);
    }
}
