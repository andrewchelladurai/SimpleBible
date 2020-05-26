package com.andrewchelladurai.simplebible.data;

import android.text.Spanned;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;

import com.andrewchelladurai.simplebible.data.entities.EntityBook;
import com.andrewchelladurai.simplebible.data.entities.EntityVerse;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.Objects;

public class Verse
    implements Comparable {

  public static final int MAX_VERSES = 31098;

  public static final String REFERENCE_SEPARATOR = ":";

  private static final String TAG = "Verse";

  @NonNull
  private final String translation;

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  private final int bookNumber;

  @IntRange(from = 1)
  private final int chapterNumber;

  @IntRange(from = 1)
  private final int verseNumber;

  @NonNull
  private final String verseText;

  @NonNull
  private final EntityBook book;

  public Verse(@NonNull final EntityVerse verse, @NonNull final EntityBook entityBook) {
    translation = verse.getTranslation();
    bookNumber = verse.getBook();
    chapterNumber = verse.getChapter();
    verseNumber = verse.getVerse();
    verseText = verse.getText();
    book = entityBook;
  }

  @Nullable
  public static int[] splitReference(@NonNull final String reference) {

    if (!validateReference(reference)) {
      return null;
    }

    final String[] parts = reference.split(REFERENCE_SEPARATOR);
    return new int[]{
        Integer.parseInt(parts[0]),
        Integer.parseInt(parts[1]),
        Integer.parseInt(parts[2]),
        };
  }

  public static boolean validateReference(@NonNull final String reference) {
    if (reference.isEmpty()) {
      Log.e(TAG, "validateVerseReference:", new IllegalArgumentException(
          "Verse reference [" + reference + "] is empty."
      ));
      return false;
    }

    if (!reference.contains(REFERENCE_SEPARATOR)) {
      Log.e(TAG, "validateVerseReference:", new IllegalArgumentException(
          "Verse reference [" + reference + "] has no separators [" + REFERENCE_SEPARATOR + "]"
      ));
      return false;
    }

    final String[] part = reference.split(REFERENCE_SEPARATOR);
    if (part.length != 3) {
      Log.e(TAG, "validateVerseReference:", new IllegalArgumentException(
          "Verse reference [" + reference + "] does not contain 3 parts"
      ));
    }

    @IntRange(from = 1, to = Utils.MAX_BOOKS) final int book;
    @IntRange(from = 1) final int chapter;
    @IntRange(from = 1) final int verse;

    try {
      book = Integer.parseInt(part[0]);
      chapter = Integer.parseInt(part[1]);
      verse = Integer.parseInt(part[2]);
    } catch (NumberFormatException nfe) {
      Log.e(TAG, "validateVerseReference: book, chapter or book is NAN", nfe);
      return false;
    }

    if (book < 1 || book > Utils.MAX_BOOKS) {
      Log.e(TAG, "validateVerseReference:", new IllegalArgumentException(
          "Verse reference [" + reference + "] has an invalid book number [" + book + "]"
      ));
      return false;
    }

    if (chapter < 1) {
      Log.e(TAG, "validateVerseReference:", new IllegalArgumentException(
          "Verse reference [" + reference + "] has an invalid chapter number [" + chapter + "]"
      ));
      return false;
    }

    if (verse < 1) {
      Log.e(TAG, "validateVerseReference:", new IllegalArgumentException(
          "Verse reference [" + reference + "] has an invalid verse number [" + verse + "]"
      ));
      return false;
    }

    return true;
  }

  @NonNull
  public static String createReference(@IntRange(from = 1, to = Utils.MAX_BOOKS) int book,
                                       @IntRange(from = 1) int chapter,
                                       @IntRange(from = 1) int verse) {
    if (book < 1 || chapter < 1 || verse < 1) {
      Log.e(TAG, "createVerseReference:",
            new IllegalArgumentException("one of the passed values is < 1\n"
                                         + "book =[" + book + "], "
                                         + "chapter =[" + chapter + "], "
                                         + "verse = [" + verse + "]"));
      return "";
    }
    return book + REFERENCE_SEPARATOR + chapter + REFERENCE_SEPARATOR + verse;
  }

  @NonNull
  public String getTranslation() {
    return translation;
  }

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  public int getBookNumber() {
    return bookNumber;
  }

  @IntRange(from = 1)
  public int getChapterNumber() {
    return chapterNumber;
  }

  @IntRange(from = 1)
  public int getVerseNumber() {
    return verseNumber;
  }

  @NonNull
  public String getVerseText() {
    return verseText;
  }

  @NonNull
  public EntityBook getBook() {
    return book;
  }

  @Override
  public boolean equals(@Nullable final Object o) {

    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Verse that = (Verse) o;
    return this.bookNumber == that.bookNumber
           && this.chapterNumber == that.chapterNumber
           && this.verseNumber == that.verseNumber
           && this.verseText.equals(that.verseText)
           && this.translation.equals(that.translation)
           && this.book.equals(that.book);
  }

  @Override
  public int hashCode() {
    return Objects.hash(translation, bookNumber, chapterNumber, verseNumber, verseText, book);
  }

  @Override
  public int compareTo(@NonNull final Object o) {
    final Verse that = (Verse) o;

    final int thisPosition = Integer.parseInt(
        this.bookNumber + "" + this.chapterNumber + "" + this.verseNumber);
    final int thatPosition = Integer.parseInt(
        that.bookNumber + "" + that.chapterNumber + "" + that.verseNumber);

    return thisPosition - thatPosition;
  }

  @NonNull
  public String getReference() {
    return bookNumber
           + REFERENCE_SEPARATOR
           + chapterNumber
           + REFERENCE_SEPARATOR
           + verseNumber;
  }

  @NonNull
  public Spanned getFormattedContentForSearchResult(@NonNull final String template) {
    if (template.isEmpty()) {
      Log.e(TAG, "getFormattedContentForSearchResult:",
            new IllegalArgumentException("Empty template value passed, returning empty string"));
      return HtmlCompat.fromHtml("", HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    return HtmlCompat.fromHtml(
        String.format(template, book.getName(), chapterNumber, verseNumber, verseText),
        HtmlCompat.FROM_HTML_MODE_COMPACT);
  }

  @NonNull
  public Spanned getFormattedContentForBookmark(@NonNull final String template) {
    if (template.isEmpty()) {
      Log.e(TAG, "getFormattedContentForSearchResult:",
            new IllegalArgumentException("Empty template value passed, returning empty string"));
      return HtmlCompat.fromHtml("", HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    return HtmlCompat.fromHtml(
        String.format(template, book.getName(), chapterNumber, verseNumber, verseText),
        HtmlCompat.FROM_HTML_MODE_COMPACT);
  }

  @NonNull
  public Spanned getFormattedContentForShareChapterVerse(@NonNull final String template) {
    if (template.isEmpty()) {
      Log.e(TAG, "getFormattedContentForSearchResult:",
            new IllegalArgumentException("Empty template value passed, returning empty string"));
      return HtmlCompat.fromHtml("", HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    return HtmlCompat.fromHtml(
        String.format(template, verseNumber, verseText),
        HtmlCompat.FROM_HTML_MODE_COMPACT);
  }

  @NonNull
  public Spanned getFormattedContentForChapter(@NonNull final String template) {
    if (template.isEmpty()) {
      Log.e(TAG, "getFormattedContentForSearchResult:",
            new IllegalArgumentException("Empty template value passed, returning empty string"));
      return HtmlCompat.fromHtml("", HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    return HtmlCompat.fromHtml(
        String.format(template, verseNumber, verseText),
        HtmlCompat.FROM_HTML_MODE_COMPACT);
  }

}
