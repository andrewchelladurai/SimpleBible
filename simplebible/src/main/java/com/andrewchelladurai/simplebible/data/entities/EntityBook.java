package com.andrewchelladurai.simplebible.data.entities;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.Objects;

@Entity(tableName = "sb_books", primaryKeys = {"number", "name", "chapters", "verses"})
public
class EntityBook
    implements Comparable {

  @NonNull
  @ColumnInfo(name = "description")
  private final String description;

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  @ColumnInfo(name = "number")
  private final int number;

  @NonNull
  @ColumnInfo(name = "name")
  private final String name;

  @IntRange(from = 1)
  @ColumnInfo(name = "chapters")
  private final int chapters;

  @IntRange(from = 1)
  @ColumnInfo(name = "verses")
  private final int verses;

  @NonNull
  @ColumnInfo(name = "testament")
  private final String testament;

  public EntityBook(@NonNull final String description,
                    @IntRange(from = 1, to = Utils.MAX_BOOKS) final int number,
                    @NonNull final String name,
                    @IntRange(from = 1) final int chapters,
                    @IntRange(from = 1) final int verses,
                    @NonNull final String testament) {
    this.description = description;
    this.number = number;
    this.name = name;
    this.chapters = chapters;
    this.verses = verses;
    this.testament = testament;
  }

  @NonNull
  public String getDescription() {
    return description;
  }

  @IntRange(from = 1, to = Utils.MAX_BOOKS)
  public int getNumber() {
    return number;
  }

  @NonNull
  public String getName() {
    return name;
  }

  @IntRange(from = 1)
  public int getChapters() {
    return chapters;
  }

  @IntRange(from = 1)
  public int getVerses() {
    return verses;
  }

  @NonNull
  public String getTestament() {
    return testament;
  }

  @Override
  public boolean equals(final Object newObj) {
    if (this == newObj) {
      return true;
    }

    if (newObj == null || this.getClass() != newObj.getClass()) {
      return false;
    }

    final EntityBook that = (EntityBook) newObj;
    return this.getNumber() == that.getNumber()
           && this.getChapters() == that.getChapters()
           && this.getVerses() == that.getVerses()
           && this.getName().equals(that.getName())
           && this.getTestament().equals(that.getTestament());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getNumber(), getName(), getChapters(), getVerses(), getTestament());
  }

  @NonNull
  @Override
  public String toString() {
    return getNumber()
           + "-" + getName()
           + "-" + getDescription()
           + "-" + getChapters()
           + "-" + getVerses()
           + "-" + getTestament();
  }

  @Override
  public int compareTo(@NonNull final Object that) {
    return this.getNumber() - ((EntityBook) that).getNumber();
  }

}
