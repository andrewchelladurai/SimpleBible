package com.andrewchelladurai.simplebible.interaction;

import com.andrewchelladurai.simplebible.adapter.BooksList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 09-Sep-2016 @ 1:11 AM
 */
public interface BooksListFragmentInterface
        extends BasicOperations {

    String[] getBookNameChapterCountArray();

    void handleInteraction(BooksList.BookItem mItem);
}
