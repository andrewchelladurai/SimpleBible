package com.andrewchelladurai.simplebible.data;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.Objects;

@Entity(tableName = "sb_verses", primaryKeys = {"book", "chapter", "verse"})
public
class EntityVerse
    implements Comparable {

  @NonNull
  @ColumnInfo(name = "translation")
  private final String translation;

  @ColumnInfo(name = "book")
  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  private final int book;

  @ColumnInfo(name = "chapter")
  @IntRange(from = 1)
  private final int chapter;

  @ColumnInfo(name = "verse")
  @IntRange(from = 1)
  private final int verse;

  @NonNull
  @ColumnInfo(name = "text")
  private final String text;

  EntityVerse(@NonNull final String translation,
              @IntRange(from = 1, to = Utils.MAX_BOOKS) final int book,
              @IntRange(from = 1) final int chapter,
              @IntRange(from = 1) final int verse,
              @NonNull final String text) {
    this.translation = translation;
    this.book = book;
    this.chapter = chapter;
    this.verse = verse;
    this.text = text;
  }

  @NonNull
  public String getTranslation() {
    return translation;
  }

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  public int getBook() {
    return book;
  }

  @IntRange(from = 1)
  public int getChapter() {
    return chapter;
  }

  @IntRange(from = 1)
  public int getVerse() {
    return verse;
  }

  @NonNull
  public String getText() {
    return text;
  }

  @Override
  public boolean equals(@Nullable final Object o) {

    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final EntityVerse that = (EntityVerse) o;
    return this.book == that.book
           && this.chapter == that.chapter
           && this.verse == that.verse
           && this.translation.equals(that.translation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(translation, book, chapter, verse);
  }

  @Override
  public int compareTo(@NonNull final Object o) {
    final EntityVerse that = (EntityVerse) o;

    final int thisPosition = Integer.parseInt(this.book + "" + this.chapter + "" + this.verse);
    final int thatPosition = Integer.parseInt(that.book + "" + that.chapter + "" + that.verse);

    return thisPosition - thatPosition;
  }

}
