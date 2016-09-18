package com.andrewchelladurai.simplebible.interaction;

import com.andrewchelladurai.simplebible.model.BooksList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com on 09-Sep-2016 @ 1:11 AM
 */
public interface BooksTabOperations
        extends BasicOperations {

    String[] getBookNameChapterCountArray();

    void bookItemClicked(BooksList.BookItem mItem);

    String getBookNameTemplateString();

    String chapterCountTemplateString(int count);
}
