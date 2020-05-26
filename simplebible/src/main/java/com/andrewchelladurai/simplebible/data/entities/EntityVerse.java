package com.andrewchelladurai.simplebible.data.entities;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.andrewchelladurai.simplebible.utils.Utils;

@Entity(tableName = "sb_verses", primaryKeys = {"book", "chapter", "verse"})
public class EntityVerse {

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

  public EntityVerse(@NonNull final String translation,
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

}
