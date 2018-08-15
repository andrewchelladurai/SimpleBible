package com.andrewchelladurai.simplebible.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.andrewchelladurai.simplebible.data.BookRepository;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;

import java.util.ArrayList;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 13-Aug-2018 @ 12:56 AM.
 */
public class Utilities {

    private static final String    SEPARATOR_IN_REFERENCE      = ":";
    private static final String    SEPARATOR_BETWEEN_REFERENCE = "~";
    private static final String    TAG                         = "Utilities";
    private static final Utilities thisInstance                = new Utilities();

    private Utilities() {/* intentionally left blank*/}

    public static Utilities getInstance() {
        return thisInstance;
    }

    public String createReference(@IntRange(from = 1) final int bookNumber,
                                  @IntRange(from = 1) final int chapterNumber,
                                  @IntRange(from = 1) final int verseNumber) {
        return bookNumber + SEPARATOR_IN_REFERENCE + chapterNumber + SEPARATOR_IN_REFERENCE
               + verseNumber;
    }

    public String[] splitReferences(@NonNull final String references) {
        if (references.isEmpty()) {
            Log.e(TAG, "splitReferences: Empty references");
            return null;
        }
        if (!references.contains(SEPARATOR_IN_REFERENCE)) {
            Log.e(TAG,
                  "splitReferences: reference does not have separator [" + SEPARATOR_IN_REFERENCE
                  + "]");
            return null;
        }
        final String[] verses = references.split(SEPARATOR_BETWEEN_REFERENCE);
        if (verses.length < 1) {
            Log.e(TAG, "splitReferences: verses.length [" + verses.length + "]");
            return null;
        }
        return verses;
    }

    public boolean isValidReference(@NonNull final String reference) {
        if (reference.isEmpty()) {
            Log.e(TAG, "isValidReference: Empty reference passed");
            return false;
        }

        if (!reference.contains(SEPARATOR_IN_REFERENCE)) {
            Log.e(TAG, "isValidReference: on-existent [" + SEPARATOR_IN_REFERENCE
                       + "] in reference[" + reference + "]");
            return false;
        }

        final String[] parts = reference.split(SEPARATOR_IN_REFERENCE);
        if (parts.length != 3) {
            Log.e(TAG, "isValidReference: reference[" + reference + "] does not have 3 parts");
            return false;
        }

        int value;
        for (final String part : parts) {
            try {
                value = Integer.parseInt(part);
                if (value < 1) {
                    Log.e(TAG, "isValidReference: part of reference[" + reference
                               + "] is invalid : < 0");
                    return false;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "isValidReference: part of reference[" + reference + "] is NAN");
                return false;
            }
        }

        return true;
    }

    public int[] splitReference(@NonNull final String reference) {
        final String[] parts = reference.split(SEPARATOR_IN_REFERENCE);
        final int[] sections = new int[parts.length];
        for (int i = 0; i < sections.length; i++) {
            try {
                sections[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                Log.e(TAG, "splitReference: parsing failure", e);
                return null;
            }
        }
        return sections;
    }

    public void hideKeyboard(@NonNull final Context context, @NonNull final View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
            Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isValidChapterNumber(@IntRange(from = 1, to = 66) final int bookNumber,
                                        @IntRange(from = 1) final int chapterNumber) {
        if (chapterNumber == 0) {
            Log.e(TAG, "isValidChapterNumber: invalid chapterNumber[" + chapterNumber + "] == 0");
            return false;
        }
        boolean validated = isValidBookNumber(bookNumber);
        if (!validated) {
            Log.e(TAG, "isValidChapterNumber: invalid bookNumber[" + bookNumber + "]");
            return false;
        }
        final Book book = (Book) BookRepository.getInstance().getCachedRecordUsingKey(bookNumber);
        if (book == null) {
            Log.e(TAG, "isValidChapterNumber: ideally, this should not print");
            Log.e(TAG, "isValidChapterNumber: invalid bookNumber[" + bookNumber + "]");
            return false;
        }
        final int maxChapterNumber = book.getChapters();
        if (chapterNumber > maxChapterNumber) {
            Log.e(TAG, "isValidChapterNumber: invalid chapterNumber[" + chapterNumber
                       + "] > maxChapterNumber [" + maxChapterNumber + "] for book[" + book
                           .getName() + "]");
            return false;
        }
        return true;
    }

    @Nullable
    public String getBookName(@IntRange(from = 1, to = 66) final int bookNumber) {
        if (isValidBookNumber(bookNumber)) {
            final Book book = getBookUsingNumber(bookNumber);
            if (null != book) {
                return book.getName();
            }
        }
        return null;
    }

    public boolean isValidBookNumber(@IntRange(from = 1, to = 66) final int bookNumber) {
        return (null != getBookUsingNumber(bookNumber));
    }

    public Book getBookUsingNumber(@NonNull final Integer bookNumber) {
        return (Book) BookRepository.getInstance().getCachedRecordUsingKey(bookNumber);
    }

    @Nullable
    public String createBookmarkReference(@NonNull final ArrayList<Verse> list) {
        if (list.isEmpty()) {
            Log.e(TAG, "createBookmarkReference: Empty list passed");
            return null;
        }
        final StringBuilder value = new StringBuilder();
        for (Verse verse : list) {
            value.append(createReference(verse.getBook(), verse.getChapter(), verse.getVerse()))
                 .append(SEPARATOR_BETWEEN_REFERENCE);
        }
        value.deleteCharAt(value.lastIndexOf(SEPARATOR_BETWEEN_REFERENCE));
        return value.toString();
    }

    public boolean isValidBookmarkReference(@NonNull final String references) {
        Log.d(TAG, "isValidBookmarkReference() called with [" + references + "]");
        if (references.isEmpty()) {
            Log.e(TAG, "isValidBookmarkReference: empty references");
            return false;
        }

        if (!references.contains(SEPARATOR_IN_REFERENCE)) {
            Log.e(TAG, "isValidBookmarkReference: non-existent [" + SEPARATOR_IN_REFERENCE + "]");
            return false;
        }

        String[] verseReferences = references.split(SEPARATOR_BETWEEN_REFERENCE);
        for (final String verseReference : verseReferences) {
            if (!isValidReference(verseReference)) {
                Log.e(TAG, "isValidBookmarkReference: one verse reference ["
                           + verseReference + "] is invalid");
                return false;
            }
        }

        return true;
    }
}
