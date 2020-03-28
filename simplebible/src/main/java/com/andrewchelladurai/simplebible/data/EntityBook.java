package com.andrewchelladurai.simplebible.data;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.andrewchelladurai.simplebible.utils.Utils;

@Entity(tableName = "sb_books", primaryKeys = {"number", "name", "chapters", "verses"})
public
class EntityBook {

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

}
